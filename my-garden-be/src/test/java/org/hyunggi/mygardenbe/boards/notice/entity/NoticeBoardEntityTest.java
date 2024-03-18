package org.hyunggi.mygardenbe.boards.notice.entity;

import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NoticeBoardEntityTest {

    @Test
    @DisplayName("of() 메서드를 통해 NoticeEntity 객체를 생성할 수 있다.")
    void of() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when
        final NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(title, content, boardCategoryEntity, isImportant, writer, writtenAt, memberId);

        // then
        assertThat(noticeBoardEntity.getTitle()).isEqualTo(title);
        assertThat(noticeBoardEntity.getContent()).isEqualTo(content);
        assertThat(noticeBoardEntity.getCategory()).isEqualTo(boardCategoryEntity);
        assertThat(noticeBoardEntity.getIsImportant()).isEqualTo(isImportant);
        assertThat(noticeBoardEntity.getWriter()).isEqualTo(writer);
        assertThat(noticeBoardEntity.getWrittenAt()).isEqualTo(writtenAt);
        assertThat(noticeBoardEntity.getMemberId()).isEqualTo(memberId);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("title이 null 혹은 빈 문자열이면 IllegalArgumentException이 발생한다.")
    void titleIsNull(final String title) {
        // given
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, boardCategoryEntity, isImportant, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("제목은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("content가 null 혹은 빈 문자열이면 IllegalArgumentException이 발생한다.")
    void contentIsNull(final String content) {
        // given
        final String title = "title";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, boardCategoryEntity, isImportant, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("내용은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @Test
    @DisplayName("isImportant가 null이면 IllegalArgumentException이 발생한다.")
    void isImportantIsNull() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, boardCategoryEntity, null, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("알림글 여부는 null이 될 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("writer가 null 혹은 빈 문자열이면 IllegalArgumentException이 발생한다.")
    void writerIsNull(final String writer) {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final Boolean isImportant = false;
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, boardCategoryEntity, isImportant, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("작성자는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @Test
    @DisplayName("writtenAt이 null이면 IllegalArgumentException이 발생한다.")
    void writtenAtIsNull() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final Boolean isImportant = false;
        final String writer = "writer";
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, boardCategoryEntity, isImportant, writer, null, memberId))
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
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, boardCategoryEntity, isImportant, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("멤버 아이디는 null이 될 수 없고 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("setImportant() 메서드를 통해 isImportant를 true로 변경할 수 있다.")
    void setImportant() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;
        final NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(title, content, boardCategoryEntity, isImportant, writer, writtenAt, memberId);

        // when
        noticeBoardEntity.setImportant();

        // then
        assertThat(noticeBoardEntity.getIsImportant()).isTrue();
    }

    @Test
    @DisplayName("increaseViewCount() 메서드를 통해 viewCount를 1 증가시킬 수 있다.")
    void increaseViewCount() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;
        final NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(title, content, boardCategoryEntity, isImportant, writer, writtenAt, memberId);

        // when
        noticeBoardEntity.increaseViewCount();

        // then
        assertThat(noticeBoardEntity.getViews()).isEqualTo(1);
    }

    @Test
    @DisplayName("update() 메서드를 통해 NoticeEntity 객체의 필드를 업데이트할 수 있다.")
    void update() {
        // given
        final String title = "title";
        final String content = "content";
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;
        final NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(title, content, boardCategoryEntity, isImportant, writer, writtenAt, memberId);

        final String updatedTitle = "updatedTitle";
        final String updatedContent = "updatedContent";
        final BoardCategoryEntity newBoardCategoryEntity = new BoardCategoryEntity("updatedCategory", "신규 카테고리", "notice");
        final Boolean updatedIsImportant = true;

        // when
        noticeBoardEntity.update(updatedTitle, updatedContent, newBoardCategoryEntity, updatedIsImportant);

        // then
        assertThat(noticeBoardEntity.getTitle()).isEqualTo(updatedTitle);
        assertThat(noticeBoardEntity.getContent()).isEqualTo(updatedContent);
        assertThat(noticeBoardEntity.getCategory()).isEqualTo(newBoardCategoryEntity);
        assertThat(noticeBoardEntity.getIsImportant()).isEqualTo(updatedIsImportant);
    }

    @Test
    @DisplayName("isWriter() 메서드를 통해 작성자와 멤버가 같은 memberId를 가지는지 확인할 수 있다.")
    void isWriter() {
        // given
        final Long memberId = 1L;
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "notice");

        final NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of("title", "content", boardCategoryEntity, false, "writer", LocalDateTime.now(), memberId);

        // when, then
        assertThat(noticeBoardEntity.isWriter(memberId)).isTrue();
    }
}
