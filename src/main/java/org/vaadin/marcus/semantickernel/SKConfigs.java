package org.vaadin.marcus.semantickernel;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypeConverter;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.vaadin.marcus.service.BookingDetails;

@Configuration
public class SKConfigs {

    @Value("${sk.openai.key}") String apiKey;
    @Value("${sk.azure.openai.endpoint}") String endpoint;
    @Value("${sk.deployment.name}") String deploymentName;

    private OpenAIAsyncClient openAIAsyncClient() {
        if(StringUtils.hasLength(endpoint)) {
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
    public ContextVariableTypeConverter<BookingDetails> bookingDetailsTypeConverter(ObjectMapper objectMapper) {
        return new ContextVariableTypeConverter<>(
                BookingDetails.class,
                objectToObject -> (BookingDetails) objectToObject,
                Record::toString,
                    // THE IDEAL implementation would be proper deserializable type, example to JSON below
                    /*
                    {
                        try {
                            String json = objectMapper.writeValueAsString(o);
                            log.debug("converting from object to json {}", json);
                            return json;
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }*/
                stringToObject -> null
                // THE IDEAL implementation would be proper serialization, example from JSON below
                /*
                {
                    try {
                        log.debug("converting from json to object {}", s);
                        return objectMapper.readValue(s, BookingDetails.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }*/
        );
    }
}
