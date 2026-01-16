# Smart Cultural Storyteller - Complete Production Guide

## 🎬 Project Overview

Smart Cultural Storyteller is a sophisticated AI-powered platform that generates culturally-rich anime narratives using a combination of:

- **Retrieval-Augmented Generation (RAG)** for contextual accuracy
- **Claude Haiku 3 AI** for creative story generation
- **AWS Polly** for multi-emotional text-to-speech narration
- **Spring Boot 3** REST API backend
- **Modern Web UI** with 6 production-ready tabs

### Key Capabilities
✅ Generate full anime stories from cultural texts (Sundarakanda)
✅ Search knowledge base with semantic similarity
✅ Synthesize emotional speech in 8 Indian Rasas
✅ Create anime scenes with synchronized audio
✅ Filter content by emotion and character
✅ Manage and monitor RAG vector store

---

## 📁 Project Structure

```
spring-ai-anime-narrator/
├── src/main/
│   ├── java/com/example/animenarrrator/
│   │   ├── AnimeNarratorApplication.java          # Entry point
│   │   ├── config/
│   │   │   └── SpringAiConfig.java                # Spring AI configuration
│   │   ├── controller/
│   │   │   └── NarratorController.java            # REST API (10+ endpoints)
│   │   ├── model/
│   │   │   ├── AnimeRequest.java
│   │   │   ├── StoryResponse.java
│   │   │   ├── StoryChunk.java
│   │   │   └── ...other models
│   │   ├── service/
│   │   │   ├── ClaudeAiService.java               # LLM integration
│   │   │   ├── RagService.java                    # RAG orchestration
│   │   │   ├── VectorStoreRagService.java         # Vector store management
│   │   │   ├── TTSService.java                    # Text-to-speech
│   │   │   ├── StoryGenerationService.java
│   │   │   ├── StoryOrchestratorService.java
│   │   │   ├── AnimePromptService.java
│   │   │   └── PromptTemplateService.java
│   │   ├── repository/
│   │   │   └── CulturalTextRepository.java        # H2 database
│   │   └── AnimeNarratorApplication.java
│   └── resources/
│       ├── static/
│       │   ├── index.html                         # Main UI dashboard
│       │   ├── styles.css                         # Production CSS
│       │   ├── app.js                             # Client-side logic
│       │   └── demo.html                          # Feature showcase
│       ├── application.properties                 # Configuration
│       └── cultural-texts/
│           ├── sundarakanda.txt                   # Cultural knowledge base
│           └── sample-texts.txt
├── pom.xml                                         # Maven dependencies
├── UI_DOCUMENTATION.md                             # UI feature guide
├── QUICK_START_UI.md                              # 5-minute quickstart
├── ARCHITECTURE.md                                # System design
├── COMPLETE_IMPLEMENTATION.md                     # Technical details
├── API_DOCUMENTATION.md                           # Endpoint reference
└── README.md                                      # This file
```

---

## 🚀 Quick Start (5 Minutes)

### 1. Prerequisites
```bash
Java 17+
Maven 3.6+
Spring Boot 3.2
```

### 2. Clone & Build
```bash
git clone <repository>
cd spring-ai-anime-narrator
mvn clean install
```

### 3. Start Application
```bash
mvn spring-boot:run
```

Expected output:
```
Tomcat started on port(s): 8080 with context path ''
```

### 4. Open Web UI
Visit: **http://localhost:8080/**

### 5. Verify Connection
Click "Check Status" button - should show "● Connected"

---

## 📊 Feature Breakdown

### Tab 1: 📖 Story Generation
**Generate complete stories from Sundarakanda**

```bash
# Example request
curl -X POST http://localhost:8080/api/narrator/generate-story \
  -H "Content-Type: application/json" \
  -d '{
    "storyName": "Hanuman Journey",
    "userQuery": "Tell me Hanuman journey to Lanka",
    "style": "anime",
    "maxScenes": 5
  }'
```

### Tab 2: 🔍 RAG Search
**Semantic search across vector store**

```bash
curl "http://localhost:8080/api/narrator/search-rag?query=Hanuman%20ocean%20leap"
```

### Tab 3: 🎙️ TTS Synthesis
**Emotionally-aware text-to-speech**

```bash
curl -X POST http://localhost:8080/api/narrator/tts/synthesize \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Hanuman stood upon the mountain...",
    "emotion": "Veeram",
    "language": "EN"
  }'
```

**Supported Emotions (Rasas):**
- Veeram (Heroism) → Joanna (EN), Aditi (HI)
- Shringar (Love) → Emma (EN), Raveena (HI)
- Karuna (Compassion) → Amy (EN), Aditi (HI)
- Bhaya (Fear) → Brian (EN), Kalpana (HI)
- Hasya (Joy) → Ivy (EN), Raveena (HI)
- Adbhuta (Wonder) → Salli (EN), Aditi (HI)
- Raudra (Anger) → Matthew (EN), Kalpana (HI)
- Bibhatsa (Disgust) → Russell (EN), Kalpana (HI)

