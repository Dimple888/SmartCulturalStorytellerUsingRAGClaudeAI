# Smart Cultural Storyteller - UI/UX Documentation

## Overview

The Smart Cultural Storyteller provides a comprehensive web-based user interface for generating, managing, and interacting with cultural stories using AI, RAG, and text-to-speech technology.

## Architecture

### Frontend Structure
- **index.html** - Main application shell with tabbed interface
- **styles.css** - Production-ready responsive styling with dark theme
- **app.js** - Client-side API integration and tab management

### Backend Integration
- RESTful API endpoints at `http://localhost:8080/api/narrator`
- CORS-enabled for cross-origin requests
- JSON request/response format
- Error handling with fallback messages

## Features & Use Cases

### 1. 📖 Story Generation Tab
**Purpose:** Create full narrative stories from Sundarakanda using RAG and Claude AI

**Features:**
- Custom story naming
- User query input for story content
- Anime style selection (Anime, Manga, Fantasy, Epic)
- Scene count configuration (1-10 scenes)
- Real-time response with scene breakdowns

**API Endpoint:** `POST /generate-story`
```json
{
  "storyName": "Hanuman's Heroic Journey",
  "userQuery": "Tell me the complete story of Hanuman's journey to Lanka",
  "style": "anime",
  "maxScenes": 5
}
```

### 2. 🔍 RAG Search Tab
**Purpose:** Perform semantic similarity search across the Sundarakanda vector store

**Features:**
- Keyword-based semantic search
- Retrieve cultural text in full
- Display chunk metadata (emotion, characters, location)
- Support for context-aware queries

**API Endpoints:**
- `GET /search-rag?query={query}`
- `GET /cultural-text`

### 3. 🎙️ TTS/Audio Synthesis Tab
**Purpose:** Convert narration text to emotional speech using AWS Polly

**Features:**
- 8 Indian Rasas (emotions) for voice mapping
- English and Hindi language support
- Real-time audio playback
- Voice recommendation display
- Audio download capability

**Voice Mapping (8 Rasas):**
| Emotion (Rasa) | English Voice | Hindi Voice | Meaning |
|---|---|---|---|
| Veeram | Joanna | Aditi | Heroism |
| Shringar | Emma | Raveena | Love/Romance |
| Karuna | Amy | Aditi | Compassion |
| Bhaya | Brian | Kalpana | Fear |
| Hasya | Ivy | Raveena | Joy |
| Adbhuta | Salli | Aditi | Wonder |
| Raudra | Matthew | Kalpana | Anger |
| Bibhatsa | Russell | Kalpana | Disgust |

**API Endpoints:**
- `POST /tts/synthesize` - Synthesize text to speech
- `GET /tts/voices` - Get available voice configurations

### 4. 🎨 Anime Scene Generation Tab
**Purpose:** Create anime scene descriptions with synchronized narration and audio

**Features:**
- Narration text input
- Scene visual description
- Emotion-based voice assignment
- Audio synchronization
- Image prompt generation for anime rendering

**API Endpoint:** `POST /anime-scene-with-tts`

### 5. 🎭 Emotions & Characters Tab
**Purpose:** Filter story content by emotional tone or character focus

**Features:**
- Filter scenes by Rasa (emotion)
- Filter scenes by character name
- Display scene metadata
- View location and character associations

**API Endpoints:**
- `GET /emotion/{emotion}`
- `GET /character/{character}`

### 6. 📊 Vector Store Management Tab
**Purpose:** Manage and monitor the RAG vector database

**Features:**
- Load Sundarakanda into vector store
- View vector store statistics
- Monitor chunk count
- Check indexed emotions and characters
- System information display

**API Endpoints:**
- `POST /load-sundarakanda`
- `GET /vector-store-stats`
- `GET /info`

## UI Components

### Header
- Application title and description
- Real-time connection status indicator
- Health check button
- Visual feedback for API connectivity

### Tab Navigation
- 6 main tabs for different use cases
- Keyboard shortcuts: Alt+1 through Alt+6
- Active state indication
- Responsive mobile navigation

### Cards
- Organized feature containers
- Hover effects with color transitions
- Shadow depth for visual hierarchy
- Responsive grid layouts

### Forms
- Input fields with validation
- Text areas for longer content
- Dropdown selectors for predefined options
- Button groups for related actions

### Results Display
- Syntax-highlighted JSON output
- Formatted response tables
- Audio player with controls
- Alert boxes for success/error messages
- Expandable details sections

## Styling & Design

### Color Scheme (Dark Theme)
```css
Primary: #6366f1 (Indigo)
Secondary: #8b5cf6 (Purple)
Success: #10b981 (Green)
Error: #ef4444 (Red)
Warning: #f59e0b (Amber)
Background: #0f172a (Dark Navy)
```

### Typography
- Font Family: Segoe UI, Tahoma, Geneva, Verdana
- Heading Scale: 1.2x to 2.5rem
- Line Height: 1.6 for readability
- Monospace: Courier New for code

### Responsive Breakpoints
- Desktop: Full-width layout
- Tablet (1024px): Single column cards
- Mobile (768px): Optimized touch targets
- Mobile Small (480px): Stacked layouts

