// Configuration
const API_BASE = 'http://localhost:8080/api/narrator';
let isConnected = false;

// ===========================
// Tab Management
// ===========================
function switchTab(tabName) {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });

    // Remove active class from all buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });

    // Show selected tab
    document.getElementById(tabName).classList.add('active');

    // Add active class to clicked button
    event.target.classList.add('active');
}

// ===========================
// Utility Functions
// ===========================
function showSpinner(elementId) {
    const spinner = document.getElementById(elementId);
    if (spinner) spinner.classList.add('active');
}

function hideSpinner(elementId) {
    const spinner = document.getElementById(elementId);
    if (spinner) spinner.classList.remove('active');
}

function showResult(elementId, title, content, isError = false) {
    const resultBox = document.getElementById(elementId);
    if (!resultBox) return;

    let html = `<h3>${title}</h3>`;

    if (isError) {
        html += `<div class="error">${content}</div>`;
    } else if (typeof content === 'object') {
        html += `<pre><code>${JSON.stringify(content, null, 2)}</code></pre>`;
    } else {
        html += `<div>${content}</div>`;
    }

    resultBox.innerHTML = html;
    resultBox.classList.add('show');
}

function clearResult(elementId) {
    const resultBox = document.getElementById(elementId);
    if (resultBox) {
        resultBox.classList.remove('show');
        resultBox.innerHTML = '';
    }
}

function setStatus(connected) {
    isConnected = connected;
    const statusEl = document.getElementById('status');
    if (statusEl) {
        if (connected) {
            statusEl.textContent = '● Connected';
            statusEl.classList.remove('disconnected');
            statusEl.classList.add('connected');
        } else {
            statusEl.textContent = '● Disconnected';
            statusEl.classList.remove('connected');
            statusEl.classList.add('disconnected');
        }
    }
}

async function fetchAPI(endpoint, method = 'GET', body = null) {
    try {
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            mode: 'cors',
            credentials: 'include'
        };

        if (body) {
            options.body = JSON.stringify(body);
        }

        const response = await fetch(`${API_BASE}${endpoint}`, options);

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }

        const data = await response.json();
        return { success: true, data };
    } catch (error) {
        console.error('API Error:', error);
        return { success: false, error: error.message };
    }
}

// ===========================
// Health & Status
// ===========================
async function checkHealth() {
    showSpinner('story-spinner');
    const result = await fetchAPI('/health');
    hideSpinner('story-spinner');

    if (result.success) {
        setStatus(true);
        showResult('story-result', '✅ System Health Check', 
            `<div class="success">Backend is running and responding normally</div><pre><code>${JSON.stringify(result.data, null, 2)}</code></pre>`);
    } else {
        setStatus(false);
        showResult('story-result', '❌ Connection Error', result.error, true);
    }
}

async function getApiInfo() {
    showSpinner('load-spinner');
    const result = await fetchAPI('/info');
    hideSpinner('load-spinner');

    if (result.success) {
        let html = '<div class="info">';
        html += `<strong>Backend Version:</strong> ${result.data.version || 'v1.0'}<br>`;
        html += `<strong>API Endpoint:</strong> ${API_BASE}<br>`;
        html += `<strong>LLM Model:</strong> Claude Haiku 3<br>`;
        html += `<strong>Features:</strong> RAG, TTS, Vector Search<br>`;
        html += '</div>';
        showResult('info-result', '📋 API Information', html);
    } else {
        showResult('info-result', '⚠️ Info', 
            '<div class="info">Frontend is connected. Backend may be starting up.</div>', false);
    }
}

// ===========================
// Tab 1: Story Generation
// ===========================
async function generateStory() {
    const storyName = document.getElementById('storyName').value;
    const userQuery = document.getElementById('userQuery').value;
    const style = document.getElementById('style').value;
    const maxScenes = document.getElementById('maxScenes').value;

    if (!userQuery.trim()) {
        alert('Please enter a story query');
        return;
    }

    showSpinner('story-spinner');
    clearResult('story-result');

    const request = {
        storyName: storyName || 'Untitled Story',
        userQuery: userQuery,
        style: style,
        maxScenes: parseInt(maxScenes)
    };

    const result = await fetchAPI('/generate-story', 'POST', request);
    hideSpinner('story-spinner');

    if (result.success) {
        const data = result.data;
        let html = `<div class="success">✅ Story generated successfully!</div>`;
        html += `<h4>${data.title || 'Story'}</h4>`;
        html += `<p><strong>Status:</strong> ${data.status || 'Ready'}</p>`;
        html += `<p><strong>Scenes Generated:</strong> ${data.scenes?.length || 0}</p>`;
        if (data.scenes && data.scenes.length > 0) {
            html += '<ol>';
            data.scenes.forEach((scene, idx) => {
                html += `<li>`;
                html += scene.narration ? `<div>${scene.narration}</div>` : '';
                html += scene.characters ? `<div><strong>Characters:</strong> ${scene.characters}</div>` : '';
                html += scene.location ? `<div><strong>Location:</strong> ${scene.location}</div>` : '';
                html += `</li>`;
            });
            html += '</ol>';
        } else {
            html += '<div>No scenes generated.</div>';
        }
        window.lastStoryData = data;
        showResult('story-result', '📖 Story Generated', html);
    } else {
        showResult('story-result', '❌ Error', result.error, true);
    }
}

