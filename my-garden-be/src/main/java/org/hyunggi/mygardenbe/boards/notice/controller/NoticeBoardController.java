package org.hyunggi.mygardenbe.boards.notice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.category.request.GetRequest;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.notice.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.notice.service.NoticeBoardService;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardResponse;
import org.hyunggi.mygardenbe.common.auth.annotation.WithLoginUserEntity;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 공지사항 게시판 Controller
 */
@RestController
@RequestMapping("/api/boards/notice")
@RequiredArgsConstructor
public class NoticeBoardController {
    /**
     * 공지사항 게시판 Service
     */
    private final NoticeBoardService noticeBoardService;

    /**
     * 중요 공지사항 목록 조회
     *
     * @return 중요 공지사항 목록
     */
    @GetMapping("/important")
    public ApiResponse<List<NoticeBoardResponse>> getNoticeImportantBoards() {
        return ApiResponse.ok(noticeBoardService.getNoticeImportantBoards());
    }

    /**
     * 공지사항 목록 조회
     *
     * @param getRequest 조회 조건
     * @return 공지사항 목록
     */
    @GetMapping("/list")
    public ApiResponse<CustomPage<NoticeBoardResponse>> getNoticeBoards(@ModelAttribute final GetRequest getRequest) {
        final CustomPage<NoticeBoardResponse> noticeBoardResponses = getNoticeBoards(getRequest.getSearchDate(), getRequest.getSearchCondition(), getRequest.getSearchPaging());

        return ApiResponse.ok(noticeBoardResponses);
    }

    /**
     * 공지사항 게시판 Service를 이용하여 목록 조회
     *
     * @param searchDate      조회 일자
     * @param searchCondition 조회 조건
     * @param searchPaging    페이징
     * @return 공지사항 목록
     */
    private CustomPage<NoticeBoardResponse> getNoticeBoards(final GetRequest.SearchDate searchDate, final GetRequest.SearchCondition searchCondition, final GetRequest.SearchPaging searchPaging) {
        return noticeBoardService.getNoticeBoards(
                searchDate.startDate(),
                searchDate.endDate(),
                searchCondition.category(),
                searchCondition.searchText(),
                buildPageable(searchPaging)
        );
    }

    /**
     * 페이징 정보를 이용하여 PageRequest 생성
     *
     * @param searchPaging 페이징 정보
     * @return PageRequest
     */
    private PageRequest buildPageable(final GetRequest.SearchPaging searchPaging) {
        return PageRequest.of(
                searchPaging.currentPage(),
                searchPaging.pageSize(),
                Sort.by(searchPaging.getOrder(), Sort.Order.asc("id"))
        );
    }

    /**
     * 공지사항 상세 조회
     *
     * @param boardId 게시글 ID
     * @return 공지사항 상세
     */
    @GetMapping("/{boardId}")
    public ApiResponse<NoticeBoardResponse> getNoticeBoard(@PathVariable final Long boardId) {
        //TODO: 게시판 조회시에 연관관계에 따라, Board Category를 Notice 게시판 조회 후에 또 다시 조회하는 문제가 있음 -> 당장 급한 문제는 아니니, 추후에 수정
        return ApiResponse.ok(noticeBoardService.getNoticeBoard(boardId));
    }

    /**
     * 공지사항 등록
     *
     * @param postRequest 등록할 게시글 내용
     * @param member      로그인 사용자
     * @return 등록한 게시글 ID
     */
    @PostMapping
    public ApiResponse<Long> postNoticeBoard(@RequestBody @Valid final PostRequest postRequest, @WithLoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(noticeBoardService.postNoticeBoard(postRequest.category(), postRequest.title(), postRequest.content(), postRequest.isImportant(), member));
    }

    /**
     * 공지사항 수정
     *
     * @param boardId     게시글 ID
     * @param postRequest 수정할 게시글 내용
     * @param member      로그인 사용자
     * @return 수정한 게시글 ID
     */
    @PutMapping("/{boardId}")
    public ApiResponse<Long> putNoticeBoard(@PathVariable final Long boardId, @RequestBody @Valid final PostRequest postRequest, @WithLoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(noticeBoardService.putNoticeBoard(boardId, postRequest.category(), postRequest.title(), postRequest.content(), postRequest.isImportant(), member));
    }

    /**
     * 공지사항 삭제
     *
     * @param boardId 게시글 ID
     * @param member  로그인 사용자
     * @return 삭제한 게시글 ID
     */
    @DeleteMapping("/{boardId}")
    public ApiResponse<Long> deleteNoticeBoard(@PathVariable final Long boardId, @WithLoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(noticeBoardService.deleteNoticeBoard(boardId, member));
    }
}
