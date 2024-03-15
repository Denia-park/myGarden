package org.hyunggi.mygardenbe.boards.comment.service;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.comment.repository.CommentRepository;
import org.hyunggi.mygardenbe.boards.comment.service.response.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 댓글 Service
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    /**
     * 댓글 Entity Repository
     */
    private final CommentRepository commentRepository;

    /**
     * 댓글 목록 조회
     *
     * @param boardType 게시판 종류
     * @param boardId   게시판 ID
     * @return 댓글 목록
     */
    public List<CommentResponse> getComments(final String boardType, final Long boardId) {
        return commentRepository.findAllByBoardTypeAndBoardId(boardType, boardId).stream()
                .map(CommentResponse::of)
                .toList();
    }
}
