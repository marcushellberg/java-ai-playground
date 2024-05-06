# Java AI library comparison app

![A browser window with a chatbot and a data grid displaying flight booking details](screenshot.jpg)

This app is an AI-powered customer support application that:

- Has access to terms and conditions (retrieval augmented generation, RAG)
- Can access tools (Java methods) to perform actions
- Uses an LLM to interact with the user

The application includes implementations for: 

- [LangChain4j](https://github.com/langchain4j/langchain4j) in the `main` branch
- [Spring AI](https://spring.io/projects/spring-ai/) in the `spring-ai` branch (thanks to [@tzolov](https://github.com/tzolov)!)
- [Semantic Kernel](https://github.com/microsoft/semantic-kernel) in the `semantic-kernel` branch (thanks to [@sohamda](https://github.com/sohamda)!)

The UI is built using [Vaadin Hilla](https://vaadin.com) and the backend is built using Spring Boot.

## Requirements
- Java 17+
- OpenAI API key in `OPENAI_API_KEY` environment variable
- For Semantic Kernel & Azure OpenAI instance set also `AZURE_OPENAI_ENDPOINT` environment variable
- For Semantic Kernel set also `OPENAI_MODEL_NAME` environment variable (this is deployment name for Azure instance and model name in case of OpenAI instance)

### Semantic Kernel - Whys' and Whats'
- Everything in SK starts with initializing a 'Kernel'. This object has all the 'Functions' attached and also the 'AI Service' that must be used to perform tasks with an LLM.
- An 'AI Service' in SK can be a Text completion service or a Chat completion service. In this repo, a chat completion type is used to support a chatbot.
- SK handles any task by executing a 'Function'. This can be calling an external API, invoking an embedded memory store (vector db) or even performing business logic.
  - In the definition of the functions, all inputs, outputs and function goals are defined explicitly, this is to help LLM to select the right function(s) to execute and extract information require to invoke the function(s).
  - One can invoke these functions manually.
  - But also can let the Kernel decides which function(s) is/are appropriate for a given task. In this example InvocationContext object is used to let the kernel invoke functions automatically.
- If a 'Function' wants to return a custom object, a ContextVariableTypeConverter needs to be defined, where one can specify custom functions to call for Object to Object, Object to String and vice-versa conversions. 
  - This gives the flexibility to convert objects to the format one need, such as JSON, XML or any other format.
  - This is useful when there are multiple functions will be invoked one after another, and one need to pass the object in different format among them.
- Exceptions in 'Functions' are handled manually here, to give LLM the info about something went wrong. 
  - That's why some of the 'Functions' have return type String.
- SK doesn't have an in-memory embedding store, the framework supports Azure AI Search, but one can use any embedding memory store. 
  - Since everything one can define as 'Functions', all search operations on a memory store can be coded in a 'Function'. (for example Weaviate DB)
  - On a more complex implementation one can always invoke the search operation before performing any other task(s), to erich the context of LLM.
- Kernel doesn't store or manage ChatHistory, nor the ChatCompletion AI Service. 
  - Chat history is managed separately and passed to Kernel invocation on every chat message.
  - One also needs to manage the responses from a LLM and add them to Chat history. since that is not done automatically.
  - In this example, also a custom SKChatManager is used to support multi-tenancy. (this is a simple implementation, ideally this object should also hold session info, user info etc.)
- SK right now 'only' supports OpenAI and Azure OpenAI instances. (as of April'24).

## Running
Run the app by running `Application.java` in your IDE or `mvn` in the command line.

## Thanks
This demo was inspired by the [LangChain4jCustomer Support Agent example](https://github.com/langchain4j/langchain4j-examples/tree/main/spring-boot-example/src/main/java/dev/langchain4j/example).

I want to thank the LangChain4j, Spring AI, and Microsoft teams for their support in building this demo.
Especially, I want to thank [@tzolov](https://github.com/tzolov) from The Spring AI team for his help in building the Spring AI implementation and [@sohamda](https://github.com/sohamda) from Microsoft for the Semantic Kernel implementation.