package org.hyunggi.mygardenbe.boards.learn.entity;

import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LearnBoardEntityTest {
    @Test
    @DisplayName("of() 메서드를 통해 LearnEntity 객체를 생성할 수 있다.")
    void of() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(title, content, boardCategoryEntity, writer, writtenAt, memberId);

        // then
        assertThat(learnBoardEntity.getTitle()).isEqualTo(title);
        assertThat(learnBoardEntity.getContent()).isEqualTo(content);
        assertThat(learnBoardEntity.getCategory()).isEqualTo(boardCategoryEntity);
        assertThat(learnBoardEntity.getWriter()).isEqualTo(writer);
        assertThat(learnBoardEntity.getWrittenAt()).isEqualTo(writtenAt);
        assertThat(learnBoardEntity.getMemberId()).isEqualTo(memberId);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("title이 null 혹은 빈 문자열이면 IllegalArgumentException이 발생한다.")
    void titleIsNull(final String title) {
        // given
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> LearnBoardEntity.of(title, content, boardCategoryEntity, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("제목은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("content가 null 혹은 빈 문자열이면 IllegalArgumentException이 발생한다.")
    void contentIsNull(final String content) {
        // given
        final String title = "title";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> LearnBoardEntity.of(title, content, boardCategoryEntity, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("내용은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("writer가 null 혹은 빈 문자열이면 IllegalArgumentException이 발생한다.")
    void writerIsNull(final String writer) {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> LearnBoardEntity.of(title, content, boardCategoryEntity, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("작성자는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @Test
    @DisplayName("writtenAt이 null이면 IllegalArgumentException이 발생한다.")
    void writtenAtIsNull() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");
        final String writer = "writer";
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> LearnBoardEntity.of(title, content, boardCategoryEntity, writer, null, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("작성일은 null이 될 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"0", "-1", ","})
    @DisplayName("memberId가 null 혹은 0이하이면, IllegalArgumentException이 발생한다.")
    void memberIdIsNull(final Long memberId) {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();

        // when, then
        assertThatThrownBy(() -> LearnBoardEntity.of(title, content, boardCategoryEntity, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("멤버 아이디는 null이 될 수 없고 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("increaseViewCount() 메서드를 통해 viewCount를 1 증가시킬 수 있다.")
    void increaseViewCount() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(title, content, boardCategoryEntity, writer, writtenAt, memberId);

        // when
        learnBoardEntity.increaseViewCount();

        // then
        assertThat(learnBoardEntity.getViews()).isEqualTo(1);
    }

    @Test
    @DisplayName("update() 메서드를 통해 LearnEntity 객체의 필드를 업데이트할 수 있다.")
    void update() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(title, content, boardCategoryEntity, writer, writtenAt, memberId);

        final String updatedTitle = "updatedTitle";
        final String updatedContent = "updatedContent";
        final BoardCategoryEntity newBoardCategoryEntity = new BoardCategoryEntity("updatedCategory", "신규 카테고리", "learn");

        // when
        learnBoardEntity.update(updatedTitle, updatedContent, newBoardCategoryEntity);

        // then
        assertThat(learnBoardEntity.getTitle()).isEqualTo(updatedTitle);
        assertThat(learnBoardEntity.getContent()).isEqualTo(updatedContent);
        assertThat(learnBoardEntity.getCategory()).isEqualTo(newBoardCategoryEntity);
    }

    @Test
    @DisplayName("isWriter() 메서드를 통해 작성자와 멤버가 같은 memberId를 가지는지 확인할 수 있다.")
    void isWriter() {
        // given
        final Long memberId = 1L;
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");

        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of("title", "content", boardCategoryEntity, "writer", LocalDateTime.now(), memberId);

        // when, then
        assertThat(learnBoardEntity.isWriter(memberId)).isTrue();
    }

    @Test
    @DisplayName("getCategoryCode() 메서드를 통해 카테고리 코드를 가져올 수 있다.")
    void getCategoryCode() {
        // given
        final String categoryCode = "category";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity(categoryCode, "카테고리", "learn");

        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of("title", "content", boardCategoryEntity, "writer", LocalDateTime.now(), 1L);

        // when, then
        assertThat(learnBoardEntity.getCategoryCode()).isEqualTo(categoryCode);
    }
}