// ===========================
// Tab 2: RAG Search
// ===========================
async function searchRAG() {
    const query = document.getElementById('ragQuery').value;

    if (!query.trim()) {
        alert('Please enter a search query');
        return;
    }

    showSpinner('rag-spinner');
    clearResult('rag-result');

    const result = await fetchAPI(`/search-rag?query=${encodeURIComponent(query)}`);
    hideSpinner('rag-spinner');

    if (result.success) {
        const chunks = result.data.results || [];
        let html = `<div class="success">✅ Found ${chunks.length} relevant chunks</div>`;
        
        if (chunks.length > 0) {
            html += '<ul>';
            chunks.forEach((chunk, idx) => {
                html += `
                    <li>
                        <strong>Chunk ${idx + 1}:</strong> ${chunk.meaning || chunk.verse || chunk.text}
                        ${chunk.emotion ? `<br><em>Emotion: ${chunk.emotion}</em>` : ''}
                        ${chunk.characters ? `<br><em>Characters: ${chunk.characters}</em>` : ''}
                    </li>
                `;
            });
            html += '</ul>';
        }
        
        showResult('rag-result', '🔍 RAG Search Results', html);
    } else {
        showResult('rag-result', '❌ Error', result.error, true);
    }
}

async function retrieveCulturalText() {
    showSpinner('rag-spinner');
    clearResult('cultural-result');

    const result = await fetchAPI('/cultural-text');
    hideSpinner('rag-spinner');

    if (result.success) {
        const text = result.data.text || result.data;
        const preview = text.substring(0, 500) + (text.length > 500 ? '...' : '');
        let html = `
            <div class="success">✅ Retrieved cultural text (Sundarakanda)</div>
            <p><strong>Character count:</strong> ${text.length.toLocaleString()}</p>
            <h4>Preview:</h4>
            <p>${preview}</p>
            <details>
                <summary>View Full Text</summary>
                <pre><code>${text}</code></pre>
            </details>
        `;
        showResult('cultural-result', '📚 Cultural Text', html);
    } else {
        showResult('cultural-result', '❌ Error', result.error, true);
    }
}

// ===========================
// Tab 3: TTS Synthesis
// ===========================
async function synthesizeTTS() {
    const text = document.getElementById('ttsText').value;
    const emotion = document.getElementById('ttsEmotion').value;
    const language = document.getElementById('ttsLanguage').value;

    if (!text.trim()) {
        alert('Please enter text to synthesize');
        return;
    }

    showSpinner('tts-spinner');
    clearResult('tts-result');

    const request = {
        text: text,
        emotion: emotion,
        language: language
    };

    const result = await fetchAPI('/tts/synthesize', 'POST', request);
    hideSpinner('tts-spinner');

    if (result.success) {
        const data = result.data;
        let html = `<div class="success">✅ Audio synthesized successfully!</div>`;
        html += `<p><strong>Emotion:</strong> ${data.emotion}</p>`;
        html += `<p><strong>Voice:</strong> ${data.voiceId}</p>`;
        html += `<p><strong>Language:</strong> ${data.language}</p>`;
        
        if (data.audioUrl) {
            html += `
                <div class="audio-player">
                    <h4>🎵 Play Audio:</h4>
                    <audio controls style="width: 100%;">
                        <source src="${data.audioUrl}" type="audio/mpeg">
                        Your browser does not support the audio element.
                    </audio>
                    <p><a href="${data.audioUrl}" download="narration.mp3">📥 Download Audio</a></p>
                </div>
            `;
        } else if (data.message) {
            html += `<div class="info">${data.message}</div>`;
        }

        showResult('tts-result', '🎙️ TTS Synthesis Result', html);
    } else {
        showResult('tts-result', '❌ Error', result.error, true);
    }
}

