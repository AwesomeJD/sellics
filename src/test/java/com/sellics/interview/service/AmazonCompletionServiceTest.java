package com.sellics.interview.service;

import com.sellics.interview.dto.RequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AmazonCompletionServiceTest {

    @Autowired
    private AmazonCompletionService amazonCompletionService;

    @Test
    public void testGetSuggestionsFromAmazonApiSuccess() {
        //given
        final RequestDto requestDto = RequestDto.builder().searchWord("iphone").build();
        //when
        List<String> suggestions = amazonCompletionService.getSuggestionsFromAmazon(requestDto);
        //then
        assertNotNull(suggestions);
        assertEquals(suggestions.size(), 10);
    }
}