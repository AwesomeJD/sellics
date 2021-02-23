package com.sellics.interview.service;

import com.sellics.interview.dto.SuggestionsDto;
import com.sellics.interview.estimators.EstimateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class EstimateHandlerService {
    private static final Logger LOGGER = LogManager.getLogger(EstimateHandlerService.class);

    @Resource
    private List<EstimateHandler> estimateHandlers;

    /**
     * Iterate over all the handlers.
     * Each handler has its priority, logic to check the word in the suggestions and the weightage.
     * Any handler that returns the calculated weightage more than 0, the loop breaks.
     *
     * @param suggestionsDtoList
     * @return
     */
    public int getEstimateFromFurtherHandlers(final List<SuggestionsDto> suggestionsDtoList) {
        LOGGER.info("The fully loaded suggestions list, {}.", suggestionsDtoList);
        for (EstimateHandler estimateHandler : estimateHandlers) {
            final int estimate = estimateHandler.handle(suggestionsDtoList);
            if (estimate > 0) {
                LOGGER.info("The keyword found is via the handler {}", estimateHandler.getClass());
                return estimate;
            }
        }
        return 0;
    }
}
