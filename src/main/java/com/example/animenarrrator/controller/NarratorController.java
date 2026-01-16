package com.example.animenarrrator.controller;

import com.example.animenarrrator.model.*;
import com.example.animenarrrator.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/narrator")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NarratorController {
    /**
     * Load RAG test data from rag-test-data.json into the vector store.
     */
    @PostMapping("/load-rag-test-data")
    public ResponseEntity<Map<String, Object>> loadRagTestData() {
        int loaded = vectorStoreRagService.loadTestDataFromJson();
        Map<String, Object> response = new HashMap<>();
        response.put("loaded", loaded);
        response.put("status", loaded > 0 ? "success" : "no data loaded");
        response.put("totalChunks", vectorStoreRagService.getAllChunks().size());
        return ResponseEntity.ok(response);
    }

    private final RagService ragService;
    private final StoryGenerationService storyGenerationService;
    private final AnimePromptService animePromptService;
    private final StoryOrchestratorService storyOrchestratorService;
    private final VectorStoreRagService vectorStoreRagService;
    private final TTSService ttsService;
    private final MultimediaGenerationService multimediaGenerationService;

    @Autowired
    public NarratorController(RagService ragService,
                             StoryGenerationService storyGenerationService,
                             AnimePromptService animePromptService,
                             StoryOrchestratorService storyOrchestratorService,
                             VectorStoreRagService vectorStoreRagService,
                             TTSService ttsService,
                             MultimediaGenerationService multimediaGenerationService) {
        this.ragService = ragService;
        this.storyGenerationService = storyGenerationService;
        this.animePromptService = animePromptService;
        this.storyOrchestratorService = storyOrchestratorService;
        this.vectorStoreRagService = vectorStoreRagService;
        this.ttsService = ttsService;
        this.multimediaGenerationService = multimediaGenerationService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "Smart Cultural Storyteller");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Smart Cultural Storyteller");
        response.put("version", "1.0.0");
        response.put("llmModel", "Claude Haiku 3");
        response.put("features", new String[]{"RAG", "TTS", "Vector Search", "Story Generation", "Anime Scene Generation"});
        response.put("endpoints", new String[]{
                "POST /generate-story",
                "GET /search-rag",
                "GET /cultural-text",
                "GET /emotion/{emotion}",
                "GET /character/{character}",
                "POST /tts/synthesize",
                "GET /tts/voices",
                "POST /anime-scene-with-tts",
                "POST /load-sundarakanda",
                "GET /vector-store-stats"
        });
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vector-store-stats")
    public ResponseEntity<Map<String, Object>> getVectorStoreStats() {
        Map<String, Object> response = new HashMap<>();
        int totalChunks = vectorStoreRagService.getAllChunks().size();
        response.put("totalChunks", totalChunks);
        response.put("status", totalChunks > 0 ? "Ready" : "Empty");
        response.put("emotions", new String[]{"Veeram", "Shringar", "Karuna", "Bhaya", "Hasya", "Adbhuta", "Raudra", "Bibhatsa"});
        response.put("characters", new String[]{"Hanuman", "Sita", "Rama", "Ravana", "Indrajit", "Kumbhakarna"});
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-story")
    public ResponseEntity<StoryResponse> generateStory(@RequestBody AnimeRequest request) {
        StoryNarration narration = storyGenerationService.generateStoryNarration(request);

        StoryResponse response = storyOrchestratorService.orchestrateStory(
                narration.getContent(),
                request.getStoryId() != null ? request.getStoryId() : "story_001",
                request.getStoryName() != null ? request.getStoryName() : narration.getTitle()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-rag")
    public ResponseEntity<Map<String, Object>> searchRAG(@RequestParam String query) {
        var chunks = vectorStoreRagService.retrieveRelevantChunks(query, 5);
        Map<String, Object> response = new HashMap<>();
        response.put("query", query);
        response.put("resultsFound", chunks.size());
        response.put("results", chunks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cultural-text")
    public ResponseEntity<Map<String, Object>> getCulturalText() {
        Map<String, Object> response = new HashMap<>();
        response.put("content", ragService.retrieveCulturalText());
        response.put("chunks", ragService.getVectorStoreChunkCount());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-prompts")
    public ResponseEntity<List<ScenePrompt>> generatePrompts(@RequestBody StoryNarration storyNarration) {
        return ResponseEntity.ok(animePromptService.generateScenePrompts(storyNarration));
    }

    @PostMapping("/load-sundarakanda")
    public ResponseEntity<Map<String, Object>> loadSundarakanda() throws Exception {
        int chunks = ragService.loadSundarakandaTrainingData();
        return ResponseEntity.ok(Map.of("chunksAdded", chunks));
    }

    @GetMapping("/emotion/{emotion}")
    public ResponseEntity<Map<String, Object>> getChunksByEmotion(@PathVariable("emotion") String emotion) {
        var chunks = vectorStoreRagService.getChunksByEmotion(emotion);
        Map<String, Object> response = new HashMap<>();
        response.put("emotion", emotion);
        response.put("chunksFound", chunks.size());
        response.put("chunks", chunks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/character/{character}")
    public ResponseEntity<Map<String, Object>> getChunksByCharacter(@PathVariable("character") String character) {
        var chunks = vectorStoreRagService.getChunksByCharacters(character);
        Map<String, Object> response = new HashMap<>();
        response.put("character", character);
        response.put("chunksFound", chunks.size());
        response.put("chunks", chunks);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tts/synthesize")
    public ResponseEntity<Map<String, Object>> synthesizeTTS(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        String emotion = request.getOrDefault("emotion", "Veeram");
        String language = request.getOrDefault("language", "EN");

        if (!ttsService.validateTTSRequest(text, emotion)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid TTS request - check text and emotion");
            error.put("validEmotions", ttsService.getSupportedVoices());
            return ResponseEntity.badRequest().body(error);
        }

        String audioUrl = ttsService.generateTTSAudio(text, emotion, language);
        Map<String, Object> response = new HashMap<>();
        response.put("text", text);
        response.put("emotion", emotion);
        response.put("language", language);
        response.put("audioUrl", audioUrl);
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tts/voices")
    public ResponseEntity<Map<String, Object>> getTTSVoices() {
        Map<String, Object> response = new HashMap<>();
        response.put("supportedVoices", ttsService.getSupportedVoices());
        response.put("voiceRecommendations", ttsService.getVoiceRecommendations());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/anime-scene-with-tts")
    public ResponseEntity<Map<String, Object>> generateAnimeSceneWithTTS(@RequestBody Map<String, String> request) {
        String narration = request.get("narration");
        String emotion = request.getOrDefault("emotion", "Veeram");
        String sceneDescription = request.get("sceneDescription");

        // Generate TTS audio
        String audioUrl = ttsService.generateTTSAudio(narration, emotion, "EN");

        // Prepare anime scene response
        Map<String, Object> response = new HashMap<>();
        response.put("sceneDescription", sceneDescription);
        response.put("narration", narration);
        response.put("emotion", emotion);
        response.put("audioUrl", audioUrl);
        response.put("imagePrompt", "Anime scene: " + sceneDescription);
        response.put("status", "ready_for_generation");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-anime-video")
    public ResponseEntity<VideoGenerationResponse> generateAnimeVideo(@RequestBody VideoGenerationRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            boolean success = multimediaGenerationService.generateVideoFromImageAndAudio(
                request.getImagePath(),
                request.getAudioPath(),
                request.getOutputPath()
            );

            long generationTime = System.currentTimeMillis() - startTime;
            String message = success ? "Video generated successfully" : "Failed to generate video";

            return ResponseEntity.ok(new VideoGenerationResponse(
                success,
                request.getOutputPath(),
                message,
                generationTime,
                request.getVideoName()
            ));
        } catch (Exception e) {
            long generationTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.ok(new VideoGenerationResponse(
                false,
                null,
                "Error: " + e.getMessage(),
                generationTime,
                request.getVideoName()
            ));
        }
    }

    @PostMapping("/generate-anime-story-video")
    public ResponseEntity<VideoGenerationResponse> generateAnimeStoryVideo(@RequestBody AnimeRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            // First generate the story
            StoryNarration storyNarration = storyGenerationService.generateStoryNarration(request);
            
            // Then orchestrate into scenes
            StoryResponse storyResponse = storyOrchestratorService.orchestrateStory(
            	    storyNarration.getContent(),
            	    request.getStoryId(),
            	    request.getStoryName()
            	);

            // Create list of scene media data
            List<MultimediaGenerationService.SceneMediaData> sceneDataList = new ArrayList<>();
            for (Scene scene : storyResponse.getScenes()) {
                // Note: In production, these paths should come from actual generated image/audio files
                String imagePath = String.format("./anime-output/%s-scene-%d.png", 
                    request.getStoryId(), scene.getSceneNumber());
                String audioPath = String.format("./anime-output/%s-scene-%d.mp3", 
                    request.getStoryId(), scene.getSceneNumber());
                
                sceneDataList.add(new MultimediaGenerationService.SceneMediaData(
                    scene.getSceneNumber(),
                    imagePath,
                    audioPath
                ));
            }

            // Generate complete anime video
            String outputPath = String.format("./anime-output/%s-complete.mp4", request.getStoryId());
            boolean success = multimediaGenerationService.generateCompleteAnimeVideo(sceneDataList, outputPath);

            long generationTime = System.currentTimeMillis() - startTime;
            String message = success ? "Complete anime video generated successfully" : "Failed to generate complete anime video";

            return ResponseEntity.ok(new VideoGenerationResponse(
                success,
                outputPath,
                message,
                generationTime,
                request.getStoryName()
            ));
        } catch (Exception e) {
            long generationTime = System.currentTimeMillis() - startTime;
            return ResponseEntity.ok(new VideoGenerationResponse(
                false,
                null,
                "Error: " + e.getMessage(),
                generationTime,
                request.getStoryName()
            ));
        }
    }

    @GetMapping("/multimedia/ffmpeg-status")
    public ResponseEntity<Map<String, Object>> getFFmpegStatus() {
        Map<String, Object> response = new HashMap<>();
        boolean available = multimediaGenerationService.isFFmpegAvailable();
        response.put("ffmpeg_available", available);
        response.put("version", multimediaGenerationService.getFFmpegVersion());
        response.put("service", "Multimedia Generation Service");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/multimedia/batch-videos")
    public ResponseEntity<Map<String, Object>> generateBatchVideos(@RequestBody List<VideoGenerationRequest> requests) {
        Map<String, Object> response = new HashMap<>();
        List<VideoGenerationResponse> results = new ArrayList<>();

        for (VideoGenerationRequest request : requests) {
            long startTime = System.currentTimeMillis();
            try {
                boolean success = multimediaGenerationService.generateVideoFromImageAndAudio(
                    request.getImagePath(),
                    request.getAudioPath(),
                    request.getOutputPath()
                );
                long generationTime = System.currentTimeMillis() - startTime;
                results.add(new VideoGenerationResponse(
                    success,
                    request.getOutputPath(),
                    success ? "Success" : "Failed",
                    generationTime,
                    request.getVideoName()
                ));
            } catch (Exception e) {
                long generationTime = System.currentTimeMillis() - startTime;
                results.add(new VideoGenerationResponse(
                    false,
                    null,
                    "Error: " + e.getMessage(),
                    generationTime,
                    request.getVideoName()
                ));
            }
        }

        response.put("total_videos", requests.size());
        response.put("successful", results.stream().filter(VideoGenerationResponse::isSuccess).count());
        response.put("failed", requests.size() - results.stream().filter(VideoGenerationResponse::isSuccess).count());
        response.put("results", results);
        return ResponseEntity.ok(response);
    }
}
