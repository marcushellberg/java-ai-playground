# Funnair AI-Powered Customer Support (Fork)

<img width="1723" alt="image" src="https://github.com/user-attachments/assets/d57cd1b8-6295-4a8e-aebb-90ce9b7c8578">

This app demonstrates an advanced AI-powered customer support system for Funnair, a fictional airline. It showcases intelligent AI integration for handling customer inquiries and managing bookings efficiently.

## Key Features

- **AI-Powered Chat Interface**: Intelligent conversational agent for customer support
- **Comprehensive Booking Management**: Tools for tracking, updating, and managing booking statuses
- **In-Memory Chat History**: Maintains chat context within a session for coherent interactions
- **Booking Confirmation Workflow**: Guides users through a streamlined booking confirmation process
- **Retrieval Augmented Generation (RAG)**: Accesses relevant information like terms and conditions
- **Integrated Tool Usage**: Utilizes Java methods to perform actions based on user requests
- **Dynamic Booking Status Updates**: Real-time updates of booking statuses with color-coded indicators
- **Searchable Booking Grid**: Easily find and manage bookings with a powerful search feature
- **Booking Status Counts**: Quick overview of bookings in different statuses

## Tech Stack

- Backend: Spring Boot
- Frontend: Vaadin Hilla
- AI Integration: LangChain4j

## Requirements

- Java 21+
- OpenAI API key

## Setup and Running

1. Clone the repository
2. Set the `OPENAI_API_KEY` environment variable
3. Run the application:
   - IDE: Execute `Application.java`
   - Command line: Run `mvn`
4. Access the app at `http://localhost:8080`

## Usage Guide

1. **Chat Interface**: Interact with the AI assistant for flight information, bookings, or general inquiries.
2. **Booking Management**: 
   - Use the tabbed interface to view different booking statuses (All, Confirmed, Pending, Cancelled)
   - Search for specific bookings using the search bar
   - Check booking status counts at the top of the grid for a quick overview
3. **AI-Assisted Actions**: The AI can help with tasks like retrieving booking details, updating statuses, and confirming reservations.

## Recent Enhancements

- Implemented a tabbed interface for viewing bookings by status
- Added a search functionality to quickly find specific bookings
- Introduced color-coded status indicators for easy visual identification
- Displayed booking status counts for at-a-glance information
- Enhanced AI tools for detailed booking information retrieval and management
- Extended `LangChain4jAssistant` for more sophisticated multi-turn conversations
- Updated `LangChain4jTools` with advanced booking management capabilities

## Roadmap

- Implement more complex booking management workflows
- Enhance error handling and edge case management in AI interactions
- Develop an advanced memory management system for extended context retention
- Introduce data visualization for booking trends and analytics

## Contributing

We welcome contributions! Please feel free to submit Pull Requests or open Issues for suggestions and bug reports.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## AI-Assisted Development

This application was developed with the assistance of AI technologies. Specifically:

- LangChain4j was used for integrating large language models and AI services.
- AI-powered tools were utilized during the development process to enhance productivity and code quality.
