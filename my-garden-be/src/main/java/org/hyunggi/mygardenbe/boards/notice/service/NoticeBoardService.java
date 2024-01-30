package org.hyunggi.mygardenbe.boards.notice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.notice.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.hyunggi.mygardenbe.boards.notice.repository.NoticeBoardCategoryRepository;
import org.hyunggi.mygardenbe.boards.notice.repository.NoticeBoardRepository;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardCategoryResponse;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeBoardService {
    private final NoticeBoardRepository noticeBoardRepository;
    private final NoticeBoardCategoryRepository noticeBoardCategoryService;

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

    public List<NoticeBoardCategoryResponse> getCategories() {
        return noticeBoardCategoryService.findAll().stream()
                .map(NoticeBoardCategoryResponse::of)
                .toList();
    }

    @Transactional
    public NoticeBoardResponse getNoticeBoard(final Long boardId) {
        validateBoardId(boardId);
        final NoticeBoardEntity noticeBoardEntity = getNoticeBoardEntity(boardId);
        noticeBoardEntity.increaseViewCount();

        return NoticeBoardResponse.of(noticeBoardEntity);
    }

    private void validateBoardId(final Long boardId) {
        Assert.isTrue(boardId != null && boardId > 0, "boardId는 null이 될 수 없고 0보다 커야합니다.");
    }

    private NoticeBoardEntity getNoticeBoardEntity(final Long boardId) {
        return noticeBoardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    public Long postNoticeBoard(final PostRequest postRequest, final MemberEntity member) {
        validatePostRequest(postRequest);

        final NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(
                postRequest.title(),
                postRequest.content(),
                postRequest.category(),
                postRequest.isImportant(),
                getMemberEmailId(member),
                LocalDateTime.now(),
                member.getId()
        );

        return noticeBoardRepository.save(noticeBoardEntity).getId();
    }

    private void validatePostRequest(final PostRequest postRequest) {
        Assert.isTrue(postRequest != null, "PostRequest는 null이 될 수 없습니다.");

        noticeBoardCategoryService.findByCode(postRequest.category())
                .orElseThrow(() -> new EntityNotFoundException("해당 분류가 존재하지 않습니다."));
    }

    private String getMemberEmailId(final MemberEntity member) {
        return member.getEmail().split("@")[0];
    }

    public Long putNoticeBoard(final Long boardId, final PostRequest postRequest, final MemberEntity member) {
        validatePutRequest(boardId, postRequest);

        final NoticeBoardEntity noticeBoardEntity = getNoticeBoardEntity(boardId);

        validateBoardWithMember(member, noticeBoardEntity);

        noticeBoardEntity.update(
                postRequest.title(),
                postRequest.content(),
                postRequest.category(),
                postRequest.isImportant()
        );

        return noticeBoardRepository.save(noticeBoardEntity).getId();
    }

    private void validateBoardWithMember(final MemberEntity member, final NoticeBoardEntity noticeBoardEntity) {
        if (!noticeBoardEntity.isWriter(member.getId())) {
            throw new IllegalArgumentException("해당 게시글의 작성자가 아닙니다.");
        }
    }

    private void validatePutRequest(final Long boardId, final PostRequest postRequest) {
        validateBoardId(boardId);
        Assert.isTrue(postRequest != null, "PostRequest는 null이 될 수 없습니다.");

        noticeBoardCategoryService.findByCode(postRequest.category())
                .orElseThrow(() -> new EntityNotFoundException("해당 분류가 존재하지 않습니다."));
    }
}
