package org.hyunggi.mygardenbe.boards.learn.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * TIL 게시판 Entity
 */
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class LearnBoardEntity extends BaseEntity {
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "category_id")
    private BoardCategoryEntity category;

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
    private LearnBoardEntity(final String title, final String content, final BoardCategoryEntity category, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.writer = writer;
        this.writtenAt = writtenAt;
        this.memberId = memberId;

        this.views = 0;
    }

    /**
     * TIL 게시글 Entity 생성
     *
     * @param title    제목
     * @param content  내용
     * @param category 분류
     * @param writer   작성자
     * @param memberId 작성자 ID
     * @return TIL 게시글 Entity
     */
    public static LearnBoardEntity of(final String title, final String content, final BoardCategoryEntity category, final String writer, final Long memberId) {
        return of(title, content, category, writer, LocalDateTime.now(), memberId);
    }

    /**
     * TIL 게시글 Entity 생성
     *
     * @param title     제목
     * @param content   내용
     * @param category  분류
     * @param writer    작성자
     * @param writtenAt 작성일
     * @param memberId  작성자 ID
     * @return TIL 게시글 Entity
     */
    public static LearnBoardEntity of(final String title, final String content, final BoardCategoryEntity category, final String writer, final LocalDateTime writtenAt, final Long memberId) {
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

    /**
     * 생성자 유효성 검사
     *
     * @param title     제목
     * @param content   내용
     * @param category  분류
     * @param writer    작성자
     * @param writtenAt 작성일
     * @param memberId  작성자 ID
     */
    private static void validateConstructor(final String title, final String content, final BoardCategoryEntity category, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        validateTitle(title);
        validateContent(content);
        validateCategory(category);
        validateWriter(writer);
        validateWrittenAt(writtenAt);
        validateMemberId(memberId);
    }

    /**
     * 제목 유효성 검사
     *
     * @param title 제목
     */
    private static void validateTitle(final String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    /**
     * 내용 유효성 검사
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
    private static void validateCategory(final BoardCategoryEntity category) {
        if (category == null) {
            throw new IllegalArgumentException("분류는 null이 될 수 없습니다.");
        }
    }

    /**
     * 작성자 유효성 검사
     *
     * @param writer 작성자
     */
    private static void validateWriter(final String writer) {
        if (writer == null || writer.isBlank()) {
            throw new IllegalArgumentException("작성자는 null이 될 수 없고 빈 문자열이 될 수 없습니다.");
        }
    }

    /**
     * 작성일 유효성 검사
     *
     * @param writtenAt 작성일
     */
    private static void validateWrittenAt(final LocalDateTime writtenAt) {
        if (writtenAt == null) {
            throw new IllegalArgumentException("작성일은 null이 될 수 없습니다.");
        }
    }

    /**
     * 작성자 ID 유효성 검사
     *
     * @param memberId 작성자 ID
     */
    private static void validateMemberId(final Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("멤버 아이디는 null이 될 수 없고 0보다 커야 합니다.");
        }
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
     * @param title    제목
     * @param content  내용
     * @param category 분류
     */
    public void update(final String title, final String content, final BoardCategoryEntity category) {
        validateUpdate(title, content, category);

        this.title = title;
        this.content = content;
        this.category = category;
    }

    /**
     * 게시글 수정 유효성 검사
     *
     * @param title    제목
     * @param content  내용
     * @param category 분류
     */
    private void validateUpdate(final String title, final String content, final BoardCategoryEntity category) {
        validateTitle(title);
        validateContent(content);
        validateCategory(category);
    }

    /**
     * 현재 글의 작성자가 맞는지 여부 확인
     *
     * @param memberId 멤버 아이디
     * @return 작성자 여부 확인
     */
    public boolean isWriter(final Long memberId) {
        return this.memberId.equals(memberId);
    }

    /**
     * 분류 코드 반환
     *
     * @return 분류 코드
     */
    public String getCategoryCode() {
        return category.getCode();
    }
}
