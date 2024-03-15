package org.hyunggi.mygardenbe.boards.common.category.service;

import jakarta.persistence.EntityNotFoundException;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;
import org.hyunggi.mygardenbe.boards.common.category.repository.BoardCategoryRepository;
import org.hyunggi.mygardenbe.boards.common.category.service.response.BoardCategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@Transactional
class BoardCategoryServiceTest extends IntegrationTestSupport {
    @Autowired
    private BoardCategoryService boardCategoryService;
    @Autowired
    private BoardCategoryRepository boardCategoryRepository;

    @Test
    @DisplayName("공지사항 분류 목록을 조회한다.")
    void getCategories() {
        //given
        BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("project", "프로젝트", "notice");

        boardCategoryRepository.save(boardCategoryEntity);

        //when
        final List<BoardCategoryResponse> categories = boardCategoryService.getCategories("notice");

        //then
        assertThat(categories).hasSize(1)
                .extracting("code", "text")
                .containsExactly(tuple("project", "프로젝트"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("분류 종류를 가져올 때, 해당 하는 게시판 타입이 존재하지 않으면 예외를 발생시킨다.")
    void getCategoriesWithNullBoardType(final String boardType) {
        //when, then
        assertThatThrownBy(() -> boardCategoryService.getCategories(boardType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시판 타입은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("분류를 검증할 때, 해당 하는 게시판 타입이 Null이거나 빈 문자열이면 예외를 발생시킨다.")
    void validateBoardType(final String boardType) {
        //given
        BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("project", "프로젝트", "notice");

        boardCategoryRepository.save(boardCategoryEntity);

        //when, then
        assertThatThrownBy(() -> boardCategoryService.validateCategoryWithBoardType("study", boardType))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시판 타입은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("해당 하는 분류가 Null이거나 빈 문자열이면 예외를 발생시킨다.")
    void validateCategory(final String category) {
        //given
        BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("project", "프로젝트", "notice");

        boardCategoryRepository.save(boardCategoryEntity);

        //when, then
        assertThatThrownBy(() -> boardCategoryService.validateCategoryWithBoardType(category, "notice"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("분류는 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("해당 하는 분류가 존재하지 않으면 예외를 발생시킨다.")
    void validateCategoryWithBoardType() {
        //given
        BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("project", "프로젝트", "notice");

        boardCategoryRepository.save(boardCategoryEntity);

        //when, then
        assertThatThrownBy(() -> boardCategoryService.validateCategoryWithBoardType("study", "notice"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 분류가 존재하지 않습니다.");
    }
}
