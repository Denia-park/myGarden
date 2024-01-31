package org.hyunggi.mygardenbe.boards.learn.service.response;

import lombok.Builder;
import lombok.Getter;
import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;

import static java.time.format.DateTimeFormatter.ofPattern;

@Getter
public class LearnBoardResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String category;
    private final Integer views;
    private final String writer;
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

    public static LearnBoardResponse of(final LearnBoardEntity noticeBoardEntity) {
        return LearnBoardResponse.builder()
                .id(noticeBoardEntity.getId())
                .title(noticeBoardEntity.getTitle())
                .content(noticeBoardEntity.getContent())
                .category(noticeBoardEntity.getCategory())
                .views(noticeBoardEntity.getViews())
                .writer(noticeBoardEntity.getWriter())
                .writtenAt(noticeBoardEntity.getWrittenAt().format(ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
