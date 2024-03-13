# Java AI library comparison app

This app is an AI-powered customer support application that:

- Has access to terms and conditions (retrieval augmented generation, RAG)
- Can access tools (Java methods) to perform actions
- Uses an LLM to interact with the user

The application includes implementations for: 

- [LangChain4j](https://github.com/langchain4j/langchain4j) in the `langchain4j` package
- [Spring AI](https://spring.io/projects/spring-ai/) in the `springai` package (thanks to [@tzolov](https://github.com/tzolov)!)

## Requirements
- Java 17+
- OpenAI API key in `OPENAI_API_KEY` environment variable

## Running
Run the app by running `Application.java` in your IDE or `mvn` in the command line.