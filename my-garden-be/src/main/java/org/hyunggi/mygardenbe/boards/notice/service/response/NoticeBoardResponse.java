package org.hyunggi.mygardenbe.boards.notice.service.response;

import lombok.Builder;
import lombok.Getter;
import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;

import static java.time.format.DateTimeFormatter.ofPattern;

@Getter
public class NoticeBoardResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String category;
    private final Boolean isImportant;
    private final Integer views;
    private final String writer;
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

    public static NoticeBoardResponse of(final NoticeBoardEntity noticeBoardEntity) {
        return NoticeBoardResponse.builder()
                .id(noticeBoardEntity.getId())
                .title(noticeBoardEntity.getTitle())
                .content(noticeBoardEntity.getContent())
                .category(noticeBoardEntity.getCategory())
                .isImportant(noticeBoardEntity.getIsImportant())
                .views(noticeBoardEntity.getViews())
                .writer(noticeBoardEntity.getWriter())
                .writtenAt(noticeBoardEntity.getWrittenAt().format(ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
