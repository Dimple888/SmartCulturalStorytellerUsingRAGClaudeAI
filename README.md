# Spring AI Anime Narrator

## Overview
The Spring AI Anime Narrator is a Spring Boot REST API that utilizes Spring AI to retrieve cultural texts using Retrieval-Augmented Generation (RAG), generate story narrations, and provide scene-wise prompts for anime generation.

## Features
- **Cultural Text Retrieval**: Fetches cultural texts using RAG for enhanced storytelling.
- **Story Generation**: Generates engaging story narrations based on retrieved cultural texts.
- **Anime Prompt Generation**: Returns detailed scene-wise prompts to assist in anime creation.

## Project Structure
```
spring-ai-anime-narrator
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── animenarrrator
│   │   │               ├── AnimeNarratorApplication.java
│   │   │               ├── controller
│   │   │               │   └── NarratorController.java
│   │   │               ├── service
│   │   │               │   ├── RagService.java
│   │   │               │   ├── StoryGenerationService.java
│   │   │               │   └── AnimePromptService.java
│   │   │               ├── model
│   │   │               │   ├── StoryNarration.java
│   │   │               │   ├── ScenePrompt.java
│   │   │               │   └── AnimeRequest.java
│   │   │               ├── repository
│   │   │               │   └── CulturalTextRepository.java
│   │   │               └── config
│   │   │                   └── SpringAiConfig.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── cultural-texts
│   │           └── sample-texts.txt
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── animenarrrator
│                       └── AnimeNarratorApplicationTests.java
├── pom.xml
└── README.md
```

## Setup Instructions
1. **Clone the Repository**: 
   ```
   git clone <repository-url>
   cd spring-ai-anime-narrator
   ```

2. **Build the Project**: 
   ```
   mvn clean install
   ```

3. **Run the Application**: 
   ```
   mvn spring-boot:run
   ```

4. **Access the API**: 
   The API will be available at `http://localhost:8080`.

## Usage
- **Retrieve Cultural Text**: Send a GET request to `/api/cultural-text`.
- **Generate Story Narration**: Send a POST request to `/api/generate-story` with the necessary parameters.
- **Get Scene Prompts**: Send a POST request to `/api/generate-prompts` with the story ID and user preferences.

## Dependencies
- Spring Boot
- Spring AI
- Maven

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for details.