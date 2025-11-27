package com.capstone.ai_model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class stat_index_mapping {

    @Id
    private Integer statId;
    private Integer embeddingIndex;

}
