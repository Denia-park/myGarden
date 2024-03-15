package org.hyunggi.mygardenbe.boards.common.category.service.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardCategoryResponseTest {
    @Test
    @DisplayName("NoticeBoardCategoryResponse 생성 테스트")
    void constructor() {
        // given
        final String code = "code";
        final String text = "text";

        // when
        final BoardCategoryResponse boardCategoryResponse = new BoardCategoryResponse(code, text);

        // then
        assertThat(boardCategoryResponse.code()).isEqualTo(code);
        assertThat(boardCategoryResponse.text()).isEqualTo(text);
    }
}
