# Funnair AI-Powered Customer Support (Fork)

<img width="1723" alt="image" src="https://github.com/user-attachments/assets/d57cd1b8-6295-4a8e-aebb-90ce9b7c8578">

This app demonstrates an advanced AI-powered customer support system for Funnair, a fictional airline. It showcases intelligent AI integration for handling customer inquiries and managing bookings efficiently.

## Key Features

- **AI-Powered Chat Interface**: Intelligent conversational agent for customer support
- **Comprehensive Booking Management**: Tools for tracking, updating, and managing booking statuses
- **Client Management Dashboard**: Advanced tools for managing client profiles, preferences, and interactions
- **In-Memory Chat History**: Maintains chat context within a session for coherent interactions
- **Booking Confirmation Workflow**: Guides users through a streamlined booking confirmation process
- **Retrieval Augmented Generation (RAG)**: Accesses relevant information like terms and conditions
- **Integrated Tool Usage**: Utilizes Java methods to perform actions based on user requests
- **Dynamic Booking Status Updates**: Real-time updates of booking statuses with color-coded indicators
- **Searchable Booking Grid**: Easily find and manage bookings with a powerful search feature
- **Booking Status Counts**: Quick overview of bookings in different statuses
- **Client Segmentation**: Ability to segment clients based on various criteria for targeted promotions

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

- Implemented a comprehensive Client Management Dashboard for travel agents
- Added functionality to create, update, and delete client profiles
- Introduced client preference storage and automatic application during bookings
- Implemented booking history tracking and filtering capabilities
- Added client interaction logging for improved communication context
- Introduced client segmentation features for targeted marketing
- Enhanced data security with role-based access control for client information

## Roadmap

- Implement more complex booking management workflows
- Enhance error handling and edge case management in AI interactions
- Develop an advanced memory management system for extended context retention
- Introduce data visualization for booking trends and analytics
- Client Management: Enable the chatbot to manage client profiles, including preferences, frequent flyer information, and past bookings for personalized service
- Multi-Booking Management: Allow handling of multiple bookings simultaneously, including group bookings, with real-time status tracking
- Bulk Email/SMS Notifications: Implement a feature for sending bulk notifications to clients regarding booking confirmations, itinerary changes, or special offers

## Contributing

We welcome contributions! Please feel free to submit Pull Requests or open Issues for suggestions and bug reports.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## AI-Assisted Development

This application was developed with the assistance of AI technologies. Specifically:

- LangChain4j was used for integrating large language models and AI services.
- AI-powered tools were utilized during the development process to enhance productivity and code quality.
