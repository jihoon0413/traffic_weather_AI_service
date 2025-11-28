package com.capstone.ai_model.repository;

import com.capstone.ai_model.domain.StatIndexMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatMappingRepository extends JpaRepository<StatIndexMapping, Integer> {
}
