package org.hyunggi.mygardenbe.boards.notice.entity;

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
        final String category = "category";
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when
        NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(title, content, category, isImportant, writer, writtenAt, memberId);

        // then
        assertThat(noticeBoardEntity.getTitle()).isEqualTo(title);
        assertThat(noticeBoardEntity.getContent()).isEqualTo(content);
        assertThat(noticeBoardEntity.getCategory()).isEqualTo(category);
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
        final String category = "category";
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, category, isImportant, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("제목은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("content가 null 혹은 빈 문자열이면 IllegalArgumentException이 발생한다.")
    void contentIsNull(final String content) {
        // given
        final String title = "title";
        final String category = "category";
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, category, isImportant, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("내용은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("category가 null 혹은 빈 문자열이면 IllegalArgumentException이 발생한다.")
    void categoryIsNull(final String category) {
        // given
        final String title = "title";
        final String content = "content";
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, category, isImportant, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @Test
    @DisplayName("isImportant가 null이면 IllegalArgumentException이 발생한다.")
    void isImportantIsNull() {
        // given
        final String title = "title";
        final String content = "content";
        final String category = "category";
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, category, null, writer, writtenAt, memberId))
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
        final String category = "category";
        final Boolean isImportant = false;
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, category, isImportant, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("작성자는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
    }

    @Test
    @DisplayName("writtenAt이 null이면 IllegalArgumentException이 발생한다.")
    void writtenAtIsNull() {
        // given
        final String title = "title";
        final String content = "content";
        final String category = "category";
        final Boolean isImportant = false;
        final String writer = "writer";
        final Long memberId = 1L;

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, category, isImportant, writer, null, memberId))
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
        final String category = "category";
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();

        // when, then
        assertThatThrownBy(() -> NoticeBoardEntity.of(title, content, category, isImportant, writer, writtenAt, memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("멤버 아이디는 null이 될 수 없고 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("setImportant() 메서드를 통해 isImportant를 true로 변경할 수 있다.")
    void setImportant() {
        // given
        final String title = "title";
        final String content = "content";
        final String category = "category";
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;
        NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(title, content, category, isImportant, writer, writtenAt, memberId);

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
        final String category = "category";
        final Boolean isImportant = false;
        final String writer = "writer";
        final LocalDateTime writtenAt = LocalDateTime.now();
        final Long memberId = 1L;
        NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(title, content, category, isImportant, writer, writtenAt, memberId);

        // when
        noticeBoardEntity.increaseViewCount();

        // then
        assertThat(noticeBoardEntity.getViews()).isEqualTo(1);
    }
}
