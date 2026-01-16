# Multimedia Generation Integration Guide

## Overview

The Smart Cultural Storyteller API now includes integrated multimedia video generation capabilities. You can combine generated images (from DALL-E) and audio files (from OpenAI TTS) into complete anime videos using FFmpeg.

---

## Prerequisites

### 1. FFmpeg Installation

FFmpeg is required for video generation.

**Windows Installation:**

```powershell
# Using Chocolatey (recommended)
choco install ffmpeg

# Or download from: https://ffmpeg.org/download.html
```

**Verify Installation:**
```bash
ffmpeg -version
```

**Or use the API health check:**
```bash
curl http://localhost:8080/api/narrator/multimedia/ffmpeg-status
```

---

## New API Endpoints

### 1. Generate Video from Image & Audio

**Endpoint:** `POST /api/narrator/generate-anime-video`

**Request:**
```json
{
  "imagePath": "C:\\Users\\Dimple Sai Naveena\\anime\\hanuman-scene-1.png",
  "audioPath": "C:\\Users\\Dimple Sai Naveena\\anime\\hanuman-scene-1.mp3",
  "outputPath": "C:\\Users\\Dimple Sai Naveena\\anime\\hanuman-scene-1.mp4",
  "videoName": "Hanuman at Mount Mahendra"
}
```

**Response:**
```json
{
  "success": true,
  "videoPath": "C:\\Users\\Dimple Sai Naveena\\anime\\hanuman-scene-1.mp4",
  "message": "Video generated successfully",
  "generationTime": 15000,
  "videoName": "Hanuman at Mount Mahendra"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/narrator/generate-anime-video \
  -H "Content-Type: application/json" \
  -d '{
    "imagePath": "C:\\Users\\Dimple Sai Naveena\\anime\\hanuman-scene-1.png",
    "audioPath": "C:\\Users\\Dimple Sai Naveena\\anime\\hanuman-scene-1.mp3",
    "outputPath": "C:\\Users\\Dimple Sai Naveena\\anime\\hanuman-scene-1.mp4",
    "videoName": "Hanuman at Mount Mahendra"
  }'
```

---

### 2. Generate Complete Story Video

**Endpoint:** `POST /api/narrator/generate-anime-story-video`

Generates a complete anime video from a story request, combining all scenes into one video.

**Request:**
```json
{
  "storyId": "sundarakanda-001",
  "storyName": "Sundarakanda",
  "userQuery": "Tell Hanuman's story as anime",
  "userPreferences": "epic, heroic",
  "style": "anime-cinematic",
  "maxScenes": 8,
  "language": "en"
}
```

**Response:**
```json
{
  "success": true,
  "videoPath": "./anime-output/sundarakanda-001-complete.mp4",
  "message": "Complete anime video generated successfully",
  "generationTime": 120000,
  "videoName": "Sundarakanda"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/narrator/generate-anime-story-video \
  -H "Content-Type: application/json" \
  -d '{
    "storyId": "sundarakanda-001",
    "storyName": "Sundarakanda",
    "userQuery": "Tell Hanuman story as anime",
    "userPreferences": "epic, heroic",
    "style": "anime-cinematic",
    "maxScenes": 8,
    "language": "en"
  }'
```

---

### 3. Check FFmpeg Status

**Endpoint:** `GET /api/narrator/multimedia/ffmpeg-status`

**Response:**
```json
{
  "ffmpeg_available": true,
  "version": "ffmpeg version 6.0 ...",
  "service": "Multimedia Generation Service"
}
```

**cURL Command:**
```bash
curl http://localhost:8080/api/narrator/multimedia/ffmpeg-status
```

---

### 4. Generate Batch Videos

**Endpoint:** `POST /api/narrator/multimedia/batch-videos`

Generate multiple videos at once.

**Request:**
```json
[
  {
    "imagePath": "C:\\anime\\scene-1.png",
    "audioPath": "C:\\anime\\scene-1.mp3",
    "outputPath": "C:\\anime\\scene-1.mp4",
    "videoName": "Scene 1"
  },
  {
    "imagePath": "C:\\anime\\scene-2.png",
    "audioPath": "C:\\anime\\scene-2.mp3",
    "outputPath": "C:\\anime\\scene-2.mp4",
    "videoName": "Scene 2"
  }
]
```

**Response:**
```json
{
  "total_videos": 2,
  "successful": 2,
  "failed": 0,
  "results": [
    {
      "success": true,
      "videoPath": "C:\\anime\\scene-1.mp4",
      "message": "Success",
      "generationTime": 12000,
      "videoName": "Scene 1"
    },
    {
      "success": true,
      "videoPath": "C:\\anime\\scene-2.mp4",
      "message": "Success",
      "generationTime": 14000,
      "videoName": "Scene 2"
    }
  ]
}
```

---

## Complete Workflow

### Step 1: Generate Story

```bash
curl -X POST http://localhost:8080/api/narrator/generate-story \
  -H "Content-Type: application/json" \
  -d '{
    "storyId": "sundarakanda-001",
    "storyName": "Sundarakanda",
    "userQuery": "Tell Hanuman story as anime",
    "style": "anime-cinematic",
    "maxScenes": 8
  }' > story.json
```

### Step 2: Extract Scene Data

From `story.json`, get for each scene:
- `imagePrompt` - Use with DALL-E
- `audioText` - Use with OpenAI TTS

### Step 3: Generate Images

For each scene:
```bash
curl -X POST https://api.openai.com/v1/images/generations \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "dall-e-3",
    "prompt": "extracted imagePrompt from story",
    "n": 1,
    "size": "1024x1024",
    "quality": "hd"
  }' \
  --output "C:\anime\scene-1.png"
```

