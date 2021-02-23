package com.sellics.interview.estimators;

import com.sellics.interview.dto.SuggestionsDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(2)
@Component
public class PrefixMatchAnyEstimateHandler implements EstimateHandler {
    private static final Logger LOGGER = LogManager.getLogger(PrefixMatchAnyEstimateHandler.class);

    @Value(value = "${search-word.weight.prefix-match-any}")
    private Float prefixMatchAnyWeight;

    @Override
    public Integer handle(final List<SuggestionsDto> suggestionsDtos) {
        LOGGER.info("Inside the handler {}", this.getClass());
        return calculatedEstimate(suggestionsDtos, this::test, prefixMatchAnyWeight);
    }

    /**
     * Iterate all the suggestions and return true if any of the suggestions has the search keyword as prefix.
     *
     * @param suggestionsDto
     * @return
     */
    public boolean test(SuggestionsDto suggestionsDto) {
        return suggestionsDto.getSuggestions().stream().anyMatch(suggestion -> suggestion.startsWith(suggestionsDto.getSearchKeyWord()));
    }

}
