package org.vaadin.marcus.search;

import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.data.vectorstorage.VectorStoreRecordCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class IndexTermsAndConditions implements ApplicationListener<ContextRefreshedEvent> {

    Logger log = LoggerFactory.getLogger(IndexTermsAndConditions.class);

    @Value("classpath:terms-of-service.txt")
    Resource resourceFile;

    private final OpenAITextEmbeddingGenerationService embeddingGenerationService;
    private final VectorStoreRecordCollection<String, TermsAndConditions> inMemoryVectorStore;

    public IndexTermsAndConditions(OpenAITextEmbeddingGenerationService embeddingGenerationService, VectorStoreRecordCollection<String, TermsAndConditions> inMemoryVectorStore) {
        this.embeddingGenerationService = embeddingGenerationService;
        this.inMemoryVectorStore = inMemoryVectorStore;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            String termsAndConditions = resourceFile.getContentAsString(StandardCharsets.UTF_8);

            Mono<List<String>> savedCollection = inMemoryVectorStore
                    .createCollectionIfNotExistsAsync()
                    .then(storeData(inMemoryVectorStore, embeddingGenerationService, termsAndConditions));

            savedCollection.subscribe(
                    list -> log.info("Terms and conditions indexed successfully"),
                    error -> log.error("Error indexing terms and conditions", error),
                    () -> log.info("Indexing process completed")
            );

        } catch (IOException e) {
            log.error("Could not read the terms and conditions file", e);
        }
    }

    private Mono<List<String>> storeData(VectorStoreRecordCollection<String, TermsAndConditions> collection,
                                         OpenAITextEmbeddingGenerationService embeddingGenerationService,
                                         String termsAndConditions) {
        return Flux.fromIterable(chunkData(termsAndConditions).entrySet())
                .flatMap(entry -> {
                    log.debug("Saving to memory> ' {}: {}", entry.getKey(), entry.getValue());

                    return embeddingGenerationService
                            .generateEmbeddingsAsync(Collections.singletonList(entry.getValue()))
                            .flatMap(embeddings -> {
                                TermsAndConditions termsCond = new TermsAndConditions(
                                        entry.getKey(),
                                        entry.getValue(),
                                        embeddings.get(0).getVector());
                                return collection.upsertAsync(termsCond, null);
                            });
                })
                .collectList();
    }

    private Map<String, String> chunkData(String termsAndConditions) {
        int chunkSize = 250;
        int overlapSize = 100;

        Map<String, String> chunks = new HashMap<>();
        int length = termsAndConditions.length();

        for (int i = 0; i < length; i += (chunkSize - overlapSize)) {
            int end = Math.min(length, i + chunkSize);
            chunks.put(String.valueOf(i), termsAndConditions.substring(i, end));
        }

        return chunks;
    }
}