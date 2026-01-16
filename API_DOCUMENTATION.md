# Smart Cultural Storyteller REST API Documentation

## Overview
A Spring Boot REST API that generates cultural stories with anime scene prompts using:
- **Spring Boot 3**: Modern framework
- **Spring AI**: LLM integration with Claude Haiku
- **RAG (Retrieval Augmented Generation)**: Vector store for cultural context
- **Clean Architecture**: Controller → Service → RAG layers

---

## Architecture

```
Client Request
    ↓
Controller (NarratorControllerNew.java)
    ↓
Service Layer:
├── StoryGenerationService (RAG + LLM)
├── StoryOrchestratorService (Scene breakdown)
├── AnimePromptService (Anime-specific prompts)
└── ClaudeAiService (LLM calls)
    ↓
Data Layer:
├── VectorStoreRagService (In-memory vector DB)
├── RagService (Cultural text management)
└── CulturalTextRepository (DB persistence)
    ↓
Response (JSON with scenes, emotions, prompts)
```

---

## API Endpoints

### 1. Health Check
**GET** `/api/storyteller/health`

```bash
curl http://localhost:8080/api/storyteller/health
```

**Response:**
```json
{
  "status": "OK",
  "service": "Smart Cultural Storyteller",
  "version": "1.0.0"
}
```

---

### 2. Main API - Generate Complete Story (⭐ PRIMARY)
**POST** `/api/storyteller/generate-story`

Generates a cultural story with scene-wise anime prompts.

**Request Body:**
```json
{
  "storyId": "sundarakanda_001",
  "storyName": "Sundarakanda – The Leap of Faith",
  "userQuery": "Tell Sundarakanda as a devotional anime-style story focusing on Hanuman's courage and devotion",
  "userPreferences": "Emotionally powerful, visually cinematic, culturally authentic",
  "style": "Devotional",
  "maxScenes": 8,
  "language": "en"
}
```

