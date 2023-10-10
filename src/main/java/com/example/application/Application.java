package com.example.application;

import com.example.application.services.BookingTools;
import com.example.application.services.CustomerSupportAgent;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.retriever.Retriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Path;

import static dev.langchain4j.data.document.FileSystemDocumentLoader.loadDocument;
import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "customer-support-agent")
public class Application implements AppShellConfigurator {


    @Bean
    CustomerSupportAgent customerSupportAgent(StreamingChatLanguageModel chatLanguageModel,
                                              BookingTools bookingTools,
                                              Retriever<TextSegment> retriever) {
        return AiServices.builder(CustomerSupportAgent.class)
                .streamingChatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(bookingTools)
                .retriever(retriever)
                .build();
    }

    @Bean
    StreamingChatLanguageModel chatLanguageModel(@Value("${openai.api.key") String apiKey) {
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .modelName(GPT_3_5_TURBO)
                .build();
    }

    @Bean
    Retriever<TextSegment> retriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {

        // You will need to adjust these parameters to find the optimal setting, which will depend on two main factors:
        // - The nature of your data
        // - The embedding model you are using
        int maxResultsRetrieved = 1;
        double minScore = 0.6;

        return EmbeddingStoreRetriever.from(embeddingStore, embeddingModel, maxResultsRetrieved, minScore);
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel, ResourceLoader resourceLoader) throws IOException {

        // Normally, you would already have your embedding store filled with your data.
        // However, for the purpose of this demonstration, we will:

        // 1. Create an in-memory embedding store
        var embeddingStore = new InMemoryEmbeddingStore<TextSegment>();

        // 2. Load an example document ("Miles of Smiles" terms of use)
        Resource resource = resourceLoader.getResource("classpath:terms-of-use.txt");
        var termsOfUse = loadDocument(resource.getFile().toPath());

        // 3. Split the document into segments 100 tokens each
        // 4. Convert segments into embeddings
        // 5. Store embeddings into embedding store
        // All this can be done manually, but we will use EmbeddingStoreIngestor to automate this:
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(100, 0, new OpenAiTokenizer(GPT_3_5_TURBO));
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(termsOfUse);

        return embeddingStore;
    }

    @Bean
    EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
