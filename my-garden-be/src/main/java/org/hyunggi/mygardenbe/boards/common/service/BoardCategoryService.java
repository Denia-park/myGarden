package org.hyunggi.mygardenbe.boards.common.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.repository.BoardCategoryRepository;
import org.hyunggi.mygardenbe.boards.common.response.BoardCategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardCategoryService {
    private final BoardCategoryRepository boardCategoryRepository;

    public List<BoardCategoryResponse> getCategories(final String boardType) {
        return boardCategoryRepository.findAllByBoardType(boardType).stream()
                .map(BoardCategoryResponse::of)
                .toList();
    }

    public void validateCategoryWithBoardType(final String category, final String boardType) {
        boardCategoryRepository.findByCodeAndBoardType(category, boardType)
                .orElseThrow(() -> new EntityNotFoundException("해당 분류가 존재하지 않습니다."));
    }
}
