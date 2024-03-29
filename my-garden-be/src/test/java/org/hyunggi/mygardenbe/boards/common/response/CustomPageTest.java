package org.hyunggi.mygardenbe.boards.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomPageTest {
    @Test
    @DisplayName("Page 객체를 이용해서, CustomPage 객체를 생성한다.")
    void CustomPage() {
        //given
        final int pageNumber = 0;
        final int pageSize = 10;
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("writtenAt").descending());
        final List<Integer> integers = List.of(1, 2, 3);
        Page<Integer> page = new PageImpl<>(integers, pageable, integers.size());

        //when
        final CustomPage<Integer> customPage = CustomPage.of(page);

        //then
        assertThat(customPage.getContent()).containsExactly(1, 2, 3);
        assertThat(customPage.getTotalPages()).isEqualTo(1);
        assertThat(customPage.getTotalElements()).isEqualTo(3);
        assertThat(customPage.getCurrentPage()).isEqualTo(1);
        assertThat(customPage.getPageSize()).isEqualTo(pageSize);
        assertThat(customPage.getIsFirst()).isTrue();
        assertThat(customPage.getIsLast()).isTrue();
    }

    @Test
    @DisplayName("Page 객체가 null일 경우, IllegalArgumentException이 발생한다.")
    void CustomPage_NullPage() {
        //given
        final Page<Integer> page = null;

        //when, then
        assertThatThrownBy(() -> CustomPage.of(page))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page 객체는 null이 될 수 없습니다.");
    }
}
