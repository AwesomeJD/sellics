package com.sellics.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionsDto {
    private float weight;
    private String prefix;
    private List<String> suggestions;
    private String searchKeyWord;
}
