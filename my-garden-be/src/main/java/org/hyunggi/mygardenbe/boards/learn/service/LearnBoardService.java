package org.hyunggi.mygardenbe.boards.learn.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.category.service.BoardCategoryService;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.hyunggi.mygardenbe.boards.learn.repository.LearnBoardRepository;
import org.hyunggi.mygardenbe.boards.learn.service.response.LearnBoardResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TIL 게시판 Service
 */
@Service
@RequiredArgsConstructor
public class LearnBoardService {
    /**
     * TIL 게시판 Entity Repository
     */
    private final LearnBoardRepository learnBoardRepository;

    /**
     * 게시판 분류 Service
     */
    private final BoardCategoryService boardCategoryService;

    /**
     * TIL 게시판 목록 조회
     *
     * @param startDate  조회 시작일
     * @param endDate    조회 종료일
     * @param category   조회할 분류
     * @param searchText 검색어
     * @param pageable   페이징
     * @return TIL 게시판 목록 (페이지)
     */
    public CustomPage<LearnBoardResponse> getLearnBoards(final LocalDate startDate, final LocalDate endDate, final String category, final String searchText, final Pageable pageable) {
        validateArguments(startDate, endDate, category, searchText, pageable);

        final LocalDateTime startDateTime = startDate.atStartOfDay();
        final LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        return searchLearnBoards(startDateTime, endDateTime, category, searchText, pageable);
    }

    /**
     * 게시판 목록 조회의 인자 검증
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
     * TIL 게시글 검색
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @param pageable      페이징
     * @return TIL 게시글 목록 (페이지)
     */
    private CustomPage<LearnBoardResponse> searchLearnBoards(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText, final Pageable pageable) {
        final Page<LearnBoardEntity> learnBoardEntityPage = learnBoardRepository.searchLearnBoards(startDateTime, endDateTime, category, searchText, pageable);

        return CustomPage.of(learnBoardEntityPage.map(LearnBoardResponse::of));
    }

    /**
     * TIL 게시글 조회
     *
     * @param boardId 게시글 ID
     * @return TIL 게시글
     */
    @Transactional
    public LearnBoardResponse getLearnBoard(final Long boardId) {
        validateBoardId(boardId);

        final LearnBoardEntity learnBoardEntity = getLearnBoardEntity(boardId);
        learnBoardEntity.increaseViewCount();

        return LearnBoardResponse.of(learnBoardEntity);
    }

    /**
     * 게시글 ID 유효성 검사
     *
     * @param boardId 게시글 ID
     */
    private void validateBoardId(final Long boardId) {
        Assert.isTrue(boardId != null && boardId > 0, "boardId는 null이 될 수 없고 0보다 커야합니다.");
    }

    /**
     * TIL 게시글 Entity 조회
     *
     * @param boardId 게시글 ID
     * @return 조회한 게시글 Entity
     */
    private LearnBoardEntity getLearnBoardEntity(final Long boardId) {
        return learnBoardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
    }

    /**
     * TIL 게시글 작성
     *
     * @param category 분류
     * @param title    제목
     * @param content  내용
     * @param member   유저 Entity
     * @return 작성한 게시글 ID
     */
    public Long postLearnBoard(final String category, final String title, final String content, final MemberEntity member) {
        validatePostRequest(category, title, content);

        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                title,
                content,
                category,
                getMemberEmailId(member),
                LocalDateTime.now(),
                member.getId()
        );

        return learnBoardRepository.save(learnBoardEntity).getId();
    }

    /**
     * 게시글 작성의 인자 검증
     *
     * @param category 분류
     * @param title    제목
     * @param content  내용
     */
    private void validatePostRequest(final String category, final String title, final String content) {
        validateContent(category, title, content);
    }

    /**
     * 게시글 내용 유효성 검사
     *
     * @param category 분류
     * @param title    제목
     * @param content  내용
     */
    private void validateContent(final String category, final String title, final String content) {
        boardCategoryService.validateCategoryWithBoardType(category, "learn");

        Assert.isTrue(StringUtils.hasText(title), "제목은 비어있을 수 없습니다.");
        Assert.isTrue(title.length() <= 100, "제목은 100자를 넘을 수 없습니다.");
        Assert.isTrue(StringUtils.hasText(content), "게시글 내용은 비어있을 수 없습니다.");
        Assert.isTrue(content.length() <= 4000, "게시글 내용은 4000자를 넘을 수 없습니다.");
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
     * TIL 게시글 수정
     *
     * @param boardId  게시글 ID
     * @param category 분류
     * @param title    제목
     * @param content  내용
     * @param member   유저 Entity
     * @return 수정한 게시글 ID
     */
    @Transactional
    public Long putLearnBoard(final Long boardId, final String category, final String title, final String content, final MemberEntity member) {
        validatePutRequest(boardId, category, title, content);

        final LearnBoardEntity learnBoardEntity = getLearnBoardEntity(boardId);

        validateBoardWriter(member, learnBoardEntity);

        learnBoardEntity.update(
                title,
                content,
                category
        );

        return boardId;
    }

    /**
     * 게시글 작성자 유효성 검사
     *
     * @param member           유저 Entity
     * @param learnBoardEntity 게시글 Entity
     */
    private void validateBoardWriter(final MemberEntity member, final LearnBoardEntity learnBoardEntity) {
        if (!learnBoardEntity.isWriter(member.getId())) {
            throw new IllegalArgumentException("해당 게시글의 작성자가 아닙니다.");
        }
    }

    /**
     * 게시글 수정의 인자 검증
     *
     * @param boardId  게시글 ID
     * @param category 분류
     * @param title    제목
     * @param content  내용
     */
    private void validatePutRequest(final Long boardId, final String category, final String title, final String content) {
        validateBoardId(boardId);

        validateContent(category, title, content);
    }

    /**
     * TIL 게시글 삭제
     *
     * @param boardId 게시글 ID
     * @param member  유저 Entity
     * @return 삭제한 게시글 ID
     */
    @Transactional
    public Long deleteLearnBoard(final Long boardId, final MemberEntity member) {
        validateBoardId(boardId);

        final LearnBoardEntity learnBoardEntity = getLearnBoardEntity(boardId);

        validateBoardWriter(member, learnBoardEntity);

        learnBoardRepository.delete(learnBoardEntity);

        return boardId;
    }
}
