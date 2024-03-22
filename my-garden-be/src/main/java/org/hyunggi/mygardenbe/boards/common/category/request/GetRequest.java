package org.hyunggi.mygardenbe.boards.common.category.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * 게시글 조회시에 사용되는 Request (검색 및 필터링 할 때 사용)
 */
@Getter
public final class GetRequest {
    /**
     * 조회 기간
     */
    private final SearchDate searchDate;

    /**
     * 조회 조건
     */
    private final SearchCondition searchCondition;

    /**
     * 페이징
     */
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

    /**
     * GetRequest 생성 메서드
     *
     * @param searchDate      조회 기간
     * @param searchCondition 조회 조건
     * @param searchPaging    페이징
     * @return GetRequest
     */
    public static GetRequest of(final SearchDate searchDate, final SearchCondition searchCondition, final SearchPaging searchPaging) {
        return new GetRequest(searchDate, searchCondition, searchPaging);
    }

    /**
     * 조회 기간
     *
     * @param startDate 조회 시작일
     * @param endDate   조회 종료일
     */
    @Builder
    public record SearchDate(LocalDate startDate, LocalDate endDate) {
        public SearchDate(LocalDate startDate, LocalDate endDate) {
            validateSearchDate(startDate, endDate);

            this.startDate = initStartDate(startDate);
            this.endDate = initEndDate(endDate);
        }

        /**
         * 조회 시작일과 종료일의 유효성 검사
         *
         * @param startDate 조회 시작일
         * @param endDate   조회 종료일
         */
        private void validateSearchDate(final LocalDate startDate, final LocalDate endDate) {
            if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("시작일은 종료일보다 빠를 수 없습니다.");
            }
        }

        /**
         * 조회 시작일 초기화
         * <br><br>
         * - 조회 시작일이 없을 경우, 1년 전으로 초기화 <br>
         * - 조회 시작일이 있을 경우, 그대로 반환
         *
         * @param startDate 조회 시작일
         * @return 초기화된 조회 시작일
         */
        private LocalDate initStartDate(LocalDate startDate) {
            if (startDate == null) {
                return LocalDate.now().minusYears(1);
            }

            return startDate;
        }

