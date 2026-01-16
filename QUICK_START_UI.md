# Quick Start Guide - UI Integration

## 🚀 Getting Started in 5 Minutes

### Step 1: Start the Application
```bash
cd spring-ai-anime-narrator
mvn clean spring-boot:run
```

Wait for the message:
```
Tomcat started on port(s): 8080
```

### Step 2: Open the Web UI
Visit: **http://localhost:8080/**

You should see the Smart Cultural Storyteller dashboard with 6 tabs.

### Step 3: Verify Backend Connection
Click the **"Check Status"** button in the header. You should see:
```
● Connected
Backend is running and responding normally
```

### Step 4: Load Sample Data
1. Go to the **📊 Vector Store** tab
2. Click **"Load Sundarakanda into Vector Store"**
3. Confirm you see: "Chunks loaded: 8"

### Step 5: Try Each Feature

#### Feature 1: Generate a Story (2 min)
1. Go to **📖 Story Generation**
2. The default query is already filled
3. Click **"Generate Story"**
4. Wait for Claude AI response (or demo fallback)

#### Feature 2: Search the Vector Store (1 min)
1. Go to **🔍 RAG Search**
2. Keep default query: "Hanuman's leap across the ocean"
3. Click **"Search Vector Store"**
4. View relevant story chunks

#### Feature 3: Text-to-Speech (2 min)
1. Go to **🎙️ TTS/Audio**
2. Keep default text about Hanuman
3. Select emotion: **"Veeram - Heroism"**
4. Click **"Synthesize Audio"**
5. Play the generated audio (demo mode provides mock response)

#### Feature 4: Anime Scene with Audio (2 min)
1. Go to **🎨 Anime Scene**
2. Keep default narration and description
3. Click **"Generate Scene + Audio"**
4. View scene details

#### Feature 5: Filter by Emotion (1 min)
1. Go to **🎭 Emotions & Characters**
2. Select emotion: **"Veeram"**
3. Click **"Get Scenes"**
4. View all heroic scenes

#### Feature 6: Filter by Character (1 min)
1. In same tab, enter: **"Hanuman"**
2. Click **"Get Scenes"**
3. View all Hanuman scenes

## 📋 Tab Overview

| Tab | Purpose | Key Action |
|-----|---------|-----------|
| 📖 Story Gen | Create full narratives | Generate Story |
| 🔍 RAG Search | Find relevant content | Search Vector Store |
| 🎙️ TTS/Audio | Synthesize speech | Synthesize Audio |
| 🎨 Anime Scene | Create visual scenes | Generate Scene + Audio |
| 🎭 Emotions/Chars | Filter by metadata | Filter by Emotion/Character |
| 📊 Vector Store | Manage knowledge base | Load Sundarakanda |

## 🔧 Configuration

### Environment Variables (Optional)
For production with real Claude API:
```bash
export ANTHROPIC_API_KEY=sk-ant-xxx...
```

### Demo Mode (Default)
Currently enabled in `application.properties`:
```properties
app.demo-mode=true
```
This provides mock responses for testing without API calls.

### Disable Demo Mode
To use real Claude AI:
```properties
app.demo-mode=false
```
Requires valid `ANTHROPIC_API_KEY` environment variable.

## 🐛 Troubleshooting

### Issue: "Cannot connect to backend"
**Solution:**
1. Verify Spring Boot is running: Check terminal shows "Tomcat started on port 8080"
2. Check port 8080 is not blocked by firewall
3. Try health check: `curl http://localhost:8080/api/narrator/health`

### Issue: Story generation takes too long
**Solution:**
- Demo mode is enabled (instant fallback)
- If demo mode off, Claude API may be slow
- Check `app.demo-mode=true` in application.properties

### Issue: Audio not playing
**Solution:**
- In demo mode, audio is a mock file (expected)
- Browser audio permissions are enabled
- For production AWS Polly: Configure AWS credentials

