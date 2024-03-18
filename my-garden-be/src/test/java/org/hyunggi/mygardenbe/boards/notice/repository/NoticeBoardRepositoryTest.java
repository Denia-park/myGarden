package org.hyunggi.mygardenbe.boards.notice.repository;

import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;
import org.hyunggi.mygardenbe.boards.common.category.repository.BoardCategoryRepository;
import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class NoticeBoardRepositoryTest extends IntegrationTestSupport {
    @Autowired
    NoticeBoardRepository noticeBoardRepository;
    @Autowired
    BoardCategoryRepository boardCategoryRepository;

    @Test
    @DisplayName("중요 공지사항을 조회할 수 있다.")
    void findAllByIsImportant() {
        //given
        noticeBoardRepository.save(buildNoticeImportantBoardWith("title1", "content1", 10, false));
        noticeBoardRepository.save(buildNoticeImportantBoardWith("title2", "content2", 11, true));
        noticeBoardRepository.save(buildNoticeImportantBoardWith("title3", "content3", 12, true));

        //when
        final var noticeBoardEntities = noticeBoardRepository.findAllByIsImportantOrderByWrittenAtDesc(true);

        //then
        assertThat(noticeBoardEntities).hasSize(2)
                .extracting("title", "content", "isImportant")
                .containsExactly(
                        tuple("title3", "content3", true),
                        tuple("title2", "content2", true)
                );
    }

    private NoticeBoardEntity buildNoticeImportantBoardWith(final String title, final String content, final int day, final boolean isImportant) {
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        boardCategoryRepository.save(boardCategoryEntity);

        return NoticeBoardEntity.of(
                title,
                content,
                boardCategoryEntity,
                isImportant,
                "writer",
                LocalDateTime.of(2024, 3, day, 12, 0, 0),
                1L
        );
    }

    @Test
    @DisplayName("해당 날짜에 해당하는 공지사항을 조회할 수 있다.")
    void searchNoticeBoards_with_writtenAt() {
        //given
        noticeBoardRepository.save(buildNoticeBoardWith(LocalDateTime.of(2024, 1, 25, 12, 0, 0)));
        noticeBoardRepository.save(buildNoticeBoardWith(LocalDateTime.of(2024, 1, 27, 0, 0, 0)));
        noticeBoardRepository.save(buildNoticeBoardWith(LocalDateTime.of(2024, 1, 27, 12, 0, 0)));

        final LocalDateTime writtenAtStart = LocalDate.of(2024, 1, 26).atStartOfDay();
        final LocalDateTime writtenAtEnd = LocalDate.of(2024, 1, 28).atStartOfDay();

        //when
        final var noticeBoardEntities = noticeBoardRepository.searchNoticeBoards(writtenAtStart, writtenAtEnd, "", "", PageRequest.of(0, 10));

        //then
        assertThat(noticeBoardEntities).hasSize(2)
                .extracting("writtenAt")
                .extracting("year", "monthValue", "dayOfMonth", "hour", "minute", "second")
                .containsExactlyInAnyOrder(
                        tuple(2024, 1, 27, 0, 0, 0),
                        tuple(2024, 1, 27, 12, 0, 0)
                );
    }

    private NoticeBoardEntity buildNoticeBoardWith(final LocalDateTime writtenAt) {
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        boardCategoryRepository.save(boardCategoryEntity);

        return NoticeBoardEntity.of(
                "title",
                "content",
                boardCategoryEntity,
                false,
                "writer",
                writtenAt,
                1L
        );
    }

    @Test
    @DisplayName("해당 날짜에 해당하면서 분류가 같은 공지사항을 조회할 수 있다.")
    void searchNoticeBoards_with_writtenAt_and_category() {
        //given
        noticeBoardRepository.save(buildNoticeBoardWith("title", "content", "category1"));
        noticeBoardRepository.save(buildNoticeBoardWith("title", "content", "category2"));

        final LocalDateTime writtenAtStart = LocalDate.of(2024, 1, 26).atStartOfDay();
        final LocalDateTime writtenAtEnd = LocalDate.of(2024, 1, 28).atStartOfDay();

        //when
        final var noticeBoardEntities = noticeBoardRepository.searchNoticeBoards(writtenAtStart, writtenAtEnd, "category1", "", PageRequest.of(0, 10));

        //then
        assertThat(noticeBoardEntities).hasSize(1)
                .extracting("category.code")
                .containsExactly("category1");
    }

    private NoticeBoardEntity buildNoticeBoardWith(final String title, final String content, final String category) {
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity(category, "카테고리", "notice");
        boardCategoryRepository.save(boardCategoryEntity);

        return NoticeBoardEntity.of(
                title,
                content,
                boardCategoryEntity,
                false,
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                1L
        );
    }

    @Test
    @DisplayName("해당 날짜에 해당하면서 제목 또는 내용에 검색어가 포함된 공지사항을 조회할 수 있다.")
    void searchNoticeBoards_with_writtenAt_and_searchText() {
        //given
        noticeBoardRepository.save(buildNoticeBoardWith("title11", "content", "category"));
        noticeBoardRepository.save(buildNoticeBoardWith("title22", "content22", "category"));
        noticeBoardRepository.save(buildNoticeBoardWith("title", "content11", "category"));

        final LocalDateTime writtenAtStart = LocalDate.of(2024, 1, 26).atStartOfDay();
        final LocalDateTime writtenAtEnd = LocalDate.of(2024, 1, 28).atStartOfDay();

        final String searchText = "11";

        //when
        final var noticeBoardEntities = noticeBoardRepository.searchNoticeBoards(writtenAtStart, writtenAtEnd, "", searchText, PageRequest.of(0, 10));

        //then
        assertThat(noticeBoardEntities).hasSize(2)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("title11", "content"),
                        tuple("title", "content11")
                );
    }

    @Test
    @DisplayName("해당 날짜에 해당하면서 분류가 같고 제목 또는 내용에 검색어가 포함된 공지사항을 조회할 수 있다.")
    void searchNoticeBoards_with_writtenAt_and_category_and_searchText() {
        //given
        noticeBoardRepository.save(buildNoticeBoardWith("title11", "content", "category1"));
        noticeBoardRepository.save(buildNoticeBoardWith("title11", "content11", "category2"));
        noticeBoardRepository.save(buildNoticeBoardWith("title", "content11", "category1"));

        final LocalDateTime writtenAtStart = LocalDate.of(2024, 1, 26).atStartOfDay();
        final LocalDateTime writtenAtEnd = LocalDate.of(2024, 1, 28).atStartOfDay();

        final String searchText = "11";

        //when
        final var noticeBoardEntities = noticeBoardRepository.searchNoticeBoards(writtenAtStart, writtenAtEnd, "category1", searchText, PageRequest.of(0, 10));

        //then
        assertThat(noticeBoardEntities).hasSize(2)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("title11", "content"),
                        tuple("title", "content11")
                );
    }
}
