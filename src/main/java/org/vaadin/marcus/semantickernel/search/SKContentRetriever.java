package org.vaadin.marcus.semantickernel.search;

import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.data.vectorsearch.VectorSearchResult;
import com.microsoft.semantickernel.data.vectorstorage.VectorStoreRecordCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class SKContentRetriever {

    Logger log = LoggerFactory.getLogger(SKContentRetriever.class);

    private final OpenAITextEmbeddingGenerationService embeddingGenerationService;
    private final VectorStoreRecordCollection<String, Document> inMemoryVectorStore;

    public SKContentRetriever(OpenAITextEmbeddingGenerationService embeddingGenerationService, VectorStoreRecordCollection<String, Document> inMemoryVectorStore) {
        this.embeddingGenerationService = embeddingGenerationService;
        this.inMemoryVectorStore = inMemoryVectorStore;
    }

    public Mono<List<String>> searchTermsAndConditions(String query) {
        log.debug("invoked search for policies for query {}", query);

        var searchResults =
                embeddingGenerationService.generateEmbeddingsAsync(Collections.singletonList(query))
                .flatMap(r -> inMemoryVectorStore.searchAsync(r.get(0).getVector(), null));

        return searchResults.flatMapMany(Flux::fromIterable)
                .map(VectorSearchResult::getRecord)
                .map(Document::getContent)
                .collectList();
    }
}
