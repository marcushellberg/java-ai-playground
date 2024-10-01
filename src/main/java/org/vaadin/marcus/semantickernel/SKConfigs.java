package org.vaadin.marcus.semantickernel;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.aiservices.openai.textembedding.OpenAITextEmbeddingGenerationService;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.data.VolatileVectorStore;
import com.microsoft.semantickernel.data.VolatileVectorStoreRecordCollectionOptions;
import com.microsoft.semantickernel.data.vectorstorage.VectorStoreRecordCollection;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.vaadin.marcus.semantickernel.search.Document;
import org.vaadin.marcus.service.BookingDetails;

import static org.reflections.Reflections.log;

@Configuration
public class SKConfigs {

    @Value("${sk.openai.key}")
    String apiKey;
    @Value("${sk.azure.openai.endpoint}")
    String endpoint;
    @Value("${sk.deployment.name}")
    String deploymentName;
    @Value("${sk.azure.openai.embedding.model}")
    String embeddingModel;
    @Value("${sk.azure.openai.embedding.dimension}")
    String embeddingDimension;

    private OpenAIAsyncClient openAIAsyncClient() {
        if (StringUtils.hasLength(endpoint)) {
            return new OpenAIClientBuilder()
                    .endpoint(endpoint)
                    .credential(new AzureKeyCredential(apiKey))
                    .buildAsyncClient();
        }
        return new OpenAIClientBuilder()
                .credential(new KeyCredential(apiKey))
                .buildAsyncClient();
    }

    private ChatCompletionService chatCompletionService() {
        return OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(openAIAsyncClient())
                .withModelId(deploymentName)
                .build();
    }

    @Bean
    public Kernel kernel(SKFunctions skFunctions) {
        KernelPlugin kernelPlugin = KernelPluginFactory.createFromObject(
                skFunctions,
                "InformationFinder");

        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService())
                .withPlugin(kernelPlugin)
                .build();
    }

    @Bean
    public OpenAITextEmbeddingGenerationService embeddingGenerationService() {
        return OpenAITextEmbeddingGenerationService.builder()
                .withOpenAIAsyncClient(openAIAsyncClient())
                .withDeploymentName(embeddingModel)
                .withModelId(embeddingModel)
                .withDimensions(Integer.parseInt(embeddingDimension))
                .build();
    }

    @Bean
    public VectorStoreRecordCollection<String, Document> inMemoryVectorStore() {
        VolatileVectorStore volatileVectorStore = new VolatileVectorStore();

        var collectionName = "terms-and-conditions";

        return volatileVectorStore.getCollection(collectionName,
                VolatileVectorStoreRecordCollectionOptions.<Document>builder()
                        .withRecordClass(Document.class)
                        .build());
    }

    @Bean
    public ContextVariableTypeConverter<BookingDetails> bookingDetailsTypeConverter(ObjectMapper objectMapper) {
        return new ContextVariableTypeConverter<>(
                BookingDetails.class,
                objectToObject -> (BookingDetails) objectToObject,
                o -> {
                    try {
                        String json = objectMapper.writeValueAsString(o);
                        log.debug("converting from object to json {}", json);
                        return json;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                s -> {
                    try {
                        log.debug("converting from json to object {}", s);
                        return objectMapper.readValue(s, BookingDetails.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
