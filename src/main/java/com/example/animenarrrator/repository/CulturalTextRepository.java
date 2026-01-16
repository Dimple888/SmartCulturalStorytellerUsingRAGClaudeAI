package com.example.animenarrrator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.animenarrrator.model.CulturalText;

@Repository
public interface CulturalTextRepository extends JpaRepository<CulturalText, Long> {
    CulturalText findByTitle(String title);
}