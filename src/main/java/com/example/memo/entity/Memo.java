package com.example.memo.entity;

import com.example.memo.dto.MemoRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Memo {
    private Long id;
    private String title;
    private String content;

    public void updateMemo(MemoRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }
}
