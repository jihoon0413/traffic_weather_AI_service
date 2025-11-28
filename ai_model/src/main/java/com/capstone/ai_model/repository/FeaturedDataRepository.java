package com.capstone.ai_model.repository;

import com.capstone.ai_model.domain.FeaturedCongestionData;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturedDataRepository extends JpaRepository<FeaturedCongestionData, Long> {

    @Query("""
            SELECT d FROM FeaturedCongestionData d
            WHERE d.stat_idx = :statId
            ORDER BY d.record_date DESC
            """)
    List<FeaturedCongestionData> findRecentSequence(@Param("statId") Long statId,  Pageable pageable);

}
