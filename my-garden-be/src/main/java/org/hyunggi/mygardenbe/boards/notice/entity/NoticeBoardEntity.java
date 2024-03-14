package org.hyunggi.mygardenbe.boards.notice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * 공지사항 게시판 Entity
 */
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class NoticeBoardEntity extends BaseEntity {
    /**
     * 게시글 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 제목
     */
    @Column(nullable = false, length = 100)
    private String title;

    /**
     * 내용
     */
    @Column(nullable = false, length = 4000)
    private String content;

    /**
     * 분류
     */
    @Column(nullable = false, length = 20)
    private String category;

    /**
     * 중요 여부
     */
    @Column(nullable = false, length = 10)
    private Boolean isImportant;

    /**
     * 조회수
     */
    @Column(nullable = false)
    private Integer views;

    /**
     * 작성자
     */
    @Column(nullable = false, length = 30)
    private String writer;

    /**
     * 작성일
     */
    @Column(nullable = false, length = 15)
    private LocalDateTime writtenAt;

    /**
     * 작성자 ID
     */
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

    /**
     * 게시글 작성
     *
     * @param title    제목
     * @param content  내용
     * @param category 분류
     * @param writer   작성자
     * @param memberId 작성자 ID
     * @return 게시글 Entity
     */
    public static NoticeBoardEntity of(final String title, final String content, final String category, final String writer, final Long memberId) {
        return of(title, content, category, false, writer, LocalDateTime.now(), memberId);
    }

    /**
     * 게시글 작성
     *
     * @param title       제목
     * @param content     내용
     * @param category    분류
     * @param isImportant 중요 여부
     * @param writer      작성자
     * @param writtenAt   작성일
     * @param memberId    작성자 ID
     * @return 게시글 Entity
     */
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

    /**
     * 게시글 작성의 인자 검증
     *
     * @param title       제목
     * @param content     내용
     * @param category    분류
     * @param isImportant 중요 여부
     * @param writer      작성자
     * @param writtenAt   작성일
     * @param memberId    작성자 ID
     */
    private static void validateConstructor(final String title, final String content, final String category, final Boolean isImportant, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        validateTitle(title);
        validateContent(content);
        validateCategory(category);
        validateIsImportant(isImportant);
        validateWriter(writer);
        validateWrittenAt(writtenAt);
        validateMemberId(memberId);
    }

    /**
     * 제목 검증
     *
     * @param title 제목
     */
    private static void validateTitle(final String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    /**
     * 내용 검증
     *
     * @param content 내용
     */
    private static void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    /**
     * 분류 검증
     *
     * @param category 분류
     */
    private static void validateCategory(final String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("분류는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    /**
     * 중요 여부 검증
     *
     * @param isImportant 중요 여부
     */
    private static void validateIsImportant(final Boolean isImportant) {
        if (isImportant == null) {
            throw new IllegalArgumentException("알림글 여부는 null이 될 수 없습니다.");
        }
    }

    /**
     * 작성자 검증
     *
     * @param writer 작성자
     */
    private static void validateWriter(final String writer) {
        if (writer == null || writer.isBlank()) {
            throw new IllegalArgumentException("작성자는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    /**
     * 작성일 검증
     *
     * @param writtenAt 작성일
     */
    private static void validateWrittenAt(final LocalDateTime writtenAt) {
        if (writtenAt == null) {
            throw new IllegalArgumentException("작성일은 null이 될 수 없습니다.");
        }
    }

    /**
     * 작성자 ID 검증
     *
     * @param memberId 작성자 ID
     */
    private static void validateMemberId(final Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("멤버 아이디는 null이 될 수 없고 0보다 커야 합니다.");
        }
    }

    /**
     * 중요 게시글로 설정
     */
    public void setImportant() {
        this.isImportant = true;
    }

    /**
     * 조회수 증가
     */
    public void increaseViewCount() {
        this.views++;
    }

    /**
     * 게시글 수정
     *
     * @param title     제목
     * @param content   내용
     * @param category  분류
     * @param important 중요 여부
     */
    public void update(final String title, final String content, final String category, final Boolean important) {
        validateUpdate(title, content, category, important);

        this.title = title;
        this.content = content;
        this.category = category;
        this.isImportant = important;
    }

    /**
     * 게시글 수정의 인자 검증
     *
     * @param title     제목
     * @param content   내용
     * @param category  분류
     * @param important 중요 여부
     */
    private void validateUpdate(final String title, final String content, final String category, final Boolean important) {
        validateTitle(title);
        validateContent(content);
        validateCategory(category);
        validateIsImportant(important);
    }

    /**
     * 해당 게시글의 작성자인지 여부 확인
     *
     * @param memberId 멤버 아이디
     * @return 작성자 여부 확인
     */
    public boolean isWriter(final Long memberId) {
        return this.memberId.equals(memberId);
    }
}
