# Smart Cultural Storyteller - Quick Reference

## 🎯 Main API Call

```bash
curl -X POST http://localhost:8080/api/storyteller/generate-story \
  -H "Content-Type: application/json" \
  -d '{
    "storyId": "sk_001",
    "storyName": "Sundarakanda",
    "userQuery": "Tell Hanuman story as devotional anime with powerful visuals",
    "userPreferences": "Emotionally engaging, culturally authentic",
    "style": "Devotional",
    "maxScenes": 8,
    "language": "en"
  }'
```

## 📁 Project Structure at a Glance

```
src/main/java/com/example/animenarrrator/
├── controller/
│   ├── NarratorController.java (legacy)
│   └── NarratorControllerNew.java ⭐ (USE THIS)
├── service/
│   ├── StoryGenerationService.java ⭐ (RAG + LLM)
│   ├── StoryOrchestratorService.java ⭐ (Scene breakdown)
│   ├── AnimePromptService.java (Anime generation)
│   ├── ClaudeAiService.java (LLM wrapper)
│   ├── PromptTemplateService.java (Prompts)
│   ├── VectorStoreRagService.java ⭐ (Vector DB)
│   └── RagService.java
├── model/
│   ├── StoryResponse.java ⭐
│   ├── Scene.java ⭐
│   ├── StoryChunk.java
│   ├── AnimeRequest.java
│   └── ScenePrompt.java
├── repository/
│   └── CulturalTextRepository.java
└── config/
    └── SpringAiConfig.java

src/main/resources/
├── application.properties ⭐
├── cultural-texts/
│   ├── sundarakanda.txt ⭐ (Story content)
│   └── story-dataset.json ⭐ (Metadata)
```

## 🔑 Core Classes Explained

| Class | Purpose | Key Methods |
|-------|---------|-------------|
| **NarratorControllerNew** | Main API endpoint | `generateStory()`, `generatePrompts()` |
| **StoryGenerationService** | RAG + LLM pipeline | `generateStoryNarration()` |
| **StoryOrchestratorService** | Scene breakdown | `orchestrateStory()` |
| **VectorStoreRagService** | Vector store | `retrieveRelevantChunks()` |
| **ClaudeAiService** | LLM wrapper | `generateStory()`, `callLlm()` |
| **PromptTemplateService** | Prompt management | `getStoryGenerationPrompt()` |

## 📊 Data Flow

```
User Request
     ↓
NarratorControllerNew.generateStory()
     ↓
StoryGenerationService.generateStoryNarration()
     ├→ VectorStoreRagService.retrieveRelevantChunks()
     ├→ ClaudeAiService.generateStory()
     └→ Return StoryNarration
     ↓
StoryOrchestratorService.orchestrateStory()
     ├→ Extract scenes
     ├→ Enrich with LLM
     └→ Return StoryResponse
     ↓
Return JSON with 8 scenes
```

## 🎬 Sample Response Structure

```json
{
  "title": "Sundarakanda",
  "storyId": "sk_001",
  "emotion": "Veeram",
  "scenes": [
    {
      "sceneNumber": 1,
      "narration": "...",
      "emotion": "Veeram",
      "imagePrompt": "...",
      "audioText": "...",
      "characters": "Hanuman",
      "location": "Mount Mahendra"
    }
    // 7 more scenes...
  ]
}
```

## 🚀 Setup in 5 Steps

1. **Set API Key**
```powershell
$env:ANTHROPIC_API_KEY="sk-ant-xxx"
```

2. **Build**
```bash
mvn clean install
```

3. **Run**
```bash
mvn spring-boot:run
```

4. **Verify Health**
```bash
curl http://localhost:8080/api/storyteller/health
```

5. **Call Main API**
```bash
curl -X POST http://localhost:8080/api/storyteller/generate-story \
  -H "Content-Type: application/json" \
  -d '{"storyName":"Sundarakanda","userQuery":"Hanuman story"}'
```

## 🎨 The 8 Emotions (Rasas)

| Rasa | Meaning | Color | Characteristics |
|------|---------|-------|-----------------|
| Veeram | Heroism | 🔴 Red | Courage, valor, strength |
| Shringar | Love | 💗 Pink | Romance, affection, tenderness |
| Karuna | Compassion | 💙 Blue | Empathy, sorrow, sadness |
| Bhaya | Fear | 🩸 Dark Red | Dread, terror, anxiety |
| Hasya | Joy | ✨ Gold | Laughter, happiness, delight |
| Adbhuta | Wonder | 🦋 Cyan | Amazement, fascination, awe |
| Raudra | Anger | 🔥 Crimson | Rage, fury, indignation |
| Bibhatsa | Disgust | 🤢 Olive | Revulsion, contempt, disgust |

