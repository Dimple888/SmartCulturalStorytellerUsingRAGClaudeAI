package com.example.animenarrrator.service;

import com.example.animenarrrator.model.AnimeRequest;
import com.example.animenarrrator.model.StoryNarration;
import com.example.animenarrrator.repository.CulturalTextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for generating story narration from cultural content using RAG and Claude AI.
 * Orchestrates the RAG retrieval and LLM generation pipeline.
 */
@Service
public class StoryGenerationService {

    private final CulturalTextRepository culturalTextRepository;
    private final VectorStoreRagService vectorStoreRagService;
    private final ClaudeAiService claudeAiService;
    private final PromptTemplateService promptTemplateService;

    @Autowired
    public StoryGenerationService(CulturalTextRepository culturalTextRepository,
                                   VectorStoreRagService vectorStoreRagService,
                                   ClaudeAiService claudeAiService,
                                   PromptTemplateService promptTemplateService) {
        this.culturalTextRepository = culturalTextRepository;
        this.vectorStoreRagService = vectorStoreRagService;
        this.claudeAiService = claudeAiService;
        this.promptTemplateService = promptTemplateService;
    }

    /**
     * Generate story narration from an AnimeRequest using RAG and Claude Haiku.
     * 
     * Process:
     * 1. Retrieve relevant cultural chunks using RAG
     * 2. Combine chunks into coherent context
     * 3. Generate story using Claude Haiku
     * 4. Return formatted StoryNarration
     */
    public StoryNarration generateStoryNarration(AnimeRequest request) {
        // Step 1: Retrieve relevant cultural chunks from vector store
        String userQuery = request.getUserQuery() != null ? 
            request.getUserQuery() : request.getUserPreferences();
        
        var relevantChunks = vectorStoreRagService.retrieveRelevantChunks(userQuery, 5);
        
        // Step 2: Combine chunks into context
        String culturalContext = vectorStoreRagService.combineChunksAsContext(relevantChunks);
        
        // Step 3: Build prompt for story generation
        String storyPrompt = promptTemplateService.getStoryGenerationPrompt(
            culturalContext, 
            userQuery
        );
        
        // Step 4: Call Claude Haiku to generate story
        String generatedStory = claudeAiService.generateStory(storyPrompt);
        
        // Step 5: Create and return StoryNarration object
        StoryNarration narration = new StoryNarration();
        narration.setTitle(request.getStoryName() != null ? 
            request.getStoryName() : "Cultural Story Narration");
        narration.setContent(generatedStory);
        narration.setAuthor("Claude Haiku - Smart Cultural Storyteller");
        
        return narration;
    }

    /**
     * Alternative method to generate story from cultural text ID (if stored in DB).
     */
    public StoryNarration generateStory(String culturalTextId) {
        // Retrieve from database
        var culturalText = culturalTextRepository.findById(Long.parseLong(culturalTextId))
            .orElseThrow(() -> new IllegalArgumentException("Cultural text not found"));
        
        String storyContent = createStoryFromCulturalText(culturalText.getContent());
        return new StoryNarration(culturalText.getTitle(), storyContent, "Claude Haiku");
    }

    /**
     * Create story from cultural text content.
     */
    private String createStoryFromCulturalText(String culturalText) {
        // This would normally be done via LLM
        return "Generated story based on cultural text:\n\n" + culturalText;
    }
}
