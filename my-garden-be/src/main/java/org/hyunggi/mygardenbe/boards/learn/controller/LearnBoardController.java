package org.hyunggi.mygardenbe.boards.learn.controller;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.category.request.GetRequest;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.learn.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.learn.service.LearnBoardService;
import org.hyunggi.mygardenbe.boards.learn.service.response.LearnBoardResponse;
import org.hyunggi.mygardenbe.common.auth.annotation.WithLoginUserEntity;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * TIL 게시판 Controller
 */
@RestController
@RequestMapping("/api/boards/learn")
@RequiredArgsConstructor
public class LearnBoardController {
    /**
     * TIL 게시판 Service
     */
    private final LearnBoardService learnBoardService;

    /**
     * TIL 게시판 목록 조회
     *
     * @param getRequest 조회 조건
     * @return TIL 게시판 목록
     */
    @GetMapping("/list")
    public ApiResponse<CustomPage<LearnBoardResponse>> getDailyRoutine(@ModelAttribute final GetRequest getRequest) {
        final CustomPage<LearnBoardResponse> learnBoardResponses = getLearnBoards(getRequest.getSearchDate(), getRequest.getSearchCondition(), getRequest.getSearchPaging());

        return ApiResponse.ok(learnBoardResponses);
    }

    /**
     * TIL 게시판 Service를 통해서 목록 조회
     *
     * @param searchDate      조회 기간
     * @param searchCondition 조회 조건
     * @param searchPaging    페이징
     * @return TIL 게시판 목록
     */
    private CustomPage<LearnBoardResponse> getLearnBoards(final GetRequest.SearchDate searchDate, final GetRequest.SearchCondition searchCondition, final GetRequest.SearchPaging searchPaging) {
        return learnBoardService.getLearnBoards(
                searchDate.startDate(),
                searchDate.endDate(),
                searchCondition.category(),
                searchCondition.searchText(),
                buildPageable(searchPaging)
        );
    }

    /**
     * 페이징 정보를 통해서 PageRequest 생성
     *
     * @param searchPaging 페이징 정보
     * @return PageRequest
     */
    private PageRequest buildPageable(final GetRequest.SearchPaging searchPaging) {
        return PageRequest.of(
                searchPaging.currentPage(),
                searchPaging.pageSize(),
                Sort.by(searchPaging.convertOrderToSortDirection(), searchPaging.sort(), "id")
        );
    }

    /**
     * TIL 게시판 조회
     *
     * @param boardId 게시판 ID
     * @return TIL 게시판
     */
    @GetMapping("/{boardId}")
    public ApiResponse<LearnBoardResponse> getLearnBoard(@PathVariable final Long boardId) {
        return ApiResponse.ok(learnBoardService.getLearnBoard(boardId));
    }

    /**
     * TIL 게시판 등록
     *
     * @param postRequest 게시판에 등록할 정보
     * @param member      로그인한 유저 (JWT에서 추출한 정보)
     * @return 등록된 게시판 ID
     */
    @PostMapping
    public ApiResponse<Long> postLearnBoard(@RequestBody final PostRequest postRequest, @WithLoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(learnBoardService.postLearnBoard(postRequest.category(), postRequest.title(), postRequest.content(), member));
    }

    /**
     * TIL 게시판 수정
     *
     * @param boardId     게시판 ID
     * @param postRequest 게시판에 수정할 정보
     * @param member      로그인한 유저 (JWT에서 추출한 정보)
     * @return 수정된 게시판 ID
     */
    @PutMapping("/{boardId}")
    public ApiResponse<Long> putLearnBoard(@PathVariable final Long boardId, @RequestBody final PostRequest postRequest, @WithLoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(learnBoardService.putLearnBoard(boardId, postRequest.category(), postRequest.title(), postRequest.content(), member));
    }

    /**
     * TIL 게시판 삭제
     *
     * @param boardId 게시판 ID
     * @param member  로그인한 유저 (JWT에서 추출한 정보)
     * @return 삭제된 게시판 ID
     */
    @DeleteMapping("/{boardId}")
    public ApiResponse<Long> deleteLearnBoard(@PathVariable final Long boardId, @WithLoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(learnBoardService.deleteLearnBoard(boardId, member));
    }
}
