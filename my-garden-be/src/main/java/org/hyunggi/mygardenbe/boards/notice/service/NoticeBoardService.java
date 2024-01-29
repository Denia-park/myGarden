package org.hyunggi.mygardenbe.boards.notice.service;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.notice.repository.NoticeBoardRepository;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NoticeBoardService {
    private final NoticeBoardRepository noticeBoardRepository;

    public CustomPage<NoticeBoardResponse> getNoticeBoards(final LocalDate startDate, final LocalDate endDate, final String category, final String searchText, final Pageable pageable) {
        validateArguments(startDate, endDate, category, searchText, pageable);

        final LocalDateTime startDateTime = startDate.atStartOfDay();
        final LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        //TODO: QueryDSL로 리팩토링 필요함
        if (searchText.isBlank() && category.isBlank()) {
            return findAllInDateRange(pageable, startDateTime, endDateTime);
        } else if (searchText.isBlank()) {
            return findAllInDateRangeByCategory(category, pageable, startDateTime, endDateTime);
        } else if (category.isBlank()) {
            return findAllInDateRangeWithTextSearch(searchText, pageable, startDateTime, endDateTime);
        } else {
            return findAllInDateRangeByCategoryWithTextSearch(category, searchText, pageable, startDateTime, endDateTime);
        }
    }

    private void validateArguments(final LocalDate startDate, final LocalDate endDate, final String category, final String searchText, final Pageable pageable) {
        Assert.isTrue(startDate != null, "시작일은 null이 될 수 없습니다.");
        Assert.isTrue(endDate != null, "종료일은 null이 될 수 없습니다.");
        Assert.isTrue(startDate.isEqual(endDate) || startDate.isBefore(endDate), "시작일은 종료일보다 느릴 수 없습니다.");

        Assert.isTrue(category != null, "카테고리는 null이 될 수 없습니다.");
        Assert.isTrue(searchText != null, "검색어는 null이 될 수 없습니다.");
        Assert.isTrue(pageable != null, "페이징 정보는 null이 될 수 없습니다.");
    }

    private CustomPage<NoticeBoardResponse> findAllInDateRange(final Pageable pageable, final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return CustomPage.of(
                noticeBoardRepository.findAllInDateRange(startDateTime, endDateTime, pageable)
                        .map(NoticeBoardResponse::of)
        );
    }

    private CustomPage<NoticeBoardResponse> findAllInDateRangeByCategory(final String category, final Pageable pageable, final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return CustomPage.of(
                noticeBoardRepository.findAllInDateRangeByCategory(startDateTime, endDateTime, category, pageable)
                        .map(NoticeBoardResponse::of)
        );
    }

    private CustomPage<NoticeBoardResponse> findAllInDateRangeWithTextSearch(final String searchText, final Pageable pageable, final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return CustomPage.of(
                noticeBoardRepository.findAllInDateRangeWithTextSearch(startDateTime, endDateTime, searchText, pageable)
                        .map(NoticeBoardResponse::of)
        );
    }

    private CustomPage<NoticeBoardResponse> findAllInDateRangeByCategoryWithTextSearch(final String category, final String searchText, final Pageable pageable, final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return CustomPage.of(
                noticeBoardRepository.findAllInDateRangeByCategoryWithTextSearch(startDateTime, endDateTime, category, searchText, pageable)
                        .map(NoticeBoardResponse::of)
        );
    }
}