### Tab 4: 🎨 Anime Scene
**Scene generation with synchronized audio**

```bash
curl -X POST http://localhost:8080/api/narrator/anime-scene-with-tts \
  -H "Content-Type: application/json" \
  -d '{
    "narration": "Hanuman leaps across...",
    "sceneDescription": "Heroic pose on mountain, sunset, glowing aura",
    "emotion": "Veeram"
  }'
```

### Tab 5: 🎭 Emotions & Characters
**Filter by emotional tone or character**

```bash
# By emotion
curl "http://localhost:8080/api/narrator/emotion/Veeram"

# By character
curl "http://localhost:8080/api/narrator/character/Hanuman"
```

### Tab 6: 📊 Vector Store
**Knowledge base management**

```bash
# Load Sundarakanda
curl -X POST http://localhost:8080/api/narrator/load-sundarakanda

# Get statistics
curl "http://localhost:8080/api/narrator/vector-store-stats"
```

---

## 🔧 Configuration

### Environment Variables

**For Development (Demo Mode - Default)**
```bash
# No API key needed, uses fallback responses
export app.demo-mode=true
```

**For Production (Real Claude API)**
```bash
export ANTHROPIC_API_KEY=sk-ant-your-actual-key-here
export app.demo-mode=false
```

**For AWS Polly (Production TTS)**
```bash
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
export AWS_REGION=us-east-1
```

### Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Server
server.port=8080
spring.application.name=spring-ai-smart-cultural-storyteller

# Claude AI
spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY:demo}
spring.ai.anthropic.chat.options.model=claude-3-haiku-20240307
spring.ai.anthropic.chat.options.temperature=0.7
spring.ai.anthropic.chat.options.max-tokens=2048

# Demo Mode (set to false for production)
app.demo-mode=true

# Database
spring.datasource.url=jdbc:h2:mem:sundarakanda_db
spring.jpa.hibernate.ddl-auto=create-drop

# Logging
logging.level.com.example.animenarrrator=DEBUG

# Static Files
spring.web.resources.static-locations=classpath:/static/
```

---

## 🏗️ Architecture Overview

### System Flow

```
┌─────────────────────┐
│   Web UI (HTML/CSS) │
│   6 Interactive     │
│   Tabs              │
└──────────┬──────────┘
           │ JSON Requests
           ↓
┌──────────────────────────┐
│  REST API Controller     │
│  (NarratorController)    │
│  10+ Endpoints           │
└──────┬──────────────────┘
       │
       ├─→ StoryGenerationService → ClaudeAiService → Claude Haiku 3
       │
       ├─→ RagService → VectorStoreRagService → In-Memory Vector Store
       │
       ├─→ TTSService → AWS Polly (configured) or Mock Response
       │
       └─→ AnimePromptService → Scene Generation

┌────────────────────────┐
│   H2 In-Memory DB      │
│  (Cultural Texts)      │
└────────────────────────┘
```

### Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 3.2.0 |
| AI/LLM | Spring AI + Claude | 1.0-M1 + Haiku |
| Database | H2 | In-memory |
| Frontend | HTML5 + CSS3 + JS | Vanilla (no deps) |
| TTS | AWS Polly | Ready for integration |
| Build | Maven | 3.6+ |
| Runtime | Java | 17+ |

---

## 📚 Documentation Files

| Document | Purpose | Audience |
|----------|---------|----------|
| **QUICK_START_UI.md** | 5-minute setup and basic usage | All Users |
| **UI_DOCUMENTATION.md** | Detailed feature guide and API reference | UI Users |
| **API_DOCUMENTATION.md** | Complete endpoint reference | Developers |
| **ARCHITECTURE.md** | System design and component overview | Architects |
| **COMPLETE_IMPLEMENTATION.md** | Technical implementation details | Developers |
| **README.md** | This comprehensive guide | All |

---

## 🧪 Testing & Verification

### Health Check
```bash
curl http://localhost:8080/api/narrator/health
# Response: {"status":"OK","service":"Smart Cultural Storyteller","version":"1.0.0"}
```

### Load Sample Data
```bash
curl -X POST http://localhost:8080/api/narrator/load-sundarakanda
# Response: {"chunksAdded":8}
```

### Search RAG
```bash
curl "http://localhost:8080/api/narrator/search-rag?query=Hanuman"
# Response: Array of relevant chunks with metadata
```

### Synthesize TTS
```bash
curl -X POST http://localhost:8080/api/narrator/tts/synthesize \
  -H "Content-Type: application/json" \
  -d '{"text":"Test speech","emotion":"Veeram","language":"EN"}'
# Response: Audio URL or mock response
```

### Get TTS Voices
```bash
curl http://localhost:8080/api/narrator/tts/voices
# Response: All 8 Rasas with voice mappings
```

---

## 🚢 Production Deployment

### Build Release
```bash
mvn clean package -DskipTests
```

Output: `target/spring-ai-anime-narrator-0.0.1-SNAPSHOT.jar`

### Run with Production Settings
```bash
java -Dapp.demo-mode=false \
     -DANTHROPICKEY=your-actual-key \
     -Dspring.profiles.active=prod \
     -jar target/spring-ai-anime-narrator-0.0.1-SNAPSHOT.jar
