package com.sellics.interview.service;

import com.sellics.interview.dto.SuggestionsDto;
import com.sellics.interview.dto.RequestDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstimateService {
    private static final Logger LOGGER = LogManager.getLogger(EstimateService.class);

    @Value(value = "${search-word.weight.full-match}")
    private Float fullMatchWeight;

    @Autowired
    private AmazonCompletionService amazonCompletionService;

    @Autowired
    private EstimateHandlerService estimateHandlerService;
    /**
     * This is the main method that does the computation.
     * The amazon completion service is fired for each sub-array of the chars of the search keyword.
     * Ex: for Iphone, the call will be made for each substring, I, Ip, Iph, Ipho, Iphon, Iphone
     *
     * @param requestDto
     * @return
     */
    public int estimate(final RequestDto requestDto) {
        final List<Character> characters = requestDto.getSearchWord().chars().mapToObj(chr -> (char) chr).collect(Collectors.toList());
        final StringBuilder prefixBuilder = new StringBuilder();
        final List<SuggestionsDto> suggestionsDtoList = new ArrayList<>();
        for (Character chr : characters) {
            prefixBuilder.append(chr);
            final String prefix = prefixBuilder.toString();
            final RequestDto requestForApi = requestDto.cloneRequestDto(prefix);
            final List<String> suggestions = amazonCompletionService.getSuggestionsFromAmazon(requestForApi);
            final float weightOfPrefix = getWeightOfPrefix(requestDto, prefix);
            LOGGER.info("The weightOfPrefix for the prefix {} is {}", prefix, weightOfPrefix);

            if (isExactKeyWordMatchInSuggestions(requestDto, suggestions)) {
                LOGGER.info(() -> "The keyword found is an exact match in the suggestions");
                return (int) (weightOfPrefix * 100 * fullMatchWeight);
            }
            suggestionsDtoList.add(SuggestionsDto.builder()
                    .searchKeyWord(requestDto.getSearchWord())
                    .suggestions(suggestions)
                    .weight(weightOfPrefix)
                    .prefix(prefix)
                    .build());
        }
        LOGGER.info("The exact keyword found is not found in the suggestions so going to other handlers.");
        //Since the exact match is not found, so trying other strategies defined.
        return estimateHandlerService.getEstimateFromFurtherHandlers(suggestionsDtoList);
    }

    /**
     * Iterate all the suggestions and return true if any suggestion has the search keyword as the prefix.
     *
     * @param requestDto
     * @param suggestions
     * @return
     */
    private boolean isExactKeyWordMatchInSuggestions(RequestDto requestDto, List<String> suggestions) {
        return suggestions.stream().anyMatch(suggestion -> requestDto.getSearchWord().equalsIgnoreCase(suggestion));
    }

    /**
     * returns the weight of the prefix based on the
     * formula =  ((length of the search keyword + 1) - (length of the prefix))/(length of the search keyword)
     * The one is added because, when the length of the prefix is 1, we need it to be 100%. Because we got a match using a single char itself.
     *
     * @param requestDto
     * @param prefix
     * @return
     */
    private float getWeightOfPrefix(RequestDto requestDto, String prefix) {
        return ((float) requestDto.getSearchWord().length() + 1.0f - (float) prefix.length()) / (float) requestDto.getSearchWord().length();
    }
}
