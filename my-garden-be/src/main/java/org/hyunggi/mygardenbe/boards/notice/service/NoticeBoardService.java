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

/**
 * 공지사항 게시판 Service
 */
@Service
@RequiredArgsConstructor
public class NoticeBoardService {
    /**
     * 공지사항 게시판 Entity Repository
     */
    private final NoticeBoardRepository noticeBoardRepository;

    /**
     * 게시판 분류 Service
     */
    private final BoardCategoryService boardCategoryService;

    /**
     * 공지사항 게시판 목록 조회 (중요 여부 X인 공지사항)
     *
     * @param startDate  조회 시작일
     * @param endDate    조회 종료일
     * @param category   조회할 분류
     * @param searchText 검색어
     * @param pageable   페이징
     * @return 공지사항 게시판 목록 (페이지)
     */
    public CustomPage<NoticeBoardResponse> getNoticeBoards(final LocalDate startDate, final LocalDate endDate, final String category, final String searchText, final Pageable pageable) {
        validateArguments(startDate, endDate, category, searchText, pageable);

        final LocalDateTime startDateTime = startDate.atStartOfDay();
        final LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        return searchNoticeBoards(startDateTime, endDateTime, category, searchText, pageable);
    }

    /**
     * 공지사항 게시판 목록 조회 인자 유효성 검증
     *
     * @param startDate  조회 시작일
     * @param endDate    조회 종료일
     * @param category   조회할 분류
     * @param searchText 검색어
     * @param pageable   페이징
     */
    private void validateArguments(final LocalDate startDate, final LocalDate endDate, final String category, final String searchText, final Pageable pageable) {
        Assert.isTrue(startDate != null, "시작일은 null이 될 수 없습니다.");
        Assert.isTrue(endDate != null, "종료일은 null이 될 수 없습니다.");
        Assert.isTrue(startDate.isEqual(endDate) || startDate.isBefore(endDate), "시작일은 종료일보다 느릴 수 없습니다.");

        Assert.isTrue(category != null, "분류는 null이 될 수 없습니다.");
        Assert.isTrue(searchText != null, "검색어는 null이 될 수 없습니다.");
        Assert.isTrue(pageable != null, "페이징 정보는 null이 될 수 없습니다.");
    }

    /**
     * 공지사항 게시판 Service를 통해서 공지사항 목록 조회
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @param pageable      페이징
     * @return 공지사항 게시판 목록 (페이지)
     */
    private CustomPage<NoticeBoardResponse> searchNoticeBoards(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText, final Pageable pageable) {
        final Page<NoticeBoardEntity> noticeBoardEntityPage = noticeBoardRepository.searchNoticeBoards(startDateTime, endDateTime, category, searchText, pageable);

        return CustomPage.of(noticeBoardEntityPage.map(NoticeBoardResponse::of));
    }

    /**
     * 공지사항 게시판 조회
     * <br><br>
     * - 조회시 조회수 증가
     *
     * @param boardId 게시글 ID
     * @return 공지사항 게시판 조회 결과
     */
    @Transactional
    public NoticeBoardResponse getNoticeBoard(final Long boardId) {
        validateBoardId(boardId);

        final NoticeBoardEntity noticeBoardEntity = getNoticeBoardEntity(boardId);
        noticeBoardEntity.increaseViewCount();

        return NoticeBoardResponse.of(noticeBoardEntity);
    }

    /**
     * 게시글 ID 유효성 검증
     *
     * @param boardId 게시글 ID
     */
    private void validateBoardId(final Long boardId) {
        Assert.isTrue(boardId != null && boardId > 0, "boardId는 null이 될 수 없고 0보다 커야합니다.");
    }

    /**
     * 게시글 ID로 게시글 조회
     *
     * @param boardId 게시글 ID
     * @return 게시글 Entity
     */
    private NoticeBoardEntity getNoticeBoardEntity(final Long boardId) {
        return noticeBoardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    /**
     * 공지사항 게시판 작성
     *
     * @param category    분류
     * @param title       제목
     * @param content     내용
     * @param isImportant 중요 여부
     * @param member      유저 Entity
     * @return 게시글 ID
     */
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

    /**
     * 게시글 작성의 인자 검증
     *
     * @param category    분류
     * @param title       제목
     * @param content     내용
     * @param isImportant 중요 여부
     */
    private void validatePostRequest(final String category, final String title, final String content, final Boolean isImportant) {
        validateContent(category, title, content, isImportant);
    }

    /**
     * 게시글 작성의 내용 검증
     *
     * @param category    분류
     * @param title       제목
     * @param content     내용
     * @param isImportant 중요 여부
     */
    private void validateContent(final String category, final String title, final String content, final Boolean isImportant) {
        boardCategoryService.validateCategoryWithBoardType(category, "notice");

        Assert.isTrue(title != null, "제목은 null이 될 수 없습니다.");
        Assert.isTrue(content != null, "내용은 null이 될 수 없습니다.");
        Assert.isTrue(title.length() <= 100, "제목은 100자 이하여야 합니다.");
        Assert.isTrue(content.length() <= 4000, "내용은 4000자 이하여야 합니다.");
        Assert.isTrue(isImportant != null, "중요 여부는 null이 될 수 없습니다.");
    }

    /**
     * 유저 Entity에서 작성자의 이메일 ID 추출
     *
     * @param member 유저 Entity
     * @return 이메일 ID
     */
    private String getMemberEmailId(final MemberEntity member) {
        return member.getEmail().split("@")[0];
    }

    /**
     * 공지사항 게시판 수정
     *
     * @param boardId     게시글 ID
     * @param category    분류
     * @param title       제목
     * @param content     내용
     * @param isImportant 중요 여부
     * @param member      유저 Entity
     * @return 게시글 ID
     */
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

    /**
     * 게시글 작성자 유효성 검증
     *
     * @param member            유저 Entity
     * @param noticeBoardEntity 게시글 Entity
     */
    private void validateBoardWriter(final MemberEntity member, final NoticeBoardEntity noticeBoardEntity) {
        if (!noticeBoardEntity.isWriter(member.getId())) {
            throw new IllegalArgumentException("해당 게시글의 작성자가 아닙니다.");
        }
    }

    /**
     * 게시글 수정의 인자 검증
     *
     * @param boardId     게시글 ID
     * @param category    분류
     * @param title       제목
     * @param content     내용
     * @param isImportant 중요 여부
     */
    private void validatePutRequest(final Long boardId, final String category, final String title, final String content, final Boolean isImportant) {
        validateBoardId(boardId);

        validateContent(category, title, content, isImportant);
    }

    /**
     * 공지사항 게시판 삭제
     *
     * @param boardId 게시글 ID
     * @param member  유저 Entity
     * @return 삭제한 게시글 ID
     */
    @Transactional
    public Long deleteNoticeBoard(final Long boardId, final MemberEntity member) {
        validateBoardId(boardId);

        final NoticeBoardEntity noticeBoardEntity = getNoticeBoardEntity(boardId);

        validateBoardWriter(member, noticeBoardEntity);

        noticeBoardRepository.delete(noticeBoardEntity);

        return boardId;
    }

    /**
     * 중요 공지사항 게시판 목록 조회
     *
     * @return 중요 공지사항 게시판 목록
     */
    public List<NoticeBoardResponse> getNoticeImportantBoards() {
        final List<NoticeBoardEntity> noticeAlarmBoards = noticeBoardRepository.findAllByIsImportantOrderByWrittenAtDesc(true);

        return noticeAlarmBoards.stream()
                .map(NoticeBoardResponse::of)
                .toList();
    }
}
