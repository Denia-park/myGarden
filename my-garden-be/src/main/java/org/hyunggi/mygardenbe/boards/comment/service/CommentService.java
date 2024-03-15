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
    public Long postComment(final String boardType, final Long boardId, final String content, final MemberEntity member) {
        validateBoardType(boardType);

        CommentEntity comment = CommentEntity.of(
                content,
                boardType,
                boardId,
                extractEmailId(member.getEmail()),
                LocalDateTime.now(),
                member.getId()
        );

        return commentRepository.save(comment).getId();
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
}
