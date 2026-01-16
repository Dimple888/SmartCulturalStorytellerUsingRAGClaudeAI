package com.example.animenarrrator.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a complete story with multiple scenes.
 * This is the final output returned to the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryResponse {
    private String title;
    private String storyId;
    private String culturalSource;
    private String emotion;
    private List<Scene> scenes;
}
