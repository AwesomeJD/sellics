package com.sellics.interview.estimators;

import com.sellics.interview.dto.SuggestionsDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(3)
@Component
public class ContainsMatchAllEstimateHandler implements EstimateHandler {
    private static final Logger LOGGER = LogManager.getLogger(ContainsMatchAllEstimateHandler.class);

    @Value(value = "${search-word.weight.contains-match}")
    private Float containsMatchWeight;

    @Override
    public Integer handle(final List<SuggestionsDto> suggestionsDtos) {
        LOGGER.info("Inside the handler {}", this.getClass());
        return calculatedEstimate(suggestionsDtos, this::test, containsMatchWeight );
    }

    /**
     * Iterate all the suggestions and return true if all of the suggestion contains the search keyword.
     * @param suggestionsDto
     * @return
     */
    private boolean test(final SuggestionsDto suggestionsDto) {
        return suggestionsDto.getSuggestions().stream().allMatch(suggestion -> suggestion.contains(suggestionsDto.getSearchKeyWord()));
    }
}
