package com.example.animenarrrator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "cultural_texts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CulturalText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;
    
    @Column(name = "source")
    private String source;
}