### Issue: "Search returned 0 results"
**Solution:**
1. First load Sundarakanda: Go to 📊 Vector Store tab
2. Click "Load Sundarakanda into Vector Store"
3. Wait for "Chunks loaded: 8" message
4. Try search again

## 📝 Example Workflows

### Workflow 1: Create a Story Scene
```
1. Go to 📖 Story Generation
2. Enter: "Tell me about Hanuman's arrival in Lanka"
3. Click "Generate Story"
4. Get scene descriptions and narration
5. Go to 🎙️ TTS/Audio
6. Copy narration text and synthesize with "Veeram" emotion
7. Go to 🎨 Anime Scene, add visual description
8. Generate scene with audio
```

### Workflow 2: Explore Emotional Content
```
1. Go to 🎭 Emotions & Characters
2. Select each emotion: Veeram, Karuna, Hasya, etc.
3. View scenes with specific emotional tones
4. Select a scene and get TTS narration
5. Create anime visualization
```

### Workflow 3: Search and Narrate
```
1. Go to 🔍 RAG Search
2. Enter custom query about Sundarakanda
3. Review found chunks
4. Take best chunk text
5. Go to 🎙️ TTS/Audio
6. Paste text and synthesize
7. Download audio
```

## 🎓 Learning Path

**Beginner (15 min)**
- [ ] Start application
- [ ] Check status
- [ ] Load Sundarakanda
- [ ] Generate a story
- [ ] Listen to TTS

**Intermediate (30 min)**
- [ ] Search vector store with multiple queries
- [ ] Filter by different emotions
- [ ] Generate anime scenes
- [ ] Explore different voice emotions
- [ ] Create complete scene with audio

**Advanced (1 hour)**
- [ ] Combine story generation with RAG
- [ ] Create multi-scene narratives with consistent tones
- [ ] Integrate with external anime APIs (future)
- [ ] Setup production AWS Polly
- [ ] Deploy to cloud platform

## 🚢 Production Deployment

### Build for Production
```bash
mvn clean package -DskipTests
java -Dapp.demo-mode=false \
     -DANTHROPICKEY=your-actual-key \
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

### Environment-Specific Properties
Create `application-prod.properties`:
```properties
app.demo-mode=false
server.port=8080
logging.level.root=WARN
spring.ai.anthropic.chat.options.timeout=30
```

Run with profile:
```bash
java -Dspring.profiles.active=prod -jar app.jar
```

## 📚 Additional Resources

- **UI Documentation:** See UI_DOCUMENTATION.md for detailed feature guide
- **API Reference:** See API_DOCUMENTATION.md for endpoint details
- **Architecture:** See ARCHITECTURE.md for system design
- **Complete Guide:** See COMPLETE_IMPLEMENTATION.md for full technical details

## 💡 Tips & Tricks

### Keyboard Shortcuts
- **Alt + 1:** Story Generation tab
- **Alt + 2:** RAG Search tab
- **Alt + 3:** TTS/Audio tab
- **Alt + 4:** Anime Scene tab
- **Alt + 5:** Emotions & Characters tab
- **Alt + 6:** Vector Store tab

### Best Practices
1. **Always load Sundarakanda first** before searching
2. **Use demo mode** for testing without API keys
3. **Match emotions** to narration content (e.g., Veeram for heroic scenes)
4. **Test one feature** at a time
5. **Check browser console** (F12) for detailed error messages

### Performance Tips
1. First query might be slower (LLM warmup)
2. Subsequent queries are cached in browser
3. Audio synthesis is faster with demo mode
4. Keep vector store loaded for multiple searches

## 🎯 Next Steps

1. ✅ Run the application
2. ✅ Explore all 6 tabs
3. ✅ Test each feature
4. ✅ Review UI_DOCUMENTATION.md for advanced features
5. ✅ Setup production deployment when ready

## 📞 Support

If you encounter issues:
1. Check application.properties settings
2. Review browser console for errors (F12)
3. Check Spring Boot logs for backend errors
4. Verify all dependencies are installed: `mvn clean install`
5. Clear browser cache and reload

Enjoy creating cultural narratives! 🎭🎬📚