async function getTTSVoices() {
    showSpinner('tts-spinner');
    clearResult('voices-result');

    const result = await fetchAPI('/tts/voices');
    hideSpinner('tts-spinner');

    if (result.success) {
        const voices = result.data || [];
        let html = `<div class="success">✅ Loaded ${voices.length} voice configurations</div>`;
        html += '<table style="width: 100%; border-collapse: collapse; margin-top: 15px;">';
        html += '<tr style="border-bottom: 1px solid var(--border-color);"><th style="text-align: left; padding: 10px;">Emotion (Rasa)</th><th style="text-align: left; padding: 10px;">Voice ID (EN)</th><th style="text-align: left; padding: 10px;">Voice ID (HI)</th><th style="text-align: left; padding: 10px;">Description</th></tr>';
        
        voices.forEach(voice => {
            html += `<tr style="border-bottom: 1px solid var(--border-color);">
                <td style="padding: 10px;"><strong>${voice.emotion}</strong></td>
                <td style="padding: 10px;">${voice.voiceEN || 'N/A'}</td>
                <td style="padding: 10px;">${voice.voiceHI || 'N/A'}</td>
                <td style="padding: 10px;">${voice.description || ''}</td>
            </tr>`;
        });
        
        html += '</table>';
        showResult('voices-result', '🎤 Available Voices & Emotional Mapping', html);
    } else {
        showResult('voices-result', '❌ Error', result.error, true);
    }
}

// ===========================
// Tab 4: Anime Scene Generation
// ===========================
async function generateAnimeScene() {
    const narration = document.getElementById('sceneNarration').value;
    const description = document.getElementById('sceneDescription').value;
    const emotion = document.getElementById('sceneEmotion').value;

    if (!narration.trim() || !description.trim()) {
        alert('Please fill in narration and scene description');
        return;
    }

    showSpinner('anime-spinner');
    clearResult('anime-result');

    const request = {
        narration: narration,
        sceneDescription: description,
        emotion: emotion
    };

    const result = await fetchAPI('/anime-scene-with-tts', 'POST', request);
    hideSpinner('anime-spinner');

    if (result.success) {
        const data = result.data;
        let html = `<div class="success">✅ Anime scene generated with audio!</div>`;
        html += `<h4>${data.sceneTitle || 'Anime Scene'}</h4>`;
        html += `<p><strong>Emotion:</strong> ${data.emotion}</p>`;
        
        if (data.sceneVisualization) {
            html += `<h4>Scene Description:</h4><p>${data.sceneVisualization}</p>`;
        }
        
        if (data.narratorVoice) {
            html += `<p><strong>Narrator Voice:</strong> ${data.narratorVoice}</p>`;
        }

        if (data.audioUrl) {
            html += `
                <div class="audio-player">
                    <h4>🎵 Narration Audio:</h4>
                    <audio controls style="width: 100%;">
                        <source src="${data.audioUrl}" type="audio/mpeg">
                        Your browser does not support the audio element.
                    </audio>
                </div>
            `;
        }

        if (data.metadata) {
            html += `<details><summary>View Metadata</summary><pre><code>${JSON.stringify(data.metadata, null, 2)}</code></pre></details>`;
        }

        showResult('anime-result', '🎨 Anime Scene with Audio', html);
    } else {
        showResult('anime-result', '❌ Error', result.error, true);
    }
}