## API Integration Flow

```
User Action → Form Validation → API Call → Response Parsing → UI Update
              ↓                  ↓           ↓                ↓
           Spinner Start    Show Loading   Parse JSON      Animate Results
                            Error Handling  Handle Errors   Display Data
```

## Error Handling

### Client-Side
- Form validation before submission
- Network error detection
- JSON parsing error handling
- User-friendly error messages

### Server-Side Integration
- HTTP status code checking
- JSON error response parsing
- Fallback messages for demo mode
- Detailed error logging in console

## Browser Compatibility

- Chrome/Chromium 90+
- Firefox 88+
- Safari 14+
- Edge 90+
- Mobile browsers (iOS Safari, Chrome Mobile)

## Performance Optimizations

1. **CSS Animations:** GPU-accelerated with `transform`
2. **Lazy Loading:** Tabs load content on demand
3. **Debounced Inputs:** Prevents excessive API calls
4. **Local Caching:** Recent queries stored in sessionStorage
5. **Minified Assets:** Production-ready CSS/JS

## Production Deployment

### Static Files Location
```
src/main/resources/static/
├── index.html
├── styles.css
└── app.js
```

### Configuration
Update `application.properties`:
```properties
spring.web.resources.static-locations=classpath:/static/
server.servlet.context-path=/
spring.mvc.static-path-pattern=/**
```

### CORS Settings
```java
@CrossOrigin(origins = "*", maxAge = 3600)
// Or configure for specific domains:
@CrossOrigin(origins = "https://yourdomain.com")
```

### AWS Polly Integration (Production)
1. Add AWS SDK dependency to pom.xml
2. Configure AWS credentials in environment variables
3. Replace mock TTS responses with actual Polly calls
4. Setup S3 bucket for audio file storage

## Usage Instructions

### Starting the Application
```bash
mvn clean spring-boot:run
```

### Accessing the UI
1. Open browser: `http://localhost:8080/`
2. UI loads automatically
3. Click "Check Status" to verify backend connectivity
4. Start using any tab

### Testing Each Feature

#### Story Generation
1. Go to "📖 Story Generation" tab
2. Enter a story name and query
3. Click "Generate Story"
4. Wait for response (demo mode may be instant)

#### RAG Search
1. Go to "🔍 RAG Search" tab
2. Enter a search query
3. Click "Search Vector Store"
4. View relevant chunks

#### TTS Synthesis
1. Go to "🎙️ TTS/Audio" tab
2. Enter text to synthesize
3. Select emotion (Rasa) and language
4. Click "Synthesize Audio"
5. Listen using embedded player

#### Anime Scene
1. Go to "🎨 Anime Scene" tab
2. Enter narration and visual description
3. Select emotion
4. Click "Generate Scene + Audio"
5. View results with audio

#### Filter by Emotion/Character
1. Go to "🎭 Emotions & Characters" tab
2. Select emotion or enter character name
3. Click appropriate button
4. View filtered scenes

#### Vector Store Management
1. Go to "📊 Vector Store" tab
2. Click "Load Sundarakanda" to populate
3. Click "Get Statistics" to view store info
4. View system information

## Troubleshooting

### Backend Not Responding
- Verify Spring Boot is running on port 8080
- Check firewall settings
- Review console for error messages
- Enable demo mode in properties

### Audio Not Playing
- Ensure AWS Polly is configured (production)
- Check browser audio permissions
- Verify mock audio URLs in demo mode

### CORS Errors
- Backend CORS configuration is enabled
- For specific domain, update @CrossOrigin annotation

### Slow Response Times
- Check server logs for bottlenecks
- Verify database queries
- Consider caching strategies

## Future Enhancements

1. **User Authentication:** JWT-based login
2. **Story Persistence:** Save and load custom stories
3. **Advanced Filtering:** Multi-field search
4. **Video Generation:** Integrate with animation APIs
5. **Real-time Streaming:** Server-sent events for live updates
6. **Mobile App:** React Native companion application
7. **Multi-language Support:** UI translations
8. **Analytics Dashboard:** Usage metrics and insights

## Code Examples

### Basic API Call from UI
```javascript
async function fetchAPI(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: { 'Content-Type': 'application/json' },
        mode: 'cors'
    };
    if (body) options.body = JSON.stringify(body);
    
    const response = await fetch(`${API_BASE}${endpoint}`, options);
    return await response.json();
}
```

### Tab Switching
```javascript
function switchTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(tab => 
        tab.classList.remove('active')
    );
    document.getElementById(tabName).classList.add('active');
}
```

### Result Display
```javascript
function showResult(elementId, title, content) {
    const resultBox = document.getElementById(elementId);
    resultBox.innerHTML = `<h3>${title}</h3><pre>${JSON.stringify(content, null, 2)}</pre>`;
    resultBox.classList.add('show');
}
```

## Summary

The Smart Cultural Storyteller UI provides an intuitive, production-ready interface for exploring and generating cultural narratives with AI assistance. The modular tab-based design supports multiple use cases while maintaining code clarity and extensibility.

For questions or contributions, refer to the main README.md and architecture documentation.
