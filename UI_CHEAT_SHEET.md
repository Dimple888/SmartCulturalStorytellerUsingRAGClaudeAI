# UI Cheat Sheet - Quick Reference

## 🎯 Access Points

| Resource | URL | Purpose |
|----------|-----|---------|
| **Main UI** | `http://localhost:8080/` | 6-tab interactive dashboard |
| **Feature Demo** | `http://localhost:8080/demo.html` | Feature showcase page |
| **API Health** | `http://localhost:8080/api/narrator/health` | System status check |

---

## 📑 Tab Quick Guide

### Tab 1: 📖 Story Generation
**What:** Create full anime stories from Sundarakanda
```
Input:  Story name + Query + Style + Scene count
Output: Story with multiple scenes and narration
Time:   2-5 seconds (demo) / 10-30 seconds (real API)
```

**Example Input:**
- Story Name: "Hanuman's Heroic Journey"
- Query: "Tell me the complete story of Hanuman's journey to Lanka"
- Style: "Anime"
- Max Scenes: 5

**Expected Output:**
```json
{
  "storyName": "Hanuman's Heroic Journey",
  "status": "generated",
  "scenes": [...],
  "narration": "..."
}
```

---

### Tab 2: 🔍 RAG Search
**What:** Search Sundarakanda knowledge base semantically
```
Input:  Search query (keyword or phrase)
Output: List of relevant chunks with metadata
Time:   <1 second
```

**Example Queries:**
- "Hanuman's leap across the ocean"
- "Sita in Lanka"
- "Battle with demons"
- "Devotion to Rama"

**Chunk Metadata Returned:**
- Verse/meaning
- Emotion (Rasa)
- Characters involved
- Location/scene
- Intent/summary

---

### Tab 3: 🎙️ TTS Synthesis
**What:** Convert text to emotionally-aware speech
```
Input:  Text + Emotion + Language
Output: Audio file with playback
Time:   1-3 seconds (demo) / 5-10 seconds (real API)
```

**Emotions (Rasas) - Pick One:**
1. **Veeram** (Heroism) - Bold, powerful
2. **Shringar** (Love) - Warm, tender
3. **Karuna** (Compassion) - Gentle, soothing
4. **Bhaya** (Fear) - Anxious, tense
5. **Hasya** (Joy) - Happy, cheerful
6. **Adbhuta** (Wonder) - Amazed, in awe
7. **Raudra** (Anger) - Intense, forceful
8. **Bibhatsa** (Disgust) - Repulsive, vile

**Languages Supported:**
- English (EN) - Polly standard voices
- Hindi (HI) - Polly Hindi voices (if configured)

---

### Tab 4: 🎨 Anime Scene
**What:** Generate visual scene with synchronized audio
```
Input:  Narration + Scene description + Emotion
Output: Scene details + Audio narration
Time:   2-5 seconds
```

**Example:**
```
Narration: "Hanuman stands atop Mount Mahendra..."
Scene: "Heroic pose, mountain cliff, glowing aura"
Emotion: "Veeram"
```

---

### Tab 5: 🎭 Emotions & Characters
**What:** Filter content by emotional tone or character
```
Left Side:  Filter by emotion (Rasa)
Right Side: Filter by character name

Input:  Select emotion OR enter character
Output: List of related scenes
Time:   <1 second
```

**Example Searches:**
- Emotion: "Veeram" → Heroic scenes
- Emotion: "Karuna" → Compassion scenes
- Character: "Hanuman" → All Hanuman scenes
- Character: "Sita" → All Sita scenes

---

### Tab 6: 📊 Vector Store
**What:** Manage the knowledge base
```
Action 1: Load Sundarakanda into vector store
Action 2: View statistics
Action 3: Get system information
```

**Steps to Load:**
1. Click "Load Sundarakanda into Vector Store"
2. Wait for response: "Chunks loaded: 8"
3. Now other tabs can search the data

---

## ⌨️ Keyboard Shortcuts

| Shortcut | Tab |
|----------|-----|
| **Alt + 1** | 📖 Story Generation |
| **Alt + 2** | 🔍 RAG Search |
| **Alt + 3** | 🎙️ TTS Synthesis |
| **Alt + 4** | 🎨 Anime Scene |
| **Alt + 5** | 🎭 Emotions & Characters |
| **Alt + 6** | 📊 Vector Store |

---

## 🔥 Quick Workflow Examples

### Workflow 1: Create a Story Scene with Audio
```
1. Tab 1: Generate a story → Get narration
2. Copy narration text
3. Tab 3: Paste text → Select emotion → Synthesize
4. Tab 4: Add visual description → Generate scene
5. Result: Scene + synchronized audio
```

### Workflow 2: Explore Emotional Content
```
1. Tab 6: Load Sundarakanda (if not loaded)
2. Tab 5: Select emotion → View scenes
3. Repeat for different emotions
4. Tab 3: Pick favorite scene text → Synthesize
5. Result: Curated emotional content with audio
```

### Workflow 3: Find and Narrate
```
1. Tab 2: Search for specific content
2. Review results
3. Select best chunk
4. Tab 3: Paste text → Choose emotion → Synthesize
5. Download audio
6. Use in presentation/video
```

---

## 📊 Response Examples

### Story Generation Response
```json
{
  "storyName": "Hanuman's Heroic Journey",
  "status": "generated",
  "scenes": 5,
  "narration": "Hanuman stood upon Mount Mahendra...",
  "metadata": {
    "style": "anime",
    "characters": ["Hanuman", "Rama", "Sita"],
    "emotions": ["Veeram", "Karuna", "Hasya"]
  }
}
```

