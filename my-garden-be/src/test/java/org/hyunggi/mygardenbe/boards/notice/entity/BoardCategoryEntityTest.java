package org.hyunggi.mygardenbe.boards.notice.entity;

import org.hyunggi.mygardenbe.boards.common.entity.BoardCategoryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardCategoryEntityTest {
    @Test
    @DisplayName("NoticeCategoryEntity 생성 테스트")
    void constructor() {
        // given
        final String code = "code";
        final String text = "text";

        // when
        BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity(code, text);

        // then
        assertThat(boardCategoryEntity.getCode()).isEqualTo(code);
        assertThat(boardCategoryEntity.getText()).isEqualTo(text);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("NoticeCategoryEntity 생성 테스트 - code가 null 혹은 비어있을 때")
    void constructorWithNullCode(final String code) {
        // given
        final String text = "text";

        // when, then
        assertThatThrownBy(() -> new BoardCategoryEntity(code, text))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("code는 null이거나 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("NoticeCategoryEntity 생성 테스트 - text가 null 혹은 비어있을 때")
    void constructorWithNullText(final String text) {
        // given
        final String code = "code";

        // when, then
        assertThatThrownBy(() -> new BoardCategoryEntity(code, text))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("text는 null이거나 비어있을 수 없습니다.");
    }
}
