package org.hyunggi.mygardenbe.boards.comment.service.response;

import lombok.Builder;
import org.hyunggi.mygardenbe.boards.comment.entity.CommentEntity;

import java.time.LocalDateTime;

/**
 * 댓글 응답
 *
 * @param id        댓글 ID
 * @param content   내용
 * @param boardType 게시판 종류
 * @param boardId   게시판 ID
 * @param writer    작성자
 * @param writtenAt 작성일
 * @param memberId  작성자 ID
 */
@Builder
public record CommentResponse(
        Long id,
        String content,
        String boardType,
        Long boardId,
        String writer,
        LocalDateTime writtenAt,
        Long memberId
) {
    /**
     * 댓글 응답 생성
     *
     * @param commentEntity 댓글 Entity
     * @return 댓글 응답
     */
    public static CommentResponse of(final CommentEntity commentEntity) {
        return CommentResponse.builder()
                .id(commentEntity.getId())
                .content(commentEntity.getContent())
                .boardType(commentEntity.getBoardType())
                .boardId(commentEntity.getBoardId())
                .writer(commentEntity.getWriter())
                .writtenAt(commentEntity.getWrittenAt())
                .memberId(commentEntity.getMemberId())
                .build();
    }
}
