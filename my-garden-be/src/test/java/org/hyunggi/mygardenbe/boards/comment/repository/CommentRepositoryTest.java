package org.hyunggi.mygardenbe.boards.comment.repository;

import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.boards.comment.entity.CommentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class CommentRepositoryTest extends IntegrationTestSupport {
    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("게시판 종류와 게시판 ID로 댓글 목록 조회한다.")
    void findAllByBoardTypeAndBoardId() {
        //given
        CommentEntity commentEntity1 = CommentEntity.of(
                "content1",
                "learn",
                1L,
                "writer",
                LocalDateTime.of(2024, 3, 15, 12, 0),
                1L
        );

        CommentEntity commentEntity2 = CommentEntity.of(
                "content2",
                "learn",
                2L,
                "writer",
                LocalDateTime.of(2024, 3, 15, 12, 10),
                1L
        );

        CommentEntity commentEntity3 = CommentEntity.of(
                "content3",
                "habit",
                1L,
                "writer",
                LocalDateTime.of(2024, 3, 15, 12, 20),
                1L
        );

        commentRepository.save(commentEntity1);
        commentRepository.save(commentEntity2);
        commentRepository.save(commentEntity3);

        //when
        var comments = commentRepository.findAllByBoardTypeAndBoardId("learn", 1L);

        //then
        assertThat(comments).hasSize(1)
                .extracting("content", "writer", "writtenAt")
                .containsExactly(
                        tuple("content1", "writer", LocalDateTime.of(2024, 3, 15, 12, 0))
                );
    }
}
