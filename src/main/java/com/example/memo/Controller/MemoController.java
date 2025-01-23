package com.example.memo.Controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/memos")
public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto dto) {
        Long id = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        Memo memo = Memo.builder()
                .id(id)
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        memoList.put(id, memo);

        return new MemoResponseDto(memo);
    }

    @GetMapping("/{id}")
    public MemoResponseDto findMemoById(@PathVariable Long id) {
        System.out.println(id);
        Memo memo = memoList.get(id);
        return new MemoResponseDto(memo);
    }

    @PutMapping("/{id}")
    public MemoResponseDto updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto dto) {
        Memo memo = memoList.get(id);
        memo.updateMemo(dto);
        return new MemoResponseDto(memo);
    }

    @DeleteMapping("/{id}")
    public String deleteMemo(@PathVariable Long id) {
        memoList.remove(id);
        return "memo[" + id + "] deleted";
    }

}
