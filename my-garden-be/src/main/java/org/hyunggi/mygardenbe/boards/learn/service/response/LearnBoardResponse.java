package org.hyunggi.mygardenbe.boards.learn.service.response;

import lombok.Builder;
import lombok.Getter;
import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * TIL 게시판 응답
 */
@Getter
public class LearnBoardResponse {
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
    private LearnBoardResponse(final Long id, final String title, final String content, final String category, final Integer views, final String writer, final String writtenAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.views = views;
        this.writer = writer;
        this.writtenAt = writtenAt;
    }

    /**
     * Entity -> Response 변환
     *
     * @param learnBoardEntity 게시판 Entity
     * @return 게시판 응답
     */
    public static LearnBoardResponse of(final LearnBoardEntity learnBoardEntity) {
        return LearnBoardResponse.builder()
                .id(learnBoardEntity.getId())
                .title(learnBoardEntity.getTitle())
                .content(learnBoardEntity.getContent())
                .category(learnBoardEntity.getCategoryCode())
                .views(learnBoardEntity.getViews())
                .writer(learnBoardEntity.getWriter())
                .writtenAt(learnBoardEntity.getWrittenAt().format(ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
