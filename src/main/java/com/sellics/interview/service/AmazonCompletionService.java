package com.sellics.interview.service;

import com.sellics.interview.constants.ApplicationConstants;
import com.sellics.interview.constants.ErrorConstants;
import com.sellics.interview.dto.RequestDto;
import com.sellics.interview.exception.ApplicationException;
import com.sellics.interview.model.amazon.response.AmazonCompletionResponse;
import com.sellics.interview.model.amazon.response.Suggestion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AmazonCompletionService {

    private static final Logger LOGGER = LogManager.getLogger(AmazonCompletionResponse.class);
    @Value(value = "${service.amazon.api.endpoint}")
    private String endPoint;

    @Value(value = "${service.amazon.api.marketId}")
    private String marketId;

    @Value(value = "${service.amazon.api.alias}")
    private String alias;

    @Autowired
    private RestTemplate restTemplate;

    public List<String> getSuggestionsFromAmazon(final RequestDto requestDto) {

        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endPoint)
                .queryParam(ApplicationConstants.QUERY_PARAM_MARKET_ID, Optional.ofNullable(requestDto.getMid()).orElse(marketId))
                .queryParam(ApplicationConstants.QUERY_PARAM_ALIAS, Optional.ofNullable(requestDto.getAlias()).orElse(alias))
                .queryParam(ApplicationConstants.QUERY_PARAM_PREFIX, requestDto.getSearchWord());
        final String uri = builder.toUriString();
        LOGGER.info(uri, () -> "The URI formed is {}");

        ResponseEntity<AmazonCompletionResponse> response = null;
        try {
            response = restTemplate.exchange(
                    new RequestEntity<>(HttpMethod.GET, builder.build().toUri()),
                    AmazonCompletionResponse.class);
        } catch (Exception exception) {
            throw new ApplicationException(ErrorConstants.AMAZON_API_ERROR_CODE, ErrorConstants.AMAZON_API_ERROR_MESSAGE, exception);
        }

        return Objects.requireNonNull(response.getBody()).getSuggestions().stream().map(Suggestion::getValue).collect(Collectors.toList());
    }

}
