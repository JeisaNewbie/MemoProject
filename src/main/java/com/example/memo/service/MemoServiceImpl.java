package com.example.memo.service;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MemoServiceImpl implements MemoService {
    private final MemoRepository memoRepository;

    public MemoServiceImpl(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    @Override
    public MemoResponseDto saveMemo(MemoRequestDto dto) {
        Memo memo = Memo.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        return memoRepository.saveMemo(memo);
    }

    @Override
    public List<MemoResponseDto> findAllMemos() {
        return memoRepository.findAllMemos();
    }

    @Override
    public MemoResponseDto findMemoById(Long id){

        Memo memo = memoRepository.findMemoByIdOrOrElseThrow(id);

        return new MemoResponseDto(memo);
    }

    @Transactional
    @Override
    public MemoResponseDto updateMemo(Long id, String title, String content) {

        if (title == null || content == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title or content is null");
        }

        long updated = memoRepository.updateMemo(id, title, content);

        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        Memo memoById = memoRepository.findMemoByIdOrOrElseThrow(updated);

        return new MemoResponseDto(memoById);
    }

    @Transactional
    @Override
    public MemoResponseDto updateTitle(Long id, String title, String content) {

        if (title == null || content != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is null of content is not null");
        }

        long updatedRow = memoRepository.updateTitle(id, title);

        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        Memo memoById = memoRepository.findMemoByIdOrOrElseThrow(updatedRow);

        return new MemoResponseDto(memoById);
    }

    @Override
    public void deleteMemo(Long id) {

        int deletedRow = memoRepository.deleteMemo(id);

        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
    }
}