**Response:**
```json
{
  "title": "Sundarakanda – The Leap of Faith",
  "storyId": "sundarakanda_001",
  "culturalSource": "Ramayana - Sundarakanda",
  "emotion": "Veeram",
  "scenes": [
    {
      "sceneNumber": 1,
      "narration": "Hanuman stood upon Mount Mahendra, gazing at the vast ocean before him. The morning sun glinted off the waves as he prepared himself for an impossible task—to leap across the sea and find Sita. His heart, devoted entirely to Rama, burned with determination.",
      "emotion": "Veeram",
      "imagePrompt": "Hanuman standing at the edge of Mount Mahendra, anime cinematic style. Divine aura surrounds his muscular form. Vast ocean stretches beneath golden morning sky. Style: Devotional anime, detailed backgrounds, ethereal lighting.",
      "audioText": "Hanuman looked out at the endless ocean. Every fiber of his being resonated with purpose. He thought of Rama, his beloved master, and of Sita waiting across those treacherous waters. No distance was too far, no obstacle too great. With a deep breath, he prepared to make the leap that would change everything.",
      "characters": "Hanuman",
      "location": "Mount Mahendra"
    },
    {
      "sceneNumber": 2,
      "narration": "With a mighty roar that echoed across the heavens, Hanuman expanded to his true form, divine power flowing through every limb. His leap was not merely physical—it was a manifestation of pure devotion and spiritual power.",
      "emotion": "Veeram",
      "imagePrompt": "Hanuman transforming mid-air, glowing with divine energy. His form expands against sunset backdrop. Golden aura emanates from his body. Mountains and ocean far below. Style: Action-packed anime, dynamic movements, celestial effects.",
      "audioText": "The moment had come. Hanuman's body glowed with an otherworldly light. As he pushed off from the mountain, the very earth trembled. Higher and higher he soared, his divine form dwarfing the landscape below. This was no ordinary leap—this was the embodiment of devotion itself taking flight.",
      "characters": "Hanuman",
      "location": "The Vast Ocean"
    },
    {
      "sceneNumber": 3,
      "narration": "Demons rose from the ocean depths to challenge him, their forms terrifying. Yet Hanuman pressed onward, his resolve unshaken. Every obstacle became proof of his unwavering faith in Rama.",
      "emotion": "Karuna",
      "imagePrompt": "Demonic creatures emerging from turbulent ocean waves to face Hanuman in mid-flight. Dark contrasts with golden divine light. Waves crash with supernatural intensity. Style: Anime action sequence, dark mystical atmosphere.",
      "audioText": "The demons came, grotesque and terrible, their claws reaching toward him. But Hanuman felt no fear. Each challenge only strengthened his resolve. He was not fighting for himself—he fought for Rama, for love, for duty. That made him invincible.",
      "characters": "Hanuman, Demons",
      "location": "The celestial sky"
    },
    {
      "sceneNumber": 4,
      "narration": "At last, Lanka appeared on the horizon—a city of gold and magnificent architecture, the stronghold of the demon king. Hanuman descended toward the island, ready for whatever lay ahead.",
      "emotion": "Adbhuta",
      "imagePrompt": "Lanka appears from the mist and clouds, a magnificent golden city with ornate spires and temples. Hanuman descends toward it with determination. Sunset casts golden and purple hues. Style: Fantasy anime, grand architecture, ethereal atmosphere.",
      "audioText": "There! After his endless journey, Lanka finally appeared. The legendary city, seat of Ravana's power, rose before him in all its terrible beauty. Golden towers caught the setting sun. Hanuman paused for only a moment, gathering his strength, preparing for what would come next.",
      "characters": "Hanuman, Lanka",
      "location": "Lanka, the golden city"
    },
    {
      "sceneNumber": 5,
      "narration": "In the Ashoka garden, surrounded by blossoms and guarded by demons, Hanuman found her—Sita. Though imprisoned, her spirit remained unbroken, her devotion to Rama as pure as ever. Their eyes met, and hope kindled.",
      "emotion": "Karuna",
      "imagePrompt": "Sita sits in a beautiful garden, her face showing both sorrow and strength. Hanuman approaches from shadows. Blossoms fall around them. Moonlight filters through trees. Guards sleep in background. Style: Emotional anime, soft lighting, peaceful garden setting.",
      "audioText": "And there she was. Sita. Despite her captivity, despite her suffering, her face shone with an inner light. She had not lost faith. She had not forgotten Rama. Hanuman's eyes filled with tears as he beheld the wife of his beloved master, and he knew his journey had not been in vain.",
      "characters": "Hanuman, Sita, Ravana",
      "location": "Ashoka Vana garden"
    },
    {
      "sceneNumber": 6,
      "narration": "Hanuman revealed himself and placed Rama's signet ring in Sita's trembling hand. 'Your beloved has not forgotten you,' he said. 'Rama is coming. You need only wait and have faith.'",
      "emotion": "Shringar",
      "imagePrompt": "Hanuman reveals himself to Sita in the moonlit garden. The signet ring glows with significance. Sita's face transforms with hope and joy. Tender moment between divine devotee and imprisoned queen. Style: Romantic anime, soft lighting, emotional connection.",
      "audioText": "I am Hanuman, servant of the great Rama. He sends you his love and his promise. He has not forgotten you, not for a single moment. Rescue is coming. Hold onto faith, as you have held onto your love. Rama will come, and you will be reunited.",
      "characters": "Hanuman, Sita, Rama",
      "location": "Ashoka Vana garden"
    },
    {
      "sceneNumber": 7,
      "narration": "With Sita's heart now kindled with hope, Hanuman demonstrated his power. He destroyed gardens and palaces, his message clear: Rama's forces were formidable, and Ravana's time was limited.",
      "emotion": "Raudra",
      "imagePrompt": "Hanuman in his tremendous form, destroying Lankan palace gardens. Buildings crumble, demons flee. His power shakes the earth. Fire and dust fill the air. Dynamic action sequence. Style: Action anime, powerful effects, destructive energy.",
      "audioText": "Hanuman grew to his true size, his power overwhelming. The gardens of Lanka fell before him like flowers in a hurricane. Ravana's palace shook. The demon king realized that a force far greater than his own was coming. Rama's wrath would be devastating.",
      "characters": "Hanuman, Ravana, Demons",
      "location": "Lanka's palace grounds"
    },
    {
      "sceneNumber": 8,
      "narration": "Hanuman escaped the island and returned to Rama with news of Sita. His mission was complete. He had crossed the impossible ocean, found the lost princess, and brought hope to both Sita and Rama. His devotion had made the impossible real.",
      "emotion": "Hasya",
      "imagePrompt": "Hanuman returns to Rama's camp in triumphant celebration. Rama and his forces celebrate the good news. Dawn breaking over the landscape. Joy and anticipation fill the air. Style: Joyful anime, warm colors, triumphant atmosphere.",
      "audioText": "Hanuman returned to Rama bearing news that transformed despair into hope. The journey that had seemed impossible was complete. Sita lived. She awaited rescue. The stage was set for the final chapter of this epic tale—a story of love, devotion, and the triumph of righteousness over evil.",
      "characters": "Hanuman, Rama",
      "location": "Rama's camp"
    }
  ]
}
```

---

### 3. Generate Anime Scene Prompts
**POST** `/api/storyteller/generate-prompts`

Generates detailed scene prompts from a story narrative.

**Request Body:**
```json
{
  "title": "Sundarakanda - The Leap",
  "content": "Hanuman stood on the mountain...",
  "author": "Claude Haiku"
}
```

**Response:**
```json
[
  {
    "sceneNumber": 1,
    "narration": "Hanuman stood upon Mount Mahendra...",
    "emotion": "Veeram",
    "imagePrompt": "Hanuman standing at Mount Mahendra...",
    "audioText": "Hanuman looked out at the endless ocean...",
    "characters": "Hanuman",
    "location": "Mount Mahendra",
    "style": null
  },
  // ... more scenes
]
```

---