### RAG Search Response
```json
{
  "query": "Hanuman ocean",
  "resultsFound": 3,
  "results": [
    {
      "id": "sk_002",
      "verse": "With a mighty roar, Hanuman leaped...",
      "emotion": "Veeram",
      "characters": ["Hanuman"],
      "location": "The Vast Ocean"
    }
  ]
}
```

### TTS Response
```json
{
  "text": "Hanuman stood upon the mountain...",
  "emotion": "Veeram",
  "language": "EN",
  "audioUrl": "data:audio/mpeg;base64,...",
  "voiceId": "Joanna",
  "status": "success"
}
```

### Filter by Emotion Response
```json
{
  "emotion": "Veeram",
  "chunksFound": 4,
  "chunks": [
    {
      "id": "sk_001",
      "meaning": "Hanuman prepares...",
      "emotion": "Veeram",
      "characters": ["Hanuman"]
    }
  ]
}
```

---

## 🎨 Color Meanings

| Color | Meaning |
|-------|---------|
| 🟦 Blue (Primary) | Active, clickable, important |
| 🟪 Purple (Secondary) | Hover, interactive |
| 🟩 Green | Success, positive |
| 🟥 Red | Error, negative |
| 🟨 Yellow | Warning, caution |
| ⬛ Dark Gray | Background, neutral |

---

## 📱 Mobile Tips

- Use portrait orientation for best layout
- Tap tab buttons to navigate
- Scroll within cards for long content
- Audio player controls work on mobile
- Keyboard accessible on devices with keyboard

---

## 🐛 Quick Troubleshooting

| Issue | Solution |
|-------|----------|
| "Cannot connect" | Check Spring Boot is running: `mvn spring-boot:run` |
| "No results found" | Load Sundarakanda in Vector Store tab first |
| "Audio not playing" | Check browser volume + audio permissions |
| "Button not responding" | Clear browser cache (Ctrl+Shift+Del) |
| "Very slow response" | Demo mode OFF = real API calls (slower) |
| "Tab won't load" | Reload page (Ctrl+R) |
| "Error message unclear" | Open browser console (F12) for details |

---

## ✅ Pre-Use Checklist

- [ ] Application running: `mvn spring-boot:run`
- [ ] UI accessible: `http://localhost:8080/`
- [ ] Status shows "Connected"
- [ ] Sundarakanda loaded (Vector Store tab)
- [ ] No errors in browser console (F12)
- [ ] Demo mode enabled (default)

---

## 🎓 Feature Learning Order

**First Time Users:**
1. Start with **Tab 1** - Story Generation
2. Try **Tab 3** - TTS (listen to stories)
3. Explore **Tab 2** - RAG Search
4. Play with **Tab 5** - Filter by emotion

**Power Users:**
1. Use **Tab 1** for content creation
2. Use **Tab 2** for research/references
3. Use **Tab 3** for production audio
4. Use **Tab 4** for complete scenes
5. Use **Tab 6** for data management

---

## 💡 Pro Tips

1. **For Better Stories:** Use specific, detailed queries in Tab 1
2. **For Accurate Search:** Use emotion/character context in Tab 2
3. **For Perfect Audio:** Match emotion (Rasa) to content in Tab 3
4. **For Quick Navigation:** Use Alt+Tab# keyboard shortcuts
5. **For Offline Use:** Download audio in Tab 3 before leaving
6. **For Batch Processing:** Use Vector Store stats in Tab 6

---

## 🔧 System Requirements

| Component | Requirement |
|-----------|-------------|
| **Browser** | Chrome/Firefox/Safari/Edge (recent) |
| **Java** | 17 or higher |
| **Maven** | 3.6 or higher |
| **RAM** | 512MB minimum, 2GB recommended |
| **Disk** | 500MB for application + data |
| **Internet** | Required for real Claude API (not needed in demo mode) |

---

## 📞 Quick Links

| Resource | Link | Purpose |
|----------|------|---------|
| **Main UI** | `http://localhost:8080/` | Use the application |
| **Demo Page** | `http://localhost:8080/demo.html` | Learn features |
| **API Status** | `http://localhost:8080/api/narrator/health` | Check backend |
| **Full Docs** | `README_COMPLETE.md` | Complete guide |
| **UI Guide** | `UI_DOCUMENTATION.md` | Feature details |
| **Quick Start** | `QUICK_START_UI.md` | 5-minute setup |

---

## 🎬 Example Outputs

### Story Fragment
```
"Hanuman, the mighty vanar warrior, stood upon Mount Mahendra 
gazing across the vast expanse of the ocean. His heart burned 
with the fire of devotion to Lord Rama. With a mighty roar that 
echoed across the mountains, he took a deep breath and began 
his legendary leap across the treacherous waters..."
```

### Audio Output
```
▶️ Duration: 0:15
🎵 Voice: Joanna (Veeram - Heroism)
📊 Sample Rate: 24000 Hz
💾 Format: MP3
```

### Scene Description
```
VISUAL: Hanuman on mountain peak
- Heroic stance, muscular form
- Glowing celestial aura
- Wind billowing around him
- Ocean below with waves
- Sunset sky with dramatic lighting

EMOTION: Veeram (Heroism)
NARRATION: [Audio plays with powerful voice tone]
```

---

## 🚀 Next Actions

1. **Start Application**
   ```bash
   mvn clean spring-boot:run
   ```

2. **Open UI**
   - Visit: `http://localhost:8080/`
   - Or: `http://localhost:8080/demo.html` (learn features)

3. **Try Features**
   - Load Sundarakanda (Tab 6)
   - Generate a story (Tab 1)
   - Synthesize audio (Tab 3)

4. **Explore**
   - Search RAG (Tab 2)
   - Filter emotions (Tab 5)
   - Combine features

---

**Happy Storytelling! 🎭📚🎬**

*For complete documentation, see README_COMPLETE.md*
