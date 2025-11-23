package com.capstone.ai_model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrainTestSplit<T> {
    private List<T> train;
    private List<T> test;
}
