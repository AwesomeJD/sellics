package com.sellics.interview.service;

import com.sellics.interview.dto.RequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EstimateServiceTest {

    @Autowired
    private EstimateService estimateService;

    @Test
    public void testEstimateSuccess() {
        //given
        final String searchKeyWord = "iphone";
        //when
        int searchVolume = estimateService.estimate(RequestDto.builder().searchWord(searchKeyWord).build());
        //then
        Assertions.assertEquals(searchVolume, 60);
    }
}