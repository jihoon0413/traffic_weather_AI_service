package com.capstone.ai_model.repository;

import com.capstone.ai_model.dto.FeaturedCongestionData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeaturedDataRepository extends JpaRepository<FeaturedCongestionData, Long> {
}
