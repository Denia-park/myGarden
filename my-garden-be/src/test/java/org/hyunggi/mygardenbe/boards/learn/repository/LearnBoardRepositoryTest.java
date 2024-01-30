package org.hyunggi.mygardenbe.boards.learn.repository;

import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
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
class LearnBoardRepositoryTest extends IntegrationTestSupport {
    @Autowired
    LearnBoardRepository learnBoardRepository;

    @Test
    @DisplayName("해당 날짜에 해당하는 TIL을 조회할 수 있다.")
    void findAllByWrittenAtBetween() {
        //given
        learnBoardRepository.save(buildLearnBoardWith(LocalDateTime.of(2024, 1, 25, 12, 0, 0)));
        learnBoardRepository.save(buildLearnBoardWith(LocalDateTime.of(2024, 1, 27, 0, 0, 0)));
        learnBoardRepository.save(buildLearnBoardWith(LocalDateTime.of(2024, 1, 27, 12, 0, 0)));

        final LocalDateTime writtenAtStart = LocalDate.of(2024, 1, 26).atStartOfDay();
        final LocalDateTime writtenAtEnd = LocalDate.of(2024, 1, 28).atStartOfDay();

        //when
        final var learnBoardEntities = learnBoardRepository.findAllInDateRange(writtenAtStart, writtenAtEnd, PageRequest.of(0, 10));

        //then
        assertThat(learnBoardEntities).hasSize(2)
                .extracting("writtenAt")
                .extracting("year", "monthValue", "dayOfMonth", "hour", "minute", "second")
                .containsExactlyInAnyOrder(
                        tuple(2024, 1, 27, 0, 0, 0),
                        tuple(2024, 1, 27, 12, 0, 0)
                );
    }

    private LearnBoardEntity buildLearnBoardWith(final LocalDateTime writtenAt) {
        return LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                writtenAt,
                1L
        );
    }

    @Test
    @DisplayName("해당 날짜에 해당하면서 카테고리가 같은 TIL을 조회할 수 있다.")
    void findAllByWrittenAtBetweenAndCategory() {
        //given
        learnBoardRepository.save(buildLearnBoardWith("title", "content", "category1"));
        learnBoardRepository.save(buildLearnBoardWith("title", "content", "category2"));

        final LocalDateTime writtenAtStart = LocalDate.of(2024, 1, 26).atStartOfDay();
        final LocalDateTime writtenAtEnd = LocalDate.of(2024, 1, 28).atStartOfDay();

        //when
        final var learnBoardEntities = learnBoardRepository.findAllInDateRangeByCategory(writtenAtStart, writtenAtEnd, "category1", PageRequest.of(0, 10));

        //then
        assertThat(learnBoardEntities).hasSize(1)
                .extracting("category")
                .containsExactly("category1");
    }

    private LearnBoardEntity buildLearnBoardWith(final String title, final String content, final String category) {
        return LearnBoardEntity.of(
                title,
                content,
                category,
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                1L
        );
    }

    @Test
    @DisplayName("해당 날짜에 해당하면서 제목 또는 내용에 검색어가 포함된 TIL을 조회할 수 있다.")
    void findAllByWrittenAtBetweenAndTitleContainingOrContentContaining() {
        //given
        learnBoardRepository.save(buildLearnBoardWith("title11", "content", "category"));
        learnBoardRepository.save(buildLearnBoardWith("title22", "content22", "category"));
        learnBoardRepository.save(buildLearnBoardWith("title", "content11", "category"));

        final LocalDateTime writtenAtStart = LocalDate.of(2024, 1, 26).atStartOfDay();
        final LocalDateTime writtenAtEnd = LocalDate.of(2024, 1, 28).atStartOfDay();

        final String searchTest = "11";

        //when
        final var learnBoardEntities = learnBoardRepository.findAllInDateRangeWithTextSearch(writtenAtStart, writtenAtEnd, searchTest, PageRequest.of(0, 10));

        //then
        assertThat(learnBoardEntities).hasSize(2)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("title11", "content"),
                        tuple("title", "content11")
                );
    }

    @Test
    @DisplayName("해당 날짜에 해당하면서 카테고리가 같고 제목 또는 내용에 검색어가 포함된 TIL을 조회할 수 있다.")
    void findAllByWrittenAtBetweenAndCategoryAndTitleContainingOrContentContaining() {
        //given
        learnBoardRepository.save(buildLearnBoardWith("title11", "content", "category1"));
        learnBoardRepository.save(buildLearnBoardWith("title11", "content11", "category2"));
        learnBoardRepository.save(buildLearnBoardWith("title", "content11", "category1"));

        final LocalDateTime writtenAtStart = LocalDate.of(2024, 1, 26).atStartOfDay();
        final LocalDateTime writtenAtEnd = LocalDate.of(2024, 1, 28).atStartOfDay();

        final String searchTest = "11";

        //when
        final var learnBoardEntities = learnBoardRepository.findAllInDateRangeByCategoryWithTextSearch(writtenAtStart, writtenAtEnd, "category1", searchTest, PageRequest.of(0, 10));

        //then
        assertThat(learnBoardEntities).hasSize(2)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("title11", "content"),
                        tuple("title", "content11")
                );
    }
}
