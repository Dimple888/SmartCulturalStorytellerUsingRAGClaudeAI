package com.example.animenarrrator.service;

import com.example.animenarrrator.model.StoryChunk;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Vector Store RAG Service for retrieving cultural story chunks.
 * Implements an in-memory vector database for POC purposes.
 * Can be extended to use Chroma or Pinecone for production.
 */
@Service
public class VectorStoreRagService {
    /**
     * Load StoryChunks from rag-test-data.json in resources/cultural-texts.
     * Returns the number of chunks loaded.
     */
    public int loadTestDataFromJson() {
        try {
            String filePath = "src/main/resources/cultural-texts/rag-test-data.json";
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            if (!java.nio.file.Files.exists(path)) return 0;
            String json = java.nio.file.Files.readString(path);
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.List<java.util.Map<String, Object>> data = mapper.readValue(json, java.util.List.class);
            int count = 0;
            for (java.util.Map<String, Object> entry : data) {
                StoryChunk chunk = new StoryChunk();
                chunk.setId((String) entry.getOrDefault("id", "test_" + count));
                chunk.setVerse((String) entry.getOrDefault("title", ""));
                chunk.setMeaning((String) entry.getOrDefault("content", ""));
                chunk.setEmotion((String) entry.getOrDefault("emotion", "Test"));
                chunk.setCharacters((String) entry.getOrDefault("characters", ""));
                chunk.setSceneIntent((String) entry.getOrDefault("sceneIntent", ""));
                chunk.setLocation((String) entry.getOrDefault("location", "Test Data"));
                addCustomChunk(chunk);
                count++;
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private final Map<String, StoryChunk> vectorStore = new HashMap<>();
    private final PromptTemplateService promptTemplateService;

    public VectorStoreRagService(PromptTemplateService promptTemplateService) {
        this.promptTemplateService = promptTemplateService;
        initializeVectorStore();
    }

    /**
     * Initialize the vector store with sample Sundarakanda chunks.
     */
    private void initializeVectorStore() {
        // Sundarakanda chunks with emotional and contextual metadata
        addChunk("sk_001", 
            "Hanuman stood upon Mount Mahendra, his eyes fixed upon the vast ocean.",
            "Hanuman prepares to undertake an impossible journey across the ocean to find Sita.",
            "Veeram",
            "Hanuman, Mount Mahendra",
            "Preparation and determination",
            "Mount Mahendra");

        addChunk("sk_002",
            "With a mighty roar, Hanuman leaped skyward, his form expanding with divine power.",
            "Hanuman transforms himself and begins his great leap across the ocean, embodying courage and devotion.",
            "Veeram",
            "Hanuman, Ocean",
            "The great leap",
            "The Vast Ocean");

        addChunk("sk_003",
            "The demons of the sky challenged him, but Hanuman's heart remained fixed on Rama.",
            "Despite obstacles, Hanuman's unwavering devotion to Rama keeps him focused on his mission.",
            "Karuna",
            "Hanuman, Demons, Rama",
            "Devotion amid adversity",
            "The celestial sky");

        addChunk("sk_004",
            "At last, Lanka appeared before him, a city of gold and wonder.",
            "Hanuman arrives at Lanka after his great journey, seeing the kingdom of Ravana.",
            "Adbhuta",
            "Hanuman, Lanka",
            "Arrival and discovery",
            "Lanka, the golden city");

        addChunk("sk_005",
            "In the Ashoka garden, Hanuman found Sita, sorrow etched upon her face but spirit unbroken.",
            "Hanuman discovers Sita in captivity, but finds her courage and devotion to Rama intact.",
            "Karuna",
            "Hanuman, Sita, Ravana",
            "Reunion and compassion",
            "Ashoka Vana garden");

        addChunk("sk_006",
            "Hanuman revealed himself and spoke of Rama's love, offering her Rama's signet ring.",
            "Hanuman confirms to Sita that Rama has not forgotten her and rescue is coming.",
            "Shringar",
            "Hanuman, Sita, Rama",
            "Hope and connection",
            "Ashoka Vana garden");

        addChunk("sk_007",
            "With renewed determination, Hanuman wreaked havoc in Lanka's gardens and palaces.",
            "Hanuman demonstrates his power to Ravana, proving that Rama's forces are formidable.",
            "Raudra",
            "Hanuman, Ravana, Demons",
            "Defiance and power",
            "Lanka's palace grounds");

        addChunk("sk_008",
            "Hanuman returned to Rama with news of Sita, his journey complete, his mission fulfilled.",
            "Hanuman successfully completes his mission and returns with important news for Rama.",
            "Hasya",
            "Hanuman, Rama",
            "Victory and fulfillment",
            "Rama's camp");
    }

    /**
     * Add a story chunk to the vector store.
     */
    private void addChunk(String id, String verse, String meaning, String emotion, 
                         String characters, String sceneIntent, String location) {
        StoryChunk chunk = new StoryChunk(id, verse, meaning, emotion, characters, sceneIntent,location);
        chunk.setLocation(location);
        vectorStore.put(id, chunk);
    }

    /**
     * Retrieve relevant story chunks based on a user query.
     * Uses simple keyword matching for POC (can be replaced with actual vector similarity).
     */
    public List<StoryChunk> retrieveRelevantChunks(String query, int topK) {
        String lowerQuery = query.toLowerCase();
        
        return vectorStore.values().stream()
            .map(chunk -> {
                // Simple relevance scoring based on keyword matches
                int score = 0;
                String fullContent = (chunk.getVerse() + " " + chunk.getMeaning() + " " + 
                                     chunk.getCharacters() + " " + chunk.getSceneIntent()).toLowerCase();
                
                // Higher score for matching key emotions
                if (fullContent.contains("hanuman")) score += 10;
                if (fullContent.contains("sita")) score += 10;
                if (fullContent.contains("rama")) score += 8;
                if (fullContent.contains("ocean")) score += 5;
                if (fullContent.contains("devotion")) score += 8;
                if (fullContent.contains("courage")) score += 8;
                
                // Match query keywords
                for (String keyword : lowerQuery.split(" ")) {
                    if (keyword.length() > 2 && fullContent.contains(keyword)) {
                        score += 5;
                    }
                }
                
                return new Object[]{chunk, score};
            })
            .filter(obj -> (int)obj[1] > 0)
            .sorted((a, b) -> Integer.compare((int)b[1], (int)a[1]))
            .limit(topK)
            .map(obj -> (StoryChunk)obj[0])
            .collect(Collectors.toList());
    }

    /**
     * Get all chunks for a specific emotion/Rasa.
     */
    public List<StoryChunk> getChunksByEmotion(String emotion) {
        return vectorStore.values().stream()
            .filter(chunk -> chunk.getEmotion().equals(emotion))
            .collect(Collectors.toList());
    }

    /**
     * Get all chunks mentioning specific characters.
     */
    public List<StoryChunk> getChunksByCharacters(String character) {
        String lowerChar = character.toLowerCase();
        return vectorStore.values().stream()
            .filter(chunk -> chunk.getCharacters().toLowerCase().contains(lowerChar))
            .collect(Collectors.toList());
    }

    /**
     * Combine retrieved chunks into a coherent context for story generation.
     */
    public String combineChunksAsContext(List<StoryChunk> chunks) {
        StringBuilder context = new StringBuilder();
        context.append("CULTURAL STORY CONTEXT:\n\n");
        
        for (int i = 0; i < chunks.size(); i++) {
            StoryChunk chunk = chunks.get(i);
            context.append("Section ").append(i + 1).append(":\n");
            context.append("Verse: ").append(chunk.getVerse()).append("\n");
            context.append("Meaning: ").append(chunk.getMeaning()).append("\n");
            context.append("Emotion: ").append(chunk.getEmotion()).append("\n");
            context.append("Characters: ").append(chunk.getCharacters()).append("\n");
            context.append("Setting: ").append(chunk.getLocation()).append("\n\n");
        }
        
        return context.toString();
    }

    /**
     * Add a new chunk to the vector store (for dynamic loading).
     */
    public void addCustomChunk(StoryChunk chunk) {
        vectorStore.put(chunk.getId(), chunk);
    }

    /**
     * Get total number of chunks in store.
     */
    public int getChunkCount() {
        return vectorStore.size();
    }

    /**
     * Clear the vector store.
     */
    public void clearStore() {
        vectorStore.clear();
    }

    /**
     * Get all chunks.
     */
    public Collection<StoryChunk> getAllChunks() {
        return vectorStore.values();
    }

    /**
     * Load Sundarakanda text (full) and create chunks into the vector store.
     * Splits on blank lines and creates simple chunks for POC RAG retrieval.
     */
    public int loadSundarakandaFromText(String fullText) {
        if (fullText == null || fullText.isBlank()) return 0;

        String[] parts = fullText.split("\\r?\\n\\r?\\n+");
        int count = 0;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.isEmpty()) continue;
            String id = String.format("sundara_%03d", ++count);
            String verse = part.length() > 300 ? part.substring(0, 300) + "..." : part;
            String meaning = "";
            String emotion = "Unknown";
            String characters = "";
            String sceneIntent = "Context chunk from Sundarakanda";
            addChunk(id, verse, meaning, emotion, characters, sceneIntent, "Sundarakanda");
        }

        return count;
    }
}
