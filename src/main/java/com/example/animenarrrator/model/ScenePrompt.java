package com.example.animenarrrator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a single scene prompt for anime generation.
 * Contains detailed instructions for visual and audio generation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScenePrompt {
    private Integer sceneNumber;
    private String narration;
    private String emotion;
    private String imagePrompt;
    private String audioText;
    private String characters;
    private String location;
    private String style;

    public String getSceneDescription() {
        return narration;
    }

    public String getVisualElements() {
        return imagePrompt;
    }
}
