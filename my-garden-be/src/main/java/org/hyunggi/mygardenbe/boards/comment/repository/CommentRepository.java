package org.hyunggi.mygardenbe.boards.comment.repository;

import org.hyunggi.mygardenbe.boards.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 댓글 Repository
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    /**
     * 게시판 종류와 게시판 ID로 댓글 목록 조회
     *
     * @param boardType 게시판 종류
     * @param boardId   게시판 ID
     * @return 댓글 목록
     */
    List<CommentEntity> findAllByBoardTypeAndBoardId(final String boardType, final Long boardId);
}
