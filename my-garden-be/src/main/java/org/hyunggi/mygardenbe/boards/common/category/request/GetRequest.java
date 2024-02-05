package org.hyunggi.mygardenbe.boards.common.category.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.stream.Stream;

@Getter
public final class GetRequest {
    private final SearchDate searchDate;
    private final SearchCondition searchCondition;
    private final SearchPaging searchPaging;

    @Builder
    public GetRequest(final LocalDate startDate, final LocalDate endDate, final String category, final String searchText, final Integer currentPage, final Integer pageSize, final String sort, final String order) {
        this.searchDate = new SearchDate(startDate, endDate);
        this.searchCondition = new SearchCondition(category, searchText);
        this.searchPaging = new SearchPaging(currentPage, pageSize, sort, order);
    }

    private GetRequest(final SearchDate searchDate, final SearchCondition searchCondition, final SearchPaging searchPaging) {
        this.searchDate = searchDate;
        this.searchCondition = searchCondition;
        this.searchPaging = searchPaging;
    }

    public static GetRequest of(final SearchDate searchDate, final SearchCondition searchCondition, final SearchPaging searchPaging) {
        return new GetRequest(searchDate, searchCondition, searchPaging);
    }

    @Builder
    public record SearchDate(LocalDate startDate, LocalDate endDate) {
        public SearchDate(LocalDate startDate, LocalDate endDate) {
            validateSearchDate(startDate, endDate);

            this.startDate = initStartDate(startDate);
            this.endDate = initEndDate(endDate);
        }

        private void validateSearchDate(final LocalDate startDate, final LocalDate endDate) {
            if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("시작일은 종료일보다 빠를 수 없습니다.");
            }
        }

        private LocalDate initStartDate(LocalDate startDate) {
            if (startDate == null) {
                return LocalDate.now().minusMonths(1);
            }

            return startDate;
        }

        private LocalDate initEndDate(LocalDate endDate) {
            if (endDate == null) {
                return LocalDate.now();
            }

            return endDate;
        }
    }

    @Builder
    public record SearchCondition(String category, String searchText) {
        public SearchCondition(String category, String searchText) {
            this.category = initCategory(category);
            this.searchText = initSearchText(searchText);
        }

        private String initCategory(final String category) {
            if (category == null || category.trim().isBlank()) {
                return "";
            }

            return category;
        }

        private String initSearchText(String searchText) {
            if (searchText == null || searchText.trim().isBlank()) {
                return "";
            }

            return searchText;
        }
    }

    @Builder
    public record SearchPaging(Integer currentPage, Integer pageSize, String sort, String order) {
        public SearchPaging(final Integer currentPage, final Integer pageSize, final String sort, final String order) {
            this.currentPage = initCurrentPage(currentPage);
            this.pageSize = initPageSize(pageSize);
            this.sort = initSort(sort);
            this.order = initOrder(order);
        }

        private Integer initCurrentPage(final Integer currentPage) {
            if (currentPage == null || currentPage < 1) {
                return 0;
            }

            return currentPage - 1;
        }

        private Integer initPageSize(final Integer pageSize) {
            if (pageSize == null || pageSize < 1) {
                return 10;
            }

            return pageSize;
        }

        private String initSort(final String sort) {
            if (sort == null || sort.isBlank()) {
                return "writtenAt";
            }
            return validateSort(sort);
        }

        private String validateSort(final String sort) {
            return Stream.of("writtenAt", "title", "category", "views")
                    .filter(s -> s.equals(sort))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("정렬 기준은 writtenAt, title, category, views 중 하나여야 합니다."));
        }

        private String initOrder(final String order) {
            if (order == null || order.isBlank()) {
                return "desc";
            }

            return validateOrder(order);
        }

        private String validateOrder(final String order) {
            return Stream.of("asc", "desc")
                    .filter(o -> o.equals(order))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("정렬 순서는 asc, desc 중 하나여야 합니다."));
        }

        public Sort.Direction convertOrderToSortDirection() {
            if (order.equals("desc"))
                return Sort.Direction.DESC;
            else
                return Sort.Direction.ASC;
        }
    }
}
