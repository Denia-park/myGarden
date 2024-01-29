package org.hyunggi.mygardenbe.boards.notice.service.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeBoardCategoryResponseTest {
    @Test
    @DisplayName("NoticeBoardCategoryResponse 생성 테스트")
    void constructor() {
        // given
        String code = "code";
        String text = "text";

        // when
        NoticeBoardCategoryResponse noticeBoardCategoryResponse = new NoticeBoardCategoryResponse(code, text);

        // then
        assertThat(noticeBoardCategoryResponse.code()).isEqualTo(code);
        assertThat(noticeBoardCategoryResponse.text()).isEqualTo(text);
    }
}
