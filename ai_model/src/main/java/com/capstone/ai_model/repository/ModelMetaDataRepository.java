package com.capstone.ai_model.repository;

import com.capstone.ai_model.domain.ModelMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelMetaDataRepository extends JpaRepository<ModelMetaData, Long> {
}