```

### Docker Deployment
```dockerfile
FROM openjdk:17-slim
COPY target/spring-ai-anime-narrator-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV ANTHROPIC_API_KEY=${ANTHROPIC_API_KEY}
ENV app.demo-mode=false
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t cultural-storyteller .
docker run -e ANTHROPIC_API_KEY=your-key -p 8080:8080 cultural-storyteller
```

### Cloud Deployment Options
- **AWS EC2/ECS** - Spring Boot native image
- **Google Cloud Run** - Containerized deployment
- **Azure App Service** - Direct JAR deployment
- **Heroku** - Simple push deployment

---

## 🔐 Security Considerations

### API Key Management
✅ Use environment variables (never commit keys)
✅ Rotate keys regularly
✅ Use IAM roles in cloud deployments

### CORS Configuration
```java
@CrossOrigin(origins = "https://yourdomain.com", maxAge = 3600)
```

### Input Validation
✅ All user inputs validated server-side
✅ SQL injection prevention via parameterized queries
✅ XSS protection via proper JSON encoding

### Logging
✅ PII data not logged
✅ API keys masked in logs
✅ Audit trails for critical operations

---

## 📈 Performance Optimization

### Frontend
- CSS animations use GPU acceleration
- Lazy loading for tab content
- Minimal JavaScript (no frameworks)
- Optimized for mobile devices

### Backend
- H2 in-memory database for speed
- Vector store caching
- Connection pooling configured
- Response compression enabled

### Caching Strategy
```
Cache-Control: public, max-age=3600
ETag generation for static assets
Browser caching for UI components
```

---

## 🐛 Troubleshooting

### Backend Issues

**"Port 8080 already in use"**
```bash
# Find process using port 8080
lsof -i :8080
# Kill it
kill -9 <PID>
```

**"ANTHROPIC_API_KEY not found"**
```bash
# Set environment variable
export ANTHROPIC_API_KEY=your-key
# Or use demo mode
export app.demo-mode=true
```

**"Vector store is empty"**
1. Go to 📊 Vector Store tab
2. Click "Load Sundarakanda"
3. Wait for "Chunks loaded: 8"

### Frontend Issues

**"Cannot connect to backend"**
1. Verify Spring Boot is running
2. Check port 8080: `curl localhost:8080/api/narrator/health`
3. Clear browser cache: Ctrl+Shift+Del

**"Audio not playing"**
1. Check browser volume settings
2. Enable audio permissions
3. Try different browser

---

## 📞 Support & Contribution

### Getting Help
1. Check relevant documentation file
2. Review troubleshooting section
3. Check application logs: `tail -f app.log`
4. Enable debug logging: `logging.level.root=DEBUG`

### Contributing
1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

### Reporting Issues
Include:
- Error message and stack trace
- Steps to reproduce
- Environment details (Java version, OS, etc.)
- Configuration settings (sanitized)

---

## 🎯 Roadmap

### Phase 1 (Current) ✅
- [x] Web UI with 6 tabs
- [x] RAG integration
- [x] Claude AI story generation
- [x] TTS with Rasa mapping
- [x] Vector store management
- [x] Production-ready documentation

### Phase 2 (Next)
- [ ] User authentication (JWT)
- [ ] Story persistence (PostgreSQL)
- [ ] Advanced filtering options
- [ ] Real-time streaming responses
- [ ] Multi-user support

### Phase 3 (Future)
- [ ] Mobile app (React Native)
- [ ] Video generation integration
- [ ] Advanced analytics dashboard
- [ ] ML model fine-tuning
- [ ] Multi-language UI
- [ ] WebSocket live updates

---

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

---

## 🙏 Acknowledgments

- **Spring Framework Team** - Excellent framework
- **Anthropic** - Claude AI API
- **AWS** - Polly and cloud services
- **Indian Classical Arts** - Rasa and aesthetic philosophy

---

## 📧 Contact

For questions, suggestions, or partnerships:
- Email: support@culturalstoryteller.dev
- Issues: GitHub Issues
- Discussions: GitHub Discussions

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | Jan 2026 | Initial release with 6 tabs, RAG, Claude AI, TTS |
| 0.5.0 | Dec 2025 | Beta release with core features |
| 0.1.0 | Nov 2025 | Alpha prototype |

---

## 🎬 Final Notes

The Smart Cultural Storyteller represents a unique intersection of:
- **Modern AI Technology** - Claude Haiku for creative generation
- **Cultural Heritage** - Sundarakanda and Indian Rasas
- **User Experience** - Intuitive web interface
- **Production Quality** - Comprehensive documentation and error handling

This system is **ready for production deployment** and can be extended with additional cultural texts, languages, and AI models.

**Happy storytelling! 🎭📚🎬**
