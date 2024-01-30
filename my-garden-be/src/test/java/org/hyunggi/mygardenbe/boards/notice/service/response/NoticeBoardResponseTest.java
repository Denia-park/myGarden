package org.hyunggi.mygardenbe.boards.notice.service.response;

import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;

class NoticeBoardResponseTest {

    @Test
    @DisplayName("of 메서드를 통해서, NoticeBoardResponse 객체를 생성할 수 있다.")
    void of() {
        // given
        final NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of("title", "content", "category", "writer", 1L);

        // when
        final NoticeBoardResponse noticeBoardResponse = NoticeBoardResponse.of(noticeBoardEntity);

        // then
        assertThat(noticeBoardResponse).isNotNull();
        assertThat(noticeBoardResponse.getTitle()).isEqualTo(noticeBoardEntity.getTitle());
        assertThat(noticeBoardResponse.getContent()).isEqualTo(noticeBoardEntity.getContent());
        assertThat(noticeBoardResponse.getCategory()).isEqualTo(noticeBoardEntity.getCategory());
        assertThat(noticeBoardResponse.getIsImportant()).isEqualTo(noticeBoardEntity.getIsImportant());
        assertThat(noticeBoardResponse.getViews()).isEqualTo(noticeBoardEntity.getViews());
        assertThat(noticeBoardResponse.getWriter()).isEqualTo(noticeBoardEntity.getWriter());
        assertThat(noticeBoardResponse.getWrittenAt()).isEqualTo(noticeBoardEntity.getWrittenAt().format(ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
