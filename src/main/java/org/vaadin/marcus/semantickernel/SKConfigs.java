package org.vaadin.marcus.semantickernel;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class SKConfigs {

    @Value("${sk.openai.key}") String apiKey;
    @Value("${sk.azure.openai.endpoint}") String endpoint;
    @Value("${sk.deployment.name}") String deploymentName;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
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

    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(openAIAsyncClient)
                .withModelId(deploymentName)
                .build();
    }

    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService, SKPlugins skPlugins) {
        KernelPlugin kernelPlugin = KernelPluginFactory.createFromObject(
                skPlugins,
                "InformationFinder");

        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(kernelPlugin)
                .build();
    }
    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true)).build();
    }

}
