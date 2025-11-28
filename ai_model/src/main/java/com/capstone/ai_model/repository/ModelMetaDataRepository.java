package com.capstone.ai_model.repository;

import com.capstone.ai_model.domain.ModelMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelMetaDataRepository extends JpaRepository<ModelMetaData, Long> {
}
