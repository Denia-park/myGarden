package org.hyunggi.mygardenbe.boards.common.category.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetRequestTest {
    @Test
    @DisplayName("시작일이 종료일보다 빠를 때, IllegalArgumentException가 발생한다.")
    void searchDateConstructor() {
        //given
        final LocalDate startDate = LocalDate.now().plusDays(1);
        final LocalDate endDate = LocalDate.now();

        //when, then
        assertThatThrownBy(
                () -> GetRequest.SearchDate.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작일은 종료일보다 빠를 수 없습니다.");
    }

    @Test
    @DisplayName("시작일이 null일 때, 시작일은 현재 날짜의 한달 전이다.")
    void searchDateConstructor2() {
        //given
        final LocalDate startDate = null;
        final LocalDate endDate = LocalDate.now();

        //when
        final GetRequest.SearchDate searchDate = GetRequest.SearchDate.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        //then
        assertThat(searchDate.startDate()).isEqualTo(LocalDate.now().minusMonths(1));
    }

    @Test
    @DisplayName("종료일이 null일 때, 종료일은 현재 날짜이다.")
    void searchDateConstructor3() {
        //given
        final LocalDate startDate = LocalDate.now().minusMonths(1);
        final LocalDate endDate = null;

        //when
        final GetRequest.SearchDate searchDate = GetRequest.SearchDate.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        //then
        assertThat(searchDate.endDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("분류가 null일 때, 분류는 빈 문자열이다.")
    void searchConditionConstructor() {
        //given
        final String category = null;
        final String searchText = "searchText";

        //when
        final GetRequest.SearchCondition searchCondition = GetRequest.SearchCondition.builder()
                .category(category)
                .searchText(searchText)
                .build();

        //then
        assertThat(searchCondition.category()).isEmpty();
    }

    @Test
    @DisplayName("검색어가 null일 때, 검색어는 빈 문자열이다.")
    void searchConditionConstructor2() {
        //given
        final String category = "category";
        final String searchText = null;

        //when
        final GetRequest.SearchCondition searchCondition = GetRequest.SearchCondition.builder()
                .category(category)
                .searchText(searchText)
                .build();

        //then
        assertThat(searchCondition.searchText()).isEmpty();
    }

    @Test
    @DisplayName("현재 페이지가 null일 때, 현재 페이지는 0이다.")
    void searchPagingConstructor() {
        //given
        final Integer currentPage = null;
        final Integer pageSize = 10;
        final String sort = "writtenAt";
        final String order = "desc";

        //when
        final GetRequest.SearchPaging searchPaging = GetRequest.SearchPaging.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .sort(sort)
                .order(order)
                .build();

        //then
        assertThat(searchPaging.currentPage()).isZero();
    }

    @Test
    @DisplayName("페이지 사이즈가 null일 때, 페이지 사이즈는 10이다.")
    void searchPagingConstructor2() {
        //given
        final Integer currentPage = 0;
        final Integer pageSize = null;
        final String sort = "writtenAt";
        final String order = "desc";

        //when
        final GetRequest.SearchPaging searchPaging = GetRequest.SearchPaging.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .sort(sort)
                .order(order)
                .build();

        //then
        assertThat(searchPaging.pageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("정렬 기준이 null일 때, 정렬 기준은 writtenAt이다.")
    void searchPagingConstructor3() {
        //given
        final Integer currentPage = 0;
        final Integer pageSize = 10;
        final String sort = null;
        final String order = "desc";

        //when
        final GetRequest.SearchPaging searchPaging = GetRequest.SearchPaging.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .sort(sort)
                .order(order)
                .build();

        //then
        assertThat(searchPaging.sort()).isEqualTo("writtenAt");
    }

    @Test
    @DisplayName("정렬 순서가 ['writtenAt', 'title', 'category', 'views'] 해당 값중에 없을 때, IllegalArgumentException가 발생한다.")
    void searchPagingConstructor5() {
        //given
        final Integer currentPage = 0;
        final Integer pageSize = 10;
        final String sort = "writer";
        final String order = "desc";

        //when, then
        assertThatThrownBy(
                () -> GetRequest.SearchPaging.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .sort(sort)
                        .order(order)
                        .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("정렬 기준은 writtenAt, title, category, views 중 하나여야 합니다.");
    }

    @Test
    @DisplayName("정렬 순서가 null일 때, 정렬 순서는 desc이다.")
    void searchPagingConstructor4() {
        //given
        final Integer currentPage = 0;
        final Integer pageSize = 10;
        final String sort = "writtenAt";
        final String order = null;

        //when
        final GetRequest.SearchPaging searchPaging = GetRequest.SearchPaging.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .sort(sort)
                .order(order)
                .build();

        //then
        assertThat(searchPaging.order()).isEqualTo("desc");
    }

    @Test
    @DisplayName("정렬 순서가 ['asc', 'desc'] 해당 값중에 없을 때, IllegalArgumentException가 발생한다.")
    void searchPagingConstructor6() {
        //given
        final Integer currentPage = 0;
        final Integer pageSize = 10;
        final String sort = "writtenAt";
        final String order = "ascending";

        //when, then
        assertThatThrownBy(
                () -> GetRequest.SearchPaging.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .sort(sort)
                        .order(order)
                        .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("정렬 순서는 asc, desc 중 하나여야 합니다.");
    }

    @Test
    @DisplayName("모든 값이 null일 때, 기본 값을 가지고 GetRequest 객체를 생성한다.")
    void getRequestConstructor() {
        //given
        final GetRequest.SearchDate searchDate = GetRequest.SearchDate.builder()
                .startDate(null)
                .endDate(null)
                .build();
        final GetRequest.SearchCondition searchCondition = GetRequest.SearchCondition.builder()
                .category(null)
                .searchText(null)
                .build();
        final GetRequest.SearchPaging searchPaging = GetRequest.SearchPaging.builder()
                .currentPage(null)
                .pageSize(null)
                .sort(null)
                .order(null)
                .build();

        //when
        final GetRequest getRequest = GetRequest.of(searchDate, searchCondition, searchPaging);

        //then
        final GetRequest.SearchDate requestSearchDate = getRequest.getSearchDate();
        final GetRequest.SearchCondition requestSearchCondition = getRequest.getSearchCondition();
        final GetRequest.SearchPaging requestSearchPaging = getRequest.getSearchPaging();

        assertThat(requestSearchDate.startDate()).isEqualTo(LocalDate.now().minusMonths(1));
        assertThat(requestSearchDate.endDate()).isEqualTo(LocalDate.now());
        assertThat(requestSearchCondition.category()).isEmpty();
        assertThat(requestSearchCondition.searchText()).isEmpty();
        assertThat(requestSearchPaging.currentPage()).isZero();
        assertThat(requestSearchPaging.pageSize()).isEqualTo(10);
        assertThat(requestSearchPaging.sort()).isEqualTo("writtenAt");
        assertThat(requestSearchPaging.order()).isEqualTo("desc");
    }

    @ParameterizedTest
    @MethodSource("provideGetRequestOrder")
    @DisplayName("convertOrderToSortDirection() 메서드를 통해, order를 Sort.Direction로 변환한다.")
    void convertOrderToSortDirection(final String order, final Sort.Direction expected) {
        //given
        final GetRequest.SearchPaging searchPaging = GetRequest.SearchPaging.builder()
                .currentPage(null)
                .pageSize(null)
                .sort(null)
                .order(order)
                .build();

        //when
        final Sort.Direction sortDirection = searchPaging.convertOrderToSortDirection();

        //then
        assertThat(sortDirection).isEqualTo(expected);
    }

    public static Stream<Arguments> provideGetRequestOrder() {
        return Stream.of(
                Arguments.of("desc", Sort.Direction.DESC),
                Arguments.of("asc", Sort.Direction.ASC)
        );
    }

    @Test
    @DisplayName("SearchPaging의 현재 페이지로 1이상의 정수를 넣으면, 요청 페이지의 -1된 값을 반환한다.")
    void currentPage() {
        //given
        final Integer currentPage = 2;
        final Integer pageSize = 10;
        final String sort = "writtenAt";
        final String order = "desc";

        //when
        final GetRequest.SearchPaging searchPaging = GetRequest.SearchPaging.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .sort(sort)
                .order(order)
                .build();

        //then
        assertThat(searchPaging.currentPage()).isEqualTo(1);
    }
}
