package org.hyunggi.mygardenbe.boards.comment.service;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.comment.entity.CommentEntity;
import org.hyunggi.mygardenbe.boards.comment.repository.CommentRepository;
import org.hyunggi.mygardenbe.boards.comment.service.response.CommentResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * 허용되는 게시판 종류
     */
    private final List<String> permittedBoardTypes = List.of("learn");

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

    /**
     * 게시판의 댓글을 등록한다.
     */
    public Long postComment(final String boardType, final Long boardId, final String comment, final MemberEntity member) {
        validatePostRequest(boardType, boardId, comment);

        CommentEntity commentEntity = CommentEntity.of(
                comment,
                boardType,
                boardId,
                extractEmailId(member.getEmail()),
                LocalDateTime.now(),
                member.getId()
        );

        return commentRepository.save(commentEntity).getId();
    }

    /**
     * 댓글 등록 요청을 검증한다.
     *
     * @param boardType 게시판 종류
     * @param boardId   게시판 ID
     * @param comment   댓글 내용
     */
    private void validatePostRequest(final String boardType, final Long boardId, final String comment) {
        validateBoardType(boardType);
        validateBoardId(boardId);
        validateContent(comment);
    }

    /**
     * 게시판 ID를 검증한다.
     *
     * @param boardId 게시판 ID
     */
    private void validateBoardId(final Long boardId) {
        if (boardId == null || boardId <= 0) {
            throw new IllegalArgumentException("게시판 ID는 0보다 커야 합니다.");
        }
    }

    /**
     * 댓글 내용을 검증한다.
     *
     * @param content 댓글 내용
     */
    private void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
        }

        if (content.length() > 300) {
            throw new IllegalArgumentException("댓글 내용은 300자를 넘을 수 없습니다.");
        }
    }

    /**
     * 게시판 종류를 검증한다.
     *
     * @param boardType 게시판 종류
     */
    private void validateBoardType(final String boardType) {
        if (!permittedBoardTypes.contains(boardType)) {
            throw new IllegalArgumentException("허용되지 않은 게시판 종류입니다.");
        }
    }

    /**
     * 이메일에서 ID를 추출한다.
     *
     * @param email 이메일
     * @return 이메일 ID
     */
    private String extractEmailId(final String email) {
        return email.split("@")[0];
    }

    /**
     * 게시판의 댓글을 삭제한다.
     *
     * @param boardType 게시판 종류
     * @param boardId   게시판 ID
     * @param commentId 댓글 ID
     */
    public Long deleteComment(final String boardType, final Long boardId, final Long commentId, final MemberEntity member) {
        validateDeleteRequest(boardType, boardId, commentId);
        validateCommentOwner(commentId, member.getId());

        commentRepository.deleteById(commentId);

        return commentId;
    }

    /**
     * 댓글 삭제 요청을 검증한다.
     *
     * @param boardType 게시판 종류
     * @param boardId   게시판 ID
     * @param commentId 댓글 ID
     */
    private void validateDeleteRequest(final String boardType, final Long boardId, final Long commentId) {
        validateBoardType(boardType);
        validateBoardId(boardId);
        validateCommentId(commentId);
    }

    /**
     * 댓글 ID를 검증한다.
     *
     * @param commentId 댓글 ID
     */
    private void validateCommentId(final Long commentId) {
        if (commentId == null || commentId <= 0) {
            throw new IllegalArgumentException("댓글 ID는 0보다 커야 합니다.");
        }
    }

    /**
     * 댓글 작성자를 검증한다.
     *
     * @param commentId 댓글 ID
     * @param id        회원 ID
     */
    private void validateCommentOwner(final Long commentId, final Long id) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!commentEntity.getMemberId().equals(id)) {
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
        }
    }
}
