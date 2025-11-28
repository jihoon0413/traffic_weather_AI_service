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
public class StatIndexMapping {

    @Id
    private Integer statId;
    private Integer embeddingIndex;

}
