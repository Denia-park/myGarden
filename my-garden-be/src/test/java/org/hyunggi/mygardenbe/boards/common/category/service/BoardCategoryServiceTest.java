package org.hyunggi.mygardenbe.boards.common.category.service;

import jakarta.persistence.EntityNotFoundException;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;
import org.hyunggi.mygardenbe.boards.common.category.repository.BoardCategoryRepository;
import org.hyunggi.mygardenbe.boards.common.category.response.BoardCategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    @DisplayName("공지사항 카테고리 목록을 조회한다.")
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

    @Test
    @DisplayName("해당 하는 카테고리가 존재하지 않으면 예외를 발생시킨다.")
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
