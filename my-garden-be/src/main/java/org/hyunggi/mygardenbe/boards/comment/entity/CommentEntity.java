package org.hyunggi.mygardenbe.boards.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * 댓글 Entity
 */
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class CommentEntity extends BaseEntity {
    /**
     * 댓글 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 내용
     */
    @Column(nullable = false, length = 300)
    private String content;

    /**
     * 게시판 종류
     */
    @Column(nullable = false, length = 20)
    private String boardType;

    /**
     * 게시판 ID
     */
    @Column(nullable = false)
    private Long boardId;

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

    private CommentEntity(final String content, final String boardType, final Long boardId, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        this.content = content;
        this.boardType = boardType;
        this.boardId = boardId;
        this.writer = writer;
        this.writtenAt = writtenAt;
        this.memberId = memberId;
    }

    /**
     * 댓글 생성
     *
     * @param content   내용
     * @param boardType 게시판 종류
     * @param boardId   게시판 ID
     * @param writer    작성자
     * @param writtenAt 작성일
     * @param memberId  작성자 ID
     * @return 댓글
     */
    public static CommentEntity of(final String content, final String boardType, final Long boardId, final String writer, final LocalDateTime writtenAt, final Long memberId) {
        return new CommentEntity(content, boardType, boardId, writer, writtenAt, memberId);
    }
}
