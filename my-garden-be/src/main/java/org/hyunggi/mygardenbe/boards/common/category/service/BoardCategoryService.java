package org.hyunggi.mygardenbe.boards.common.category.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.category.repository.BoardCategoryRepository;
import org.hyunggi.mygardenbe.boards.common.category.service.response.BoardCategoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 게시판 분류 Service
 */
@RequiredArgsConstructor
@Service
public class BoardCategoryService {
    /**
     * 게시판 분류 Entity Repository
     */
    private final BoardCategoryRepository boardCategoryRepository;

    /**
     * 게시판 분류 목록 조회
     *
     * @param boardType 게시판 타입
     * @return 게시판 분류 목록
     */
    public List<BoardCategoryResponse> getCategories(final String boardType) {
        validateBoardType(boardType);
        return boardCategoryRepository.findAllByBoardType(boardType).stream()
                .map(BoardCategoryResponse::of)
                .toList();
    }

    /**
     * 게시판 타입 검증
     *
     * @param boardType 게시판 타입
     */
    private void validateBoardType(final String boardType) {
        Assert.hasText(boardType, "게시판 타입은 비어있을 수 없습니다.");
    }

    /**
     * 분류와 게시판 타입 검증
     *
     * @param category  분류
     * @param boardType 게시판 타입
     */
    public void validateCategoryWithBoardType(final String category, final String boardType) {
        validateCategory(category);
        validateBoardType(boardType);

        boardCategoryRepository.findByCodeAndBoardType(category, boardType)
                .orElseThrow(() -> new EntityNotFoundException("해당 분류가 존재하지 않습니다."));
    }

    /**
     * 분류 검증
     *
     * @param category 분류
     */
    private void validateCategory(final String category) {
        Assert.hasText(category, "분류는 비어있을 수 없습니다.");
    }
}
