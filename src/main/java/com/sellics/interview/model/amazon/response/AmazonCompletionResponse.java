package com.sellics.interview.model.amazon.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmazonCompletionResponse {
    private String alias;
    private String prefix;
    private List<Suggestion> suggestions;
}
