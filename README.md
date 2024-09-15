# Funnair AI-Powered Customer Support (Enhanced Fork)

![A browser window with a chatbot and a data grid displaying flight booking details](screenshot.jpg)

This app demonstrates an enhanced AI-powered customer support system for Funnair, a fictional airline. It builds upon the original project with additional features and improvements.

## Key Enhancements

- **Booking Status Management**: Added functionality to track and update booking statuses.
- **In-Memory Chat History**: Implemented a system to maintain chat context within a session.
- **Booking Confirmation Flow**: Expanded the AI's ability to guide users through the booking confirmation process.

## Core Features

- Retrieval Augmented Generation (RAG) for accessing terms and conditions
- Tool use (Java methods) for performing actions
- LLM-based user interactions with improved context awareness

## AI Capabilities

- AI-powered chat interface with persistent context
- Enhanced flight booking management system
- Intelligent booking status updates and confirmations

## Tech Stack

- Backend: Spring Boot
- Frontend: [Vaadin Hilla](https://vaadin.com)
- AI Integration: [LangChain4j](https://github.com/langchain4j/langchain4j)

## Requirements

- Java 21+
- OpenAI API key (set in `OPENAI_API_KEY` environment variable)

## Running the Application

1. Clone this forked repository
2. Set the `OPENAI_API_KEY` environment variable
3. Run `Application.java` in your IDE or execute `mvn` in the command line

## Recent Updates

### Enhanced Booking Management

- Added tabbed interface for different booking statuses
- Implemented search functionality for bookings
- Displayed booking status counts for quick overview
- Improved AI tools for more detailed booking information retrieval

### LangChain4j Integration Improvements

- Extended `LangChain4jAssistant` to handle multi-turn conversations
- Updated `LangChain4jTools` with new booking management features
- Added tools for retrieving booking summaries and detailed information

## Next Steps

- Implement more sophisticated booking management workflows
- Enhance error handling and edge case scenarios in the AI interactions
- Develop a more robust memory management system for long-term context retention

## Contributing

Contributions to this enhanced fork are welcome! Please feel free to submit a Pull Request.

## Acknowledgements

This project is a fork of the original demo inspired by the [LangChain4j Customer Support Agent example](https://github.com/langchain4j/langchain4j-examples/tree/main/spring-boot-example/src/main/java/dev/langchain4j/example).

We extend our gratitude to the original creators and contributors:
- The LangChain4j team for the initial implementation
- [@tzolov](https://github.com/tzolov) from the Spring AI team
- [@sohamda](https://github.com/sohamda) from Microsoft for the Semantic Kernel implementation

While we have significantly enhanced and modified the original project, their foundational work was invaluable.

## License

[MIT License](LICENSE)
