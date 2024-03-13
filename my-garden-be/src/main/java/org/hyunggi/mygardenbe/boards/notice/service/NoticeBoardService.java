package org.hyunggi.mygardenbe.boards.notice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.category.service.BoardCategoryService;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.hyunggi.mygardenbe.boards.notice.repository.NoticeBoardRepository;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
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
    private final BoardCategoryService boardCategoryService;

    public CustomPage<NoticeBoardResponse> getNoticeBoards(final LocalDate startDate, final LocalDate endDate, final String category, final String searchText, final Pageable pageable) {
        validateArguments(startDate, endDate, category, searchText, pageable);

        final LocalDateTime startDateTime = startDate.atStartOfDay();
        final LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        return searchNoticeBoards(startDateTime, endDateTime, category, searchText, pageable);
    }

    private void validateArguments(final LocalDate startDate, final LocalDate endDate, final String category, final String searchText, final Pageable pageable) {
        Assert.isTrue(startDate != null, "시작일은 null이 될 수 없습니다.");
        Assert.isTrue(endDate != null, "종료일은 null이 될 수 없습니다.");
        Assert.isTrue(startDate.isEqual(endDate) || startDate.isBefore(endDate), "시작일은 종료일보다 느릴 수 없습니다.");

        Assert.isTrue(category != null, "카테고리는 null이 될 수 없습니다.");
        Assert.isTrue(searchText != null, "검색어는 null이 될 수 없습니다.");
        Assert.isTrue(pageable != null, "페이징 정보는 null이 될 수 없습니다.");
    }

    private CustomPage<NoticeBoardResponse> searchNoticeBoards(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText, final Pageable pageable) {
        final Page<NoticeBoardEntity> noticeBoardEntityPage = noticeBoardRepository.searchNoticeBoards(startDateTime, endDateTime, category, searchText, pageable);

        return CustomPage.of(noticeBoardEntityPage.map(NoticeBoardResponse::of));
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

    public Long postNoticeBoard(final String category, final String title, final String content, final Boolean isImportant, final MemberEntity member) {
        validatePostRequest(category, title, content, isImportant);

        final NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(
                title,
                content,
                category,
                isImportant,
                getMemberEmailId(member),
                LocalDateTime.now(),
                member.getId()
        );

        return noticeBoardRepository.save(noticeBoardEntity).getId();
    }

    private void validatePostRequest(final String category, final String title, final String content, final Boolean isImportant) {
        validateContent(category, title, content, isImportant);
    }

    private void validateContent(final String category, final String title, final String content, final Boolean isImportant) {
        boardCategoryService.validateCategoryWithBoardType(category, "notice");

        Assert.isTrue(title != null, "제목은 null이 될 수 없습니다.");
        Assert.isTrue(content != null, "내용은 null이 될 수 없습니다.");
        Assert.isTrue(title.length() <= 100, "제목은 100자 이하여야 합니다.");
        Assert.isTrue(content.length() <= 4000, "내용은 4000자 이하여야 합니다.");
        Assert.isTrue(isImportant != null, "중요 여부는 null이 될 수 없습니다.");
    }

    private String getMemberEmailId(final MemberEntity member) {
        return member.getEmail().split("@")[0];
    }

    @Transactional
    public Long putNoticeBoard(final Long boardId, final String category, final String title, final String content, final Boolean isImportant, final MemberEntity member) {
        validatePutRequest(boardId, category, title, content, isImportant);

        final NoticeBoardEntity noticeBoardEntity = getNoticeBoardEntity(boardId);

        validateBoardWriter(member, noticeBoardEntity);

        noticeBoardEntity.update(
                title,
                content,
                category,
                isImportant
        );

        return boardId;
    }

    private void validateBoardWriter(final MemberEntity member, final NoticeBoardEntity noticeBoardEntity) {
        if (!noticeBoardEntity.isWriter(member.getId())) {
            throw new IllegalArgumentException("해당 게시글의 작성자가 아닙니다.");
        }
    }

    private void validatePutRequest(final Long boardId, final String category, final String title, final String content, final Boolean isImportant) {
        validateBoardId(boardId);

        validateContent(category, title, content, isImportant);
    }

    @Transactional
    public Long deleteNoticeBoard(final Long boardId, final MemberEntity member) {
        validateBoardId(boardId);

        final NoticeBoardEntity noticeBoardEntity = getNoticeBoardEntity(boardId);

        validateBoardWriter(member, noticeBoardEntity);

        noticeBoardRepository.delete(noticeBoardEntity);

        return boardId;
    }

    public List<NoticeBoardResponse> getNoticeImportantBoards() {
        final List<NoticeBoardEntity> noticeAlarmBoards = noticeBoardRepository.findAllByIsImportantOrderByWrittenAtDesc(true);

        return noticeAlarmBoards.stream()
                .map(NoticeBoardResponse::of)
                .toList();
    }
}
