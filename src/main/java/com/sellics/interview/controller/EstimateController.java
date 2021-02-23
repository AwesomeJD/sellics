package com.sellics.interview.controller;

import com.sellics.interview.constants.ErrorConstants;
import com.sellics.interview.dto.RequestDto;
import com.sellics.interview.model.response.ApplicationResponse;
import com.sellics.interview.service.EstimateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Validated
@RestController
public class EstimateController {
    private static final Logger LOGGER = LogManager.getLogger(EstimateController.class);

    @Autowired
    private EstimateService estimateService;


    @GetMapping(path = "/estimate")
    public ResponseEntity<ApplicationResponse>  getEstimateForSearchWord(@NotBlank(message = ErrorConstants.KEYWORD_CANNOT_BE_BLANK) @RequestParam final String keyword,
                                                                         @RequestParam(required = false) final String alias,
                                                                         @RequestParam(required = false) final String mid){

        final int score = estimateService.estimate(RequestDto.builder().searchWord(keyword).mid(mid).alias(alias).build());

        return ResponseEntity.ok(ApplicationResponse.builder().keyword(keyword).score(score).build());
    }

}
