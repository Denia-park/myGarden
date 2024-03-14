package org.hyunggi.mygardenbe.boards.common.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 커스텀 Page
 *
 * @param <T> Page의 Content Type
 */
@Getter
public class CustomPage<T> {
    /**
     * 총 페이지 수
     */
    private final Integer totalPages;

    /**
     * 총 개수
     */
    private final Long totalElements;

    /**
     * 현재 페이지
     */
    private final Integer currentPage;

    /**
     * 페이지 크기
     */
    private final Integer pageSize;

    /**
     * 현재 페이지가 첫 페이지인지 여부
     */
    private final Boolean isFirst;

    /**
     * 현재 페이지가 마지막 페이지 여부
     */
    private final Boolean isLast;

    /**
     * 페이지 내용
     */
    private final List<T> content;

    @Builder(access = AccessLevel.PRIVATE)
    private CustomPage(final Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
    }

    /**
     * Page 객체로부터 CustomPage 객체 생성
     *
     * @param page Page 객체
     * @param <T>  Page의 Content Type
     * @return CustomPage 객체
     */
    public static <T> CustomPage<T> of(final Page<T> page) {
        validate(page);

        return CustomPage.<T>builder()
                .page(page)
                .build();
    }

    /**
     * Page 객체 유효성 검사
     *
     * @param page Page 객체
     * @param <T>  Page의 Content Type
     */
    private static <T> void validate(final Page<T> page) {
        if (page == null) {
            throw new IllegalArgumentException("Page 객체는 null이 될 수 없습니다.");
        }
    }
}