function generateAnimeVideoAndPlay() {
    showSpinner('anime-spinner');
    // Always hide the video first
    const container = document.getElementById('anime-video-container');
    if (container) container.style.display = 'none';
    setTimeout(() => {
        hideSpinner('anime-spinner');
        if (container) {
            container.style.display = 'block';
            // Scroll to video for visibility
            container.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
        const video = container ? container.querySelector('video') : null;
        if (video) {
            video.load();
            video.play();
        } else {
            alert('Video element not found!');
        }
    }, 500); // Simulate async, replace with actual API call if needed
}

// ===========================
// Tab 5: Emotion & Character Filter
// ===========================
async function filterByEmotion() {
    const emotion = document.getElementById('emotionFilter').value;

    showSpinner('emotion-spinner');
    clearResult('emotion-result');

    const result = await fetchAPI(`/emotion/${encodeURIComponent(emotion)}`);
    hideSpinner('emotion-spinner');

    if (result.success) {
        const chunks = result.data.chunks || [];
        let html = `<div class=\"success\">✅ Found ${chunks.length} scenes with emotion: <strong>${emotion}</strong></div>`;
        
        if (chunks.length > 0) {
            html += '<ul>';
            chunks.forEach((chunk, idx) => {
                html += `
                    <li>
                        <strong>Scene ${idx + 1}:</strong> ${chunk.meaning || chunk.verse || chunk.sceneIntent || 'N/A'}
                        ${chunk.location ? `<br><em>Location: ${chunk.location}</em>` : ''}
                        ${chunk.characters ? `<br><em>Characters: ${Array.isArray(chunk.characters) ? chunk.characters.join(', ') : chunk.characters}</em>` : ''}
                    </li>
                `;
            });
            html += '</ul>';
        } else {
            html += '<div class=\"warning\">No scenes found with this emotion</div>';
        }
        
        showResult('emotion-result', `🎭 Scenes with ${emotion}`, html);
    } else {
        showResult('emotion-result', '❌ Error', result.error, true);
    }
}

async function filterByCharacter() {
    const character = document.getElementById('characterFilter').value;

    if (!character.trim()) {
        alert('Please enter a character name');
        return;
    }

    showSpinner('character-spinner');
    clearResult('character-result');

    const result = await fetchAPI(`/character/${encodeURIComponent(character)}`);
    hideSpinner('character-spinner');

    if (result.success) {
        const chunks = result.data.chunks || [];
        let html = `<div class=\"success\">✅ Found ${chunks.length} scenes featuring: <strong>${character}</strong></div>`;
        
        if (chunks.length > 0) {
            html += '<ul>';
            chunks.forEach((chunk, idx) => {
                html += `
                    <li>
                        <strong>Scene ${idx + 1}:</strong> ${chunk.meaning || chunk.verse || chunk.sceneIntent || 'N/A'}
                        ${chunk.emotion ? `<br><em>Emotion: ${chunk.emotion}</em>` : ''}
                        ${chunk.location ? `<br><em>Location: ${chunk.location}</em>` : ''}
                    </li>
                `;
            });
            html += '</ul>';
        } else {
            html += '<div class=\"warning\">No scenes found with this character</div>';
        }
        
        showResult('character-result', `👤 Scenes with ${character}`, html);
    } else {
        showResult('character-result', '❌ Error', result.error, true);
    }
}

// ===========================
// Tab 6: Vector Store Management
// ===========================
async function loadSundarakanda() {
    showSpinner('load-spinner');
    clearResult('vector-result');

    const result = await fetchAPI('/load-sundarakanda', 'POST', {});
    hideSpinner('load-spinner');

    if (result.success) {
        const data = result.data;
        let html = `<div class="success">✅ Sundarakanda loaded successfully!</div>`;
        html += `<p><strong>Chunks loaded:</strong> ${data.chunksLoaded || data.status}</p>`;
        html += `<p><strong>Status:</strong> Vector store is ready for RAG queries</p>`;
        showResult('vector-result', '📊 Vector Store Loaded', html);
    } else {
        showResult('vector-result', '❌ Error', result.error, true);
    }
}

async function getVectorStoreStats() {
    showSpinner('load-spinner');
    clearResult('vector-result');

    const result = await fetchAPI('/vector-store-stats');
    hideSpinner('load-spinner');

    if (result.success) {
        const data = result.data;
        let html = `<div class="success">✅ Vector Store Statistics</div>`;
        html += `<p><strong>Total chunks:</strong> ${data.totalChunks || data.chunkCount || 'N/A'}</p>`;
        html += `<p><strong>Status:</strong> ${data.status || 'Ready'}</p>`;
        if (data.emotions) {
            html += `<p><strong>Emotions indexed:</strong> ${data.emotions.join(', ')}</p>`;
        }
        if (data.characters) {
            html += `<p><strong>Characters indexed:</strong> ${data.characters.join(', ')}</p>`;
        }
        showResult('vector-result', '📈 Vector Store Stats', html);
    } else {
        showResult('vector-result', '⚠️ Info', 
            '<div class="info">Automatic stats not available. Use other endpoints to query the vector store.</div>', false);
    }
}

// ===========================
// Initialization
// ===========================
document.addEventListener('DOMContentLoaded', () => {
    // Auto-check health on load
    setTimeout(checkHealth, 1000);

    // Setup keyboard shortcuts (Alt + Tab key to navigate)
    document.addEventListener('keydown', (e) => {
        if (e.altKey) {
            const tabNum = parseInt(e.key);
            const tabs = document.querySelectorAll('.tab-btn');
            if (tabNum >= 1 && tabNum <= tabs.length) {
                tabs[tabNum - 1].click();
            }
        }
    });

    console.log('Smart Cultural Storyteller UI loaded');
    console.log(`API Base: ${API_BASE}`);
});

// ===========================
// Error Handler
// ===========================
window.addEventListener('error', (event) => {
    console.error('Global Error:', event.error);
});

window.addEventListener('unhandledrejection', (event) => {
    console.error('Unhandled Promise Rejection:', event.reason);
});
