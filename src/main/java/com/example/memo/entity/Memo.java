package com.example.memo.entity;

import com.example.memo.dto.MemoRequestDto;
import lombok.*;

@Builder
@Getter
public class Memo {
    @Setter
    private Long id;
    private String title;
    private String content;

    public void updateMemo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