        /**
         * 조회 종료일 초기화
         * <br><br>
         * - 조회 종료일이 없을 경우, 현재 날짜로 초기화 <br>
         * - 조회 종료일이 있을 경우, 그대로 반환
         *
         * @param endDate 조회 종료일
         * @return 초기화된 조회 종료일
         */
        private LocalDate initEndDate(LocalDate endDate) {
            if (endDate == null) {
                return LocalDate.now();
            }

            return endDate;
        }
    }

    /**
     * 조회 조건
     *
     * @param category   분류
     * @param searchText 검색어
     */
    @Builder
    public record SearchCondition(String category, String searchText) {
        public SearchCondition(String category, String searchText) {
            this.category = initCategory(category);
            this.searchText = initSearchText(searchText);
        }

        /**
         * 분류 초기화
         * <br><br>
         * - 분류가 없을 경우, 빈 문자열로 초기화
         *
         * @param category 분류
         * @return 초기화된 분류
         */
        private String initCategory(final String category) {
            if (category == null || category.trim().isBlank()) {
                return "";
            }

            return category;
        }

        /**
         * 검색어 초기화
         * <br><br>
         * - 검색어가 없을 경우, 빈 문자열로 초기화
         *
         * @param searchText 검색어
         * @return 초기화된 검색어
         */
        private String initSearchText(String searchText) {
            if (searchText == null || searchText.trim().isBlank()) {
                return "";
            }

            return searchText;
        }
    }

    /**
     * 페이징
     *
     * @param currentPage 현재 페이지
     * @param pageSize    페이지 크기
     * @param sort        정렬 기준
     * @param order       정렬 순서
     */
    @Builder
    public record SearchPaging(Integer currentPage, Integer pageSize, String sort, String order) {
        public SearchPaging(final Integer currentPage, final Integer pageSize, final String sort, final String order) {
            this.currentPage = initCurrentPage(currentPage);
            this.pageSize = initPageSize(pageSize);
            this.sort = initSort(sort);
            this.order = initOrder(order);
        }

        /**
         * 현재 페이지 초기화
         * <br><br>
         * - 현재 페이지가 없거나 1보다 작은 경우, 0으로 초기화 <br>
         * - 현재 페이지가 1 이상일 경우, 1을 뺀 값으로 초기화
         *
         * @param currentPage 현재 페이지
         * @return 초기화된 현재 페이지
         */
        private Integer initCurrentPage(final Integer currentPage) {
            if (currentPage == null || currentPage < 1) {
                return 0;
            }

            return currentPage - 1;
        }

        /**
         * 페이지 크기 초기화
         * <br><br>
         * - 페이지 크기가 없거나 1보다 작은 경우, 10으로 초기화
         *
         * @param pageSize 페이지 크기
         * @return 초기화된 페이지 크기
         */
        private Integer initPageSize(final Integer pageSize) {
            if (pageSize == null || pageSize < 1) {
                return 10;
            }

            return pageSize;
        }

        /**
         * 정렬 기준 초기화
         * <br><br>
         * - 정렬 기준이 없을 경우, writtenAt으로 초기화 <br>
         * - 정렬 기준 유효성 검사 진행
         *
         * @param sort 정렬 기준
         * @return 초기화된 정렬 기준
         */
        private String initSort(final String sort) {
            if (sort == null || sort.isBlank()) {
                return "writtenAt";
            }

            if (sort.equals("category")) {
                return "category.text";
            }

            return validateSort(sort);
        }

        /**
         * 정렬 기준 유효성 검사
         * <br><br>
         * - 정렬 기준이 writtenAt, title, category, views 중 하나일 경우, 해당 정렬 조건 반환 <br>
         * - 정렬 기준이 writtenAt, title, category, views 중 하나가 아닐 경우, IllegalArgumentException 발생
         *
         * @param sort 정렬 기준
         * @return 유효한 정렬 기준
         */
        private String validateSort(final String sort) {
            return Stream.of("writtenAt", "title", "category", "views")
                    .filter(s -> s.equals(sort))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("정렬 기준은 writtenAt, title, category, views 중 하나여야 합니다."));
        }

        /**
         * 정렬 순서 초기화
         * <br><br>
         * - 정렬 순서가 없을 경우, desc로 초기화 <br>
         * - 정렬 순서 유효성 검사 진행
         *
         * @param order 정렬 순서
         * @return 초기화된 정렬 순서
         */
        private String initOrder(final String order) {
            if (order == null || order.isBlank()) {
                return "desc";
            }

            return validateOrder(order);
        }

        /**
         * 정렬 순서 유효성 검사
         * <br><br>
         * - 정렬 순서가 asc, desc 중 하나일 경우, 해당 정렬 순서 반환 <br>
         * - 정렬 순서가 asc, desc 중 하나가 아닐 경우, IllegalArgumentException 발생
         *
         * @param order 정렬 순서
         * @return 유효한 정렬 순서
         */
        private String validateOrder(final String order) {
            return Stream.of("asc", "desc")
                    .filter(o -> o.equals(order))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("정렬 순서는 asc, desc 중 하나여야 합니다."));
        }

        /**
         * 정렬 기준을 Sort.Order로 변환
         *
         * @return Sort.Order
         */
        public Sort.Order getOrder() {
            return Sort.Order.by(sort).with(convertOrderToSortDirection());
        }

        /**
         * 정렬 순서를 Sort.Direction으로 변환
         *
         * @return Sort.Direction
         */
        private Sort.Direction convertOrderToSortDirection() {
            if (order.equals("desc"))
                return Sort.Direction.DESC;
            else
                return Sort.Direction.ASC;
        }
    }
}
