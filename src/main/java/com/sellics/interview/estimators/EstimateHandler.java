package com.sellics.interview.estimators;

import com.sellics.interview.dto.SuggestionsDto;

import java.util.List;
import java.util.function.Predicate;

public interface EstimateHandler {
    /**
     * This will have the logic of finding out the keyword is present
     * the suggestions retrieved from amazon api based on teh various prefixes.
     * @param suggestionsDtoList
     * @return
     */
    Integer handle(final List<SuggestionsDto> suggestionsDtoList);

    default Integer calculatedEstimate(final List<SuggestionsDto> suggestionsDtos, final Predicate<SuggestionsDto> predicate, final Float weight) {
        int estimate = 0;
        for (SuggestionsDto suggestionsDto : suggestionsDtos) {
            if (predicate.test(suggestionsDto)) {
                estimate = (int) (suggestionsDto.getWeight() * 100 * weight);
                break;
            }
        }
        return estimate;
    }
}