### 4. RAG Search
**GET** `/api/storyteller/rag-search?query=Hanuman+devotion`

Retrieves relevant story chunks from the vector store.

**Response:**
```json
{
  "query": "Hanuman devotion",
  "context": "CULTURAL STORY CONTEXT:\n\nSection 1:\nVerse: Hanuman stood upon Mount Mahendra...",
  "chunksRetrieved": 3
}
```

---

### 5. Get Vector Store Statistics
**GET** `/api/storyteller/vector-store/stats`

**Response:**
```json
{
  "totalChunks": 8,
  "emotions": [
    "Veeram",
    "Shringar",
    "Karuna",
    "Bhaya",
    "Hasya",
    "Adbhuta",
    "Raudra",
    "Bibhatsa"
  ],
  "source": "Sundarakanda (Ramayana)"
}
```

---

### 6. Get Available Emotions (Rasas)
**GET** `/api/storyteller/emotions`

**Response:**
```json
{
  "Veeram": "Heroism - Courage and valor",
  "Shringar": "Love - Romance and affection",
  "Karuna": "Compassion - Empathy and sorrow",
  "Bhaya": "Fear - Dread and terror",
  "Hasya": "Joy - Laughter and delight",
  "Adbhuta": "Wonder - Amazement and awe",
  "Raudra": "Anger - Rage and fury",
  "Bibhatsa": "Disgust - Revulsion"
}
```

---

### 7. Get Cultural Text
**GET** `/api/storyteller/cultural-text`

**Response:**
```json
{
  "content": "Sundarakanda - The Book of the Beautiful\n\nThe Sundarakanda is the fifth book...",
  "availableChunks": 8,
  "source": "Ramayana - Sundarakanda"
}
```

---

### 8. API Information
**GET** `/api/storyteller/info`

**Response:**
```json
{
  "name": "Smart Cultural Storyteller",
  "version": "1.0.0",
  "description": "REST API for generating cultural stories with anime scene prompts",
  "features": [
    "RAG-based story generation",
    "Claude Haiku LLM integration",
    "Scene-wise anime prompt generation",
    "Emotional tone (Rasa) mapping",
    "Multi-language support ready"
  ]
}
```

---

## Setup Instructions

### 1. Environment Variables
```powershell
$env:ANTHROPIC_API_KEY="your-anthropic-api-key"
```

### 2. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

### 3. Access Swagger UI (if added)
```
http://localhost:8080/swagger-ui.html
```

---

## Model Classes

### StoryRequest (AnimeRequest)
```json
{
  "storyId": "unique_identifier",
  "storyName": "Story Title",
  "userQuery": "User request description",
  "userPreferences": "Preferences",
  "style": "anime_style",
  "maxScenes": 10,
  "language": "en"
}
```

### StoryResponse
```json
{
  "title": "Story Title",
  "storyId": "unique_id",
  "culturalSource": "Source",
  "emotion": "Primary emotion",
  "scenes": [
    {
      "sceneNumber": 1,
      "narration": "Scene text",
      "emotion": "Veeram",
      "imagePrompt": "Image generation prompt",
      "audioText": "Narration script",
      "characters": "Characters",
      "location": "Location"
    }
  ]
}
```

---

## Key Features

✅ **RAG-Based Retrieval**: Uses vector store with 8+ cultural chunks  
✅ **Claude Haiku Integration**: Fast, cost-effective LLM calls  
✅ **Scene Orchestration**: Breaks stories into 5-8 distinct visual scenes  
✅ **Emotion Mapping**: 8 Indian classical emotions (Rasas)  
✅ **Anime-Specific Prompts**: Image and audio prompt generation  
✅ **Clean Architecture**: Separation of concerns, easy to extend  
✅ **POC-Friendly**: In-memory vector store, locally runnable  
✅ **Extensible**: Ready for multi-language, TTS, and image generation

---

## Extension Points

### 1. Text-to-Speech Integration
```java
// In AnimePromptService
public String generateAudioNarration(String text) {
    // Integrate with Google Cloud TTS or AWS Polly
}
```

### 2. Image Generation
```java
// Call Stable Diffusion or DALL-E with scene prompts
```

### 3. Vector Database
```java
// Replace in-memory store with Chroma or Pinecone
```

### 4. Multi-Language Support
```java
// Add language parameter and translation service
```

---

## Troubleshooting

**Issue**: "ANTHROPIC_API_KEY not found"  
**Solution**: Set environment variable before running app

**Issue**: "Vector store has 0 chunks"  
**Solution**: Check that VectorStoreRagService.initializeVectorStore() is called on startup

**Issue**: "Claude Haiku timeout"  
**Solution**: Increase timeout in application.properties

---

## Performance Notes

- **Response Time**: 5-10 seconds (depends on LLM latency)
- **Vector Store**: 8 chunks in-memory (expandable)
- **Memory**: ~100MB (excluding JVM)
- **Concurrent Requests**: Limited by Anthropic API rate limits

---

## Contact & Support

For issues or questions, refer to the source code comments and README.md
