package org.hyunggi.mygardenbe.boards.learn.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class LearnBoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 4000)
    private String content;
    @Column(nullable = false, length = 20)
    private String category;
    @Column(nullable = false)
    private Integer views;
    @Column(nullable = false, length = 30)
    private String writer;
    @Column(nullable = false, length = 15)
    private LocalDateTime writtenAt;
    @Column(nullable = false)
    private Long memberId;

    @Builder(access = lombok.AccessLevel.PRIVATE)
    private LearnBoardEntity(final String title, final String content, final String category, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.writer = writer;
        this.writtenAt = writtenAt;
        this.memberId = memberId;

        this.views = 0;
    }

    public static LearnBoardEntity of(final String title, final String content, final String category, final String writer, final Long memberId) {
        return of(title, content, category, writer, LocalDateTime.now(), memberId);
    }

    public static LearnBoardEntity of(final String title, final String content, final String category, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        validateConstructor(title, content, category, writer, writtenAt, memberId);

        return LearnBoardEntity.builder()
                .title(title)
                .content(content)
                .category(category)
                .writer(writer)
                .writtenAt(writtenAt)
                .memberId(memberId)
                .build();
    }

    private static void validateConstructor(final String title, final String content, final String category, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        validateTitle(title);
        validateContent(content);
        validateCategory(category);
        validateWriter(writer);
        validateWrittenAt(writtenAt);
        validateMemberId(memberId);
    }

    private static void validateTitle(final String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    private static void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    private static void validateCategory(final String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("분류는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    private static void validateWriter(final String writer) {
        if (writer == null || writer.isBlank()) {
            throw new IllegalArgumentException("작성자는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    private static void validateWrittenAt(final LocalDateTime writtenAt) {
        if (writtenAt == null) {
            throw new IllegalArgumentException("작성일은 null이 될 수 없습니다.");
        }
    }

    private static void validateMemberId(final Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("멤버 아이디는 null이 될 수 없고 0보다 커야 합니다.");
        }
    }

    public void increaseViewCount() {
        this.views++;
    }

    public void update(final String title, final String content, final String category) {
        validateUpdate(title, content, category);

        this.title = title;
        this.content = content;
        this.category = category;
    }

    private void validateUpdate(final String title, final String content, final String category) {
        validateTitle(title);
        validateContent(content);
        validateCategory(category);
    }

    public boolean isWriter(final Long memberId) {
        return this.memberId.equals(memberId);
    }
}
