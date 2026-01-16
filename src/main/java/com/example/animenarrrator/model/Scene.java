package com.example.animenarrrator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a single scene in a story.
 * Each scene contains narration, emotion, and prompts for anime generation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scene {
    private Integer sceneNumber;
    private String narration;
    private String emotion;
    private String imagePrompt;
    private String audioText;
    private String characters;
    private String location;
}
