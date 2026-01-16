package com.example.animenarrrator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.animenarrrator.repository.CulturalTextRepository;
import com.example.animenarrrator.model.CulturalText;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * RAG Service for managing cultural text retrieval and loading.
 * Integrates with CulturalTextRepository for persistence and VectorStoreRagService for retrieval.
 */
@Service
public class RagService {

    private final CulturalTextRepository culturalTextRepository;
    private final VectorStoreRagService vectorStoreRagService;

    @Autowired
    public RagService(CulturalTextRepository culturalTextRepository,
                     VectorStoreRagService vectorStoreRagService) {
        this.culturalTextRepository = culturalTextRepository;
        this.vectorStoreRagService = vectorStoreRagService;
    }

    /**
     * Load Sundarakanda training data from file into the database and into the vector store.
     * Returns number of chunks added to the vector store.
     */
    public int loadSundarakandaTrainingData() throws Exception {
        String filePath = "src/main/resources/cultural-texts/sundarakanda.txt";
        String sundarakandaContent = Files.readString(Paths.get(filePath));

        CulturalText sundarakanda = new CulturalText();
        sundarakanda.setTitle("Sundarakanda");
        sundarakanda.setContent(sundarakandaContent);
        sundarakanda.setSource("Ramayana");

        culturalTextRepository.save(sundarakanda);

        // Load into in-memory vector store and return number of chunks added
        return vectorStoreRagService.loadSundarakandaFromText(sundarakandaContent);
    }

    /**
     * Retrieve cultural text by title.
     */
    public String retrieveContext(String query) {
        CulturalText text = culturalTextRepository.findByTitle(query);
        return text != null ? text.getContent() : "";
    }

    /**
     * Retrieve cultural text (legacy method).
     */
    public String retrieveCulturalText() {
        CulturalText text = culturalTextRepository.findByTitle("Sundarakanda");
        return text != null ? text.getContent() : "No cultural text found";
    }

    /**
     * Get all chunks from the vector store (for debugging/info).
     */
    public Integer getVectorStoreChunkCount() {
        return vectorStoreRagService.getChunkCount();
    }

    /**
     * Search the vector store for chunks matching a query.
     */
    public String searchVectorStore(String query) {
        var chunks = vectorStoreRagService.retrieveRelevantChunks(query, 3);
        return vectorStoreRagService.combineChunksAsContext(chunks);
    }
}