## 🧠 Vector Store Chunks (8)

| # | Scene | Emotion | Characters |
|---|-------|---------|------------|
| 1 | Preparation on Mount | Veeram | Hanuman |
| 2 | The great leap | Veeram | Hanuman |
| 3 | Demon encounters | Karuna | Hanuman, Demons |
| 4 | Arrival at Lanka | Adbhuta | Hanuman |
| 5 | Discovery of Sita | Karuna | Hanuman, Sita |
| 6 | Offer of Rama's ring | Shringar | Hanuman, Sita |
| 7 | Destruction of Lanka | Raudra | Hanuman, Ravana |
| 8 | Return with news | Hasya | Hanuman, Rama |

## 📡 All API Endpoints

```
GET  /api/storyteller/health           - Check service
POST /api/storyteller/generate-story   - ⭐ Main API
POST /api/storyteller/generate-prompts - Scene prompts
GET  /api/storyteller/cultural-text    - RAG content
GET  /api/storyteller/rag-search       - Search vector store
GET  /api/storyteller/vector-store/stats - DB stats
GET  /api/storyteller/emotions         - Get all rasas
GET  /api/storyteller/info             - API info
```

## ⚙️ Configuration

### application.properties Key Settings
```properties
# API
server.port=8080

# Claude Haiku
spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}
spring.ai.anthropic.chat.options.model=claude-3-5-haiku-20241022
spring.ai.anthropic.chat.options.temperature=0.7

# Database
spring.datasource.url=jdbc:h2:mem:sundarakanda_db
spring.jpa.hibernate.ddl-auto=create-drop
```

## 🔧 Extending the API

### Add New Story
```java
// In VectorStoreRagService.initializeVectorStore()
addChunk("id", "verse", "meaning", "emotion", "characters", "intent", "location");
```

### Add New Endpoint
```java
// In NarratorControllerNew
@GetMapping("/new-endpoint")
public ResponseEntity<?> newEndpoint() {
    // Implementation
}
```

### Customize Prompt
```java
// In PromptTemplateService
public String getCustomPrompt(String param) {
    return String.format("Your template here: %s", param);
}
```

## 🐛 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| `API_KEY not found` | `$env:ANTHROPIC_API_KEY="key"` |
| `Port 8080 in use` | Change `server.port` in properties |
| `Vector store empty` | Check VectorStoreRagService init |
| `LLM timeout` | Increase `spring.ai.anthropic.chat.options.timeout` |
| `Build fails` | Run `mvn clean install -DskipTests` |

## 📚 Learn More

- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - Full API reference
- **[README_NEW.md](README_NEW.md)** - Project overview
- **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** - Architecture details
- **Javadoc** - Inline comments in all classes

## 🎯 Feature Matrix

| Feature | Status | Used For |
|---------|--------|----------|
| RAG Retrieval | ✅ | Cultural context |
| Story Generation | ✅ | Complete narratives |
| Scene Breakdown | ✅ | Individual scenes |
| Emotion Mapping | ✅ | Emotional tones |
| Image Prompts | ✅ | Anime generation |
| Audio Scripts | ✅ | TTS integration |
| Multi-language | 🔧 | Future: translation |
| TTS Integration | 🔧 | Future: audio synthesis |
| Image Generation | 🔧 | Future: DALL-E/Stable Diffusion |
| Video Generation | 🔧 | Future: scene animation |

## 💡 Key Insights

1. **RAG First** - Cultural context comes from vector store, not LLM training
2. **Scene-Based** - Stories broken into multimedia-ready scenes
3. **Emotion-Driven** - Each scene gets classical Indian emotion
4. **Anime-Ready** - All prompts designed for image/audio generation APIs
5. **Extensible** - Easy to add stories, emotions, or new features

## 🎓 Technologies Used

- **Spring Boot 3** - Framework
- **Spring AI** - LLM integration
- **Claude Haiku** - Language model
- **H2 Database** - In-memory data storage
- **Java 17** - Language

## 📈 Performance Targets

- Response time: 5-10 seconds
- Vector store: 8+ chunks
- Memory: ~100MB
- Concurrent requests: Limited by API rate limits

## ✨ Production Checklist

- ✅ Error handling implemented
- ✅ Logging configured
- ✅ Clean architecture
- ✅ API documentation complete
- ✅ Vector store initialized
- ✅ Configuration management
- ✅ Ready for deployment

---

**For complete details, see the full documentation files!**
