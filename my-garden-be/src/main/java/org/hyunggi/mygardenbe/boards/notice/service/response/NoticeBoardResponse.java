package org.hyunggi.mygardenbe.boards.notice.service.response;

import lombok.Builder;
import lombok.Getter;
import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * 공지사항 게시판 응답
 */
@Getter
public class NoticeBoardResponse {
    /**
     * 게시글 ID
     */
    private final Long id;

    /**
     * 제목
     */
    private final String title;

    /**
     * 내용
     */
    private final String content;

    /**
     * 분류
     */
    private final String category;

    /**
     * 중요 여부
     */
    private final Boolean isImportant;

    /**
     * 조회수
     */
    private final Integer views;

    /**
     * 작성자
     */
    private final String writer;

    /**
     * 작성일
     */
    private final String writtenAt;

    @Builder
    private NoticeBoardResponse(final Long id, final String title, final String content, final String category, final Boolean isImportant, final Integer views, final String writer, final String writtenAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.isImportant = isImportant;
        this.views = views;
        this.writer = writer;
        this.writtenAt = writtenAt;
    }

    /**
     * Entity -> Response 변환
     *
     * @param noticeBoardEntity 게시판 Entity
     * @return 게시판 응답
     */
    public static NoticeBoardResponse of(final NoticeBoardEntity noticeBoardEntity) {
        return NoticeBoardResponse.builder()
                .id(noticeBoardEntity.getId())
                .title(noticeBoardEntity.getTitle())
                .content(noticeBoardEntity.getContent())
                .category(noticeBoardEntity.getCategoryCode())
                .isImportant(noticeBoardEntity.getIsImportant())
                .views(noticeBoardEntity.getViews())
                .writer(noticeBoardEntity.getWriter())
                .writtenAt(noticeBoardEntity.getWrittenAt().format(ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
