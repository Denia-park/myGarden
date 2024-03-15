package org.hyunggi.mygardenbe.boards.comment.service;

import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.boards.comment.entity.CommentEntity;
import org.hyunggi.mygardenbe.boards.comment.repository.CommentRepository;
import org.hyunggi.mygardenbe.boards.comment.service.response.CommentResponse;
import org.hyunggi.mygardenbe.member.domain.Member;
import org.hyunggi.mygardenbe.member.domain.Role;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@Transactional
class CommentServiceTest extends IntegrationTestSupport {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private MemberEntity member;

    @BeforeEach
    void setUpEntity() {
        final Member memberDomain = new Member("test@test.com", "test1234!", Role.ADMIN, true);
        member = memberRepository.save(MemberEntity.of(memberDomain, passwordEncoder));
    }

    @Test
    @DisplayName("게시판의 댓글 목록을 조회한다")
    void getComments() {
        //given
        CommentEntity commentEntity = CommentEntity.of(
                "내용 1",
                "learn",
                1L,
                "tester",
                LocalDateTime.of(2024, 3, 15, 12, 0),
                1L
        );

        commentRepository.save(commentEntity);

        //when
        List<CommentResponse> comments = commentService.getComments("learn", 1L);

        //then
        assertThat(comments).hasSize(1)
                .extracting("content", "boardType", "boardId", "writer", "writtenAt", "memberId")
                .containsExactly(
                        tuple("내용 1", "learn", 1L, "tester", LocalDateTime.of(2024, 3, 15, 12, 0), 1L)
                );
    }

    @Test
    @DisplayName("게시판의 댓글을 등록한다")
    void postComment() {
        //given, when
        Long commentId = commentService.postComment("learn", 1L, "내용 1", member);

        //then
        assertThat(commentId).isNotNull();
    }

    @Test
    @DisplayName("게시판의 댓글을 삭제한다")
    void deleteComment() {
        //given
        CommentEntity commentEntity = CommentEntity.of(
                "내용 1",
                "learn",
                1L,
                "tester",
                LocalDateTime.of(2024, 3, 15, 12, 0),
                member.getId()
        );

        final CommentEntity savedComment = commentRepository.save(commentEntity);

        //when
        commentService.deleteComment("learn", 1L, savedComment.getId(), member);

        //then
        assertThat(commentRepository.findById(savedComment.getId())).isEmpty();
    }
}
