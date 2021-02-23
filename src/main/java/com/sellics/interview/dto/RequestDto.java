package com.sellics.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private String searchWord;
    private String alias;
    private String mid;

    public RequestDto cloneRequestDto(final String prefix) {
        return RequestDto.builder().searchWord(prefix)
                .mid(this.mid)
                .alias(this.alias)
                .build();
    }
}