package org.hyunggi.mygardenbe.boards.learn.service.response;

import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;
import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;

class LearnBoardResponseTest {
    @Test
    @DisplayName("of 메서드를 통해서, LearnBoardResponse 객체를 생성할 수 있다.")
    void of() {
        // given
        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("category", "카테고리", "learn");

        final LearnBoardEntity noticeBoardEntity = LearnBoardEntity.of("title", "content", boardCategoryEntity, "writer", 1L);

        // when
        final LearnBoardResponse noticeBoardResponse = LearnBoardResponse.of(noticeBoardEntity);

        // then
        assertThat(noticeBoardResponse).isNotNull();
        assertThat(noticeBoardResponse.getTitle()).isEqualTo(noticeBoardEntity.getTitle());
        assertThat(noticeBoardResponse.getContent()).isEqualTo(noticeBoardEntity.getContent());
        assertThat(noticeBoardResponse.getCategory()).isEqualTo(noticeBoardEntity.getCategoryCode());
        assertThat(noticeBoardResponse.getViews()).isEqualTo(noticeBoardEntity.getViews());
        assertThat(noticeBoardResponse.getWriter()).isEqualTo(noticeBoardEntity.getWriter());
        assertThat(noticeBoardResponse.getWrittenAt()).isEqualTo(noticeBoardEntity.getWrittenAt().format(ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
