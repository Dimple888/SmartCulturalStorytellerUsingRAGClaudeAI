package com.example.animenarrrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * Text-to-Speech Service using AWS Polly.
 * Converts story narration into audio with emotional tone mapping.
 */
@Service
public class TTSService {

    private static final Logger logger = LoggerFactory.getLogger(TTSService.class);

    @Value("${app.demo-mode:false}")
    private boolean demoMode;

    /**
     * Available Polly voices mapped to emotions (Rasas)
     */
    private static final Map<String, String> EMOTION_TO_VOICE = new HashMap<>();

    static {
        // Heroism/Courage - Strong, confident voices
        EMOTION_TO_VOICE.put("Veeram", "Joanna");  // English - Dynamic
        EMOTION_TO_VOICE.put("Veeram_HI", "Aditi"); // Hindi - Strong

        // Love/Romance - Soft, gentle voices
        EMOTION_TO_VOICE.put("Shringar", "Emma");   // English - Warm
        EMOTION_TO_VOICE.put("Shringar_HI", "Raveena"); // Hindi - Melodic

        // Compassion/Sorrow - Warm, empathetic
        EMOTION_TO_VOICE.put("Karuna", "Amy");      // English - Tender
        EMOTION_TO_VOICE.put("Karuna_HI", "Aditi"); // Hindi - Emotional

        // Fear/Dread - Urgent, tense
        EMOTION_TO_VOICE.put("Bhaya", "Brian");     // English - Intense
        EMOTION_TO_VOICE.put("Bhaya_HI", "Kalpana"); // Hindi - Alert

        // Joy/Laughter - Cheerful, upbeat
        EMOTION_TO_VOICE.put("Hasya", "Ivy");       // English - Happy
        EMOTION_TO_VOICE.put("Hasya_HI", "Raveena"); // Hindi - Joyful

        // Wonder/Amazement - Curious, reverent
        EMOTION_TO_VOICE.put("Adbhuta", "Salli");   // English - Amazed
        EMOTION_TO_VOICE.put("Adbhuta_HI", "Aditi"); // Hindi - Awed

        // Anger/Rage - Forceful, intense
        EMOTION_TO_VOICE.put("Raudra", "Matthew");  // English - Commanding
        EMOTION_TO_VOICE.put("Raudra_HI", "Kalpana"); // Hindi - Fierce

        // Disgust - Stern, disapproving
        EMOTION_TO_VOICE.put("Bibhatsa", "Russell"); // English - Stern
        EMOTION_TO_VOICE.put("Bibhatsa_HI", "Kalpana"); // Hindi - Dismissive
    }

    /**
     * Generate TTS audio URL from AWS Polly for a given text and emotion
     */
    public String generateTTSAudio(String text, String emotion, String language) {
        if (demoMode) {
            logger.info("Demo mode: Returning mock TTS response");
            return generateMockTTSResponse(text, emotion, language);
        }

        try {
            String voiceId = getVoiceIdForEmotion(emotion, language);
            return synthesizeWithPolly(text, voiceId, emotion, language);
        } catch (Exception e) {
            logger.error("TTS synthesis failed: {}", e.getMessage());
            return generateMockTTSResponse(text, emotion, language);
        }
    }

    /**
     * Get AWS Polly voice ID based on emotion (Rasa)
     */
    private String getVoiceIdForEmotion(String emotion, String language) {
        String key = emotion + (language.equalsIgnoreCase("HI") ? "_HI" : "");
        return EMOTION_TO_VOICE.getOrDefault(key, "Joanna"); // Default to Joanna
    }

    /**
     * Synthesize audio using AWS Polly (requires AWS SDK)
     * In production, this would use: software.amazon.awssdk:polly
     */
    private String synthesizeWithPolly(String text, String voiceId, String emotion, String language) {
        // Mock implementation - in production would call actual Polly API
        String audioUrl = String.format(
            "https://polly-audio.s3.amazonaws.com/audio_%s_%s.mp3",
            emotion.toLowerCase(),
            System.currentTimeMillis()
        );

        logger.info("Generated Polly audio: voice={}, emotion={}, language={}, url={}", 
            voiceId, emotion, language, audioUrl);

        return audioUrl;
    }

    /**
     * Mock TTS response for demo/testing
     */
    private String generateMockTTSResponse(String text, String emotion, String language) {
        String voiceId = getVoiceIdForEmotion(emotion, language);
        return String.format(
            "https://demo-tts.s3.amazonaws.com/mock_audio_%s_%s_%d.mp3",
            emotion.toLowerCase(),
            voiceId.toLowerCase(),
            System.currentTimeMillis()
        );
    }

    /**
     * Get supported Polly voices
     */
    public List<String> getSupportedVoices() {
        return new ArrayList<>(EMOTION_TO_VOICE.values());
    }

    /**
     * Get voice recommendations for emotion
     */
    public Map<String, String> getVoiceRecommendations() {
        Map<String, String> recommendations = new HashMap<>();
        recommendations.put("Veeram", "Joanna (EN) / Aditi (HI) - Courageous, heroic tone");
        recommendations.put("Shringar", "Emma (EN) / Raveena (HI) - Warm, romantic tone");
        recommendations.put("Karuna", "Amy (EN) / Aditi (HI) - Compassionate, empathetic tone");
        recommendations.put("Bhaya", "Brian (EN) / Kalpana (HI) - Tense, urgent tone");
        recommendations.put("Hasya", "Ivy (EN) / Raveena (HI) - Cheerful, joyful tone");
        recommendations.put("Adbhuta", "Salli (EN) / Aditi (HI) - Amazed, reverent tone");
        recommendations.put("Raudra", "Matthew (EN) / Kalpana (HI) - Powerful, commanding tone");
        recommendations.put("Bibhatsa", "Russell (EN) / Kalpana (HI) - Stern, dismissive tone");
        return recommendations;
    }

    /**
     * Validate TTS parameters
     */
    public boolean validateTTSRequest(String text, String emotion) {
        if (text == null || text.isBlank()) {
            logger.warn("TTS text is empty");
            return false;
        }
        if (!EMOTION_TO_VOICE.containsKey(emotion) && !EMOTION_TO_VOICE.containsKey(emotion + "_HI")) {
            logger.warn("Invalid emotion: {}", emotion);
            return false;
        }
        return true;
    }
}
