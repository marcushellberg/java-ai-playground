package org.vaadin.marcus.springai;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class SpringAiConfig {
    // In the real world, ingesting documents would often happen separately, on a CI server or similar
    @Bean
    CommandLineRunner ingestDocsForSpringAi(
            VectorStore vectorStore,
            ResourceLoader resourceLoader) throws IOException {

        return args -> {

            Resource resource = resourceLoader.getResource("classpath:terms-of-service.txt");

            // Ingest the document into the vector store
            vectorStore
                    .accept(new TokenTextSplitter(30, 20, 1, 10000, true)
                            .apply(new TextReader(resource).get()));
        };
    }

    @Bean
    public VectorStore vectorStore(EmbeddingClient embeddingClient) {
        return new SimpleVectorStore(embeddingClient);
    }
}
