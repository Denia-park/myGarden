package org.hyunggi.mygardenbe.boards.notice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class NoticeBoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 4000)
    private String content;
    @Column(nullable = false, length = 20)
    private String category;
    @Column(nullable = false, length = 10)
    private Boolean isImportant;
    @Column(nullable = false)
    private Integer views;
    @Column(nullable = false, length = 30)
    private String writer;
    @Column(nullable = false, length = 15)
    private LocalDateTime writtenAt;
    @Column(nullable = false)
    private Long memberId;

    @Builder(access = lombok.AccessLevel.PRIVATE)
    private NoticeBoardEntity(final String title, final String content, final String category, final Boolean isImportant, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.isImportant = isImportant;
        this.writer = writer;
        this.writtenAt = writtenAt;
        this.memberId = memberId;

        this.views = 0;
    }

    public static NoticeBoardEntity of(final String title, final String content, final String category, final String writer, final Long memberId) {
        return of(title, content, category, false, writer, LocalDateTime.now(), memberId);
    }

    public static NoticeBoardEntity of(final String title, final String content, final String category, final Boolean isImportant, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        validateConstructor(title, content, category, isImportant, writer, writtenAt, memberId);

        return NoticeBoardEntity.builder()
                .title(title)
                .content(content)
                .category(category)
                .isImportant(isImportant)
                .writer(writer)
                .writtenAt(writtenAt)
                .memberId(memberId)
                .build();
    }

    private static void validateConstructor(final String title, final String content, final String category, final Boolean isImportant, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        validateTitle(title);
        validateContent(content);
        validateCategory(category);
        validateIsImportant(isImportant);
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
            throw new IllegalArgumentException("카테고리는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    private static void validateIsImportant(final Boolean isImportant) {
        if (isImportant == null) {
            throw new IllegalArgumentException("알림글 여부는 null이 될 수 없습니다.");
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

    public void setImportant() {
        this.isImportant = true;
    }

    public void increaseViewCount() {
        this.views++;
    }
}
