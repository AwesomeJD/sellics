package com.sellics.interview.estimators;

import com.sellics.interview.dto.SuggestionsDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(1)
@Component
public class PrefixMatchAllEstimateHandler implements EstimateHandler {
    private static final Logger LOGGER = LogManager.getLogger(PrefixMatchAllEstimateHandler.class);

    @Value(value = "${search-word.weight.prefix-match-all}")
    private Float prefixMatchAllWeight;

    @Override
    public Integer handle(final List<SuggestionsDto> suggestionsDtos) {
        LOGGER.info("Inside the handler {}", this.getClass());
        return calculatedEstimate(suggestionsDtos, this::test, prefixMatchAllWeight);
    }

    /**
     * Iterate all the suggestions and return true if all the suggestions has the search keyword as prefix.
     *
     * @param suggestionsDto
     * @return
     */
    private boolean test(final SuggestionsDto suggestionsDto) {
        return suggestionsDto.getSuggestions().stream().allMatch(suggestion -> suggestion.startsWith(suggestionsDto.getSearchKeyWord()));
    }
}
