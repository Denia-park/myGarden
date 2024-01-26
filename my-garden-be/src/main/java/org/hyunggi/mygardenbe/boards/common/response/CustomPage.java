package org.hyunggi.mygardenbe.boards.common.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class CustomPage<T> {
    private final Integer totalPages;
    private final Long totalElements;
    private final Integer currentPage;
    private final Integer pageSize;
    private final Boolean isFirst;
    private final Boolean isLast;
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

    public static <T> CustomPage<T> of(final Page<T> page) {
        validate(page);

        return CustomPage.<T>builder()
                .page(page)
                .build();
    }

    private static <T> void validate(final Page<T> page) {
        if (page == null) {
            throw new IllegalArgumentException("Page 객체는 null이 될 수 없습니다.");
        }
    }
}
