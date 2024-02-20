package org.hyunggi.mygardenbe.boards.common.category.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.category.repository.BoardCategoryRepository;
import org.hyunggi.mygardenbe.boards.common.category.response.BoardCategoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardCategoryService {
    private final BoardCategoryRepository boardCategoryRepository;

    public List<BoardCategoryResponse> getCategories(final String boardType) {
        validateBoardType(boardType);
        return boardCategoryRepository.findAllByBoardType(boardType).stream()
                .map(BoardCategoryResponse::of)
                .toList();
    }

    private void validateBoardType(final String boardType) {
        Assert.hasText(boardType, "게시판 타입은 비어있을 수 없습니다.");
    }

    public void validateCategoryWithBoardType(final String category, final String boardType) {
        validateCategory(category);
        validateBoardType(boardType);
        
        boardCategoryRepository.findByCodeAndBoardType(category, boardType)
                .orElseThrow(() -> new EntityNotFoundException("해당 분류가 존재하지 않습니다."));
    }

    private void validateCategory(final String category) {
        Assert.hasText(category, "분류는 비어있을 수 없습니다.");
    }
}
