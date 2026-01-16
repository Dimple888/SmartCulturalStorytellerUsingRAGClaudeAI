package com.example.animenarrrator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a chunk of cultural text with metadata for RAG.
 * Stores story elements with emotional and contextual information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryChunk {
    private String id;
    private String verse;
    private String meaning;
    private String emotion;
    private String characters;
    private String sceneIntent;
    private String location;
}
