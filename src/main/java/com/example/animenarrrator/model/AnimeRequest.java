package com.example.animenarrrator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request object for generating a cultural story with anime narration.
 * Contains user preferences and story requirements.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimeRequest {
    private String storyId;
    private String storyName;
    private String userQuery;
    private String userPreferences;
    private String style;
    private Integer maxScenes;
    private String language;
}