### Step 4: Generate Audio

For each scene:
```bash
curl -X POST https://api.openai.com/v1/audio/speech \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "tts-1-hd",
    "input": "extracted audioText from story",
    "voice": "nova"
  }' \
  --output "C:\anime\scene-1.mp3"
```

### Step 5: Generate Videos

**Single Video:**
```bash
curl -X POST http://localhost:8080/api/narrator/generate-anime-video \
  -H "Content-Type: application/json" \
  -d '{
    "imagePath": "C:\\anime\\scene-1.png",
    "audioPath": "C:\\anime\\scene-1.mp3",
    "outputPath": "C:\\anime\\scene-1.mp4",
    "videoName": "Scene 1"
  }'
```

**Or Complete Story Video:**
```bash
curl -X POST http://localhost:8080/api/narrator/generate-anime-story-video \
  -H "Content-Type: application/json" \
  -d '{
    "storyId": "sundarakanda-001",
    "storyName": "Sundarakanda",
    "userQuery": "Tell Hanuman story as anime"
  }'
```

---

## Configuration Options

In `application.properties`:

```properties
# Output directory for videos
app.multimedia.output-dir=./anime-videos

# FFmpeg path (or full path if not in system PATH)
app.multimedia.ffmpeg-path=ffmpeg

# Video codec
app.multimedia.video-codec=libx264

# Audio codec
app.multimedia.audio-codec=aac

# Audio bitrate
app.multimedia.audio-bitrate=192k
```

---

## Postman Collection Template

**Generate Video Request:**
```
Method: POST
URL: http://localhost:8080/api/narrator/generate-anime-video

Headers:
- Content-Type: application/json

Body (raw JSON):
{
  "imagePath": "{{image_path}}",
  "audioPath": "{{audio_path}}",
  "outputPath": "{{output_path}}",
  "videoName": "{{video_name}}"
}
```

**Save Postman Variables:**
- `image_path` = C:\Users\Dimple Sai Naveena\anime\scene.png
- `audio_path` = C:\Users\Dimple Sai Naveena\anime\scene.mp3
- `output_path` = C:\Users\Dimple Sai Naveena\anime\scene.mp4
- `video_name` = Scene Name

---

## Troubleshooting

### Issue: FFmpeg not found

**Solution 1:** Install FFmpeg
```bash
choco install ffmpeg
```

**Solution 2:** Set full path in application.properties
```properties
app.multimedia.ffmpeg-path=C:\\Program Files\\ffmpeg\\bin\\ffmpeg.exe
```

**Solution 3:** Check if FFmpeg is in PATH
```bash
where ffmpeg
```

### Issue: Video generation fails

**Check FFmpeg status:**
```bash
curl http://localhost:8080/api/narrator/multimedia/ffmpeg-status
```

**Check file paths:** Ensure image and audio files exist and paths are correct.

**Check permissions:** Ensure write permissions to output directory.

### Issue: No video output file

**Solutions:**
1. Check `generationTime` in response - if it's very quick, generation may have failed
2. Check application logs for error messages
3. Verify input files exist and are valid
4. Ensure output directory exists and is writable

---

## Performance Tips

1. **Use HD images cautiously** - Large images increase processing time
2. **Batch process videos** - Use the batch endpoint for multiple videos
3. **Optimize audio** - Pre-process audio to correct bitrate
4. **Cache results** - Save generated videos to avoid regeneration

---

## Advanced Usage

### Custom FFmpeg Parameters

Modify `MultimediaGenerationService.buildFFmpegCommand()` for:
- Different video codecs (h265, vp9, etc.)
- Custom frame rates
- Resolution adjustments
- Advanced audio filters

### Video Quality Settings

```properties
# For high quality (slower processing)
app.multimedia.video-codec=libx264
-crf 18

# For balanced quality/speed
app.multimedia.video-codec=libx264
-crf 28

# For fastest processing
app.multimedia.video-codec=mpeg4
-q:v 5
```

---

## Output Examples

**Successful Response:**
```json
{
  "success": true,
  "videoPath": "C:\\Users\\Dimple Sai Naveena\\anime\\hanuman-scene-1.mp4",
  "message": "Video generated successfully",
  "generationTime": 18750,
  "videoName": "Hanuman at Mount Mahendra"
}
```

**Failed Response:**
```json
{
  "success": false,
  "videoPath": null,
  "message": "Error: File not found at path",
  "generationTime": 45,
  "videoName": "Scene 1"
}
```

---

## Integration Checklist

- [ ] FFmpeg installed and accessible
- [ ] API running on http://localhost:8080
- [ ] ANTHROPIC_API_KEY environment variable set
- [ ] OPENAI_API_KEY environment variable set
- [ ] Output directories exist and are writable
- [ ] Test FFmpeg status endpoint
- [ ] Generate sample story
- [ ] Extract images from DALL-E
- [ ] Extract audio from OpenAI TTS
- [ ] Generate single video
- [ ] Verify video plays correctly
- [ ] Generate complete story video
- [ ] Batch process multiple scenes

---

## Next Steps

1. **Integrate with UI** - Create web interface for video generation
2. **Add subtitle support** - Overlay story text on videos
3. **Custom effects** - Add anime-specific effects and filters
4. **Music background** - Add background music to videos
5. **Multiple language support** - Generate videos in different languages
6. **Cloud storage** - Upload generated videos to S3/Cloud Storage
7. **Streaming** - Stream videos directly without saving to disk

---

## Support

For issues or questions:
1. Check FFmpeg status endpoint
2. Review application logs
3. Verify all prerequisites are installed
4. Check file paths and permissions
5. Validate API request format

