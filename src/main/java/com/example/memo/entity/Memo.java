package com.example.memo.entity;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class Memo {
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
