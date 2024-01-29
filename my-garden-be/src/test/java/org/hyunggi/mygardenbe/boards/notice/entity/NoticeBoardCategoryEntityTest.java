package org.hyunggi.mygardenbe.boards.notice.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeBoardCategoryEntityTest {
    @Test
    @DisplayName("NoticeCategoryEntity 생성 테스트")
    void constructor() {
        // given
        String code = "code";
        String text = "text";

        // when
        NoticeBoardCategoryEntity noticeBoardCategoryEntity = new NoticeBoardCategoryEntity(code, text);

        // then
        assertThat(noticeBoardCategoryEntity.getCode()).isEqualTo(code);
        assertThat(noticeBoardCategoryEntity.getText()).isEqualTo(text);
    }
}
