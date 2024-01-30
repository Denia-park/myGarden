package org.hyunggi.mygardenbe.boards.notice.controller;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.request.GetRequest;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.notice.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.notice.service.NoticeBoardService;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardCategoryResponse;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardResponse;
import org.hyunggi.mygardenbe.common.auth.LoginUserEntity;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/notice")
@RequiredArgsConstructor
public class NoticeBoardController {
    private final NoticeBoardService noticeBoardService;

    @GetMapping("/list")
    public ApiResponse<CustomPage<NoticeBoardResponse>> getDailyRoutine(@ModelAttribute final GetRequest getRequest) {
        final CustomPage<NoticeBoardResponse> noticeBoardResponses = getNoticeBoards(getRequest.getSearchDate(), getRequest.getSearchCondition(), getRequest.getSearchPaging());

        return ApiResponse.ok(noticeBoardResponses);
    }

    private CustomPage<NoticeBoardResponse> getNoticeBoards(final GetRequest.SearchDate searchDate, final GetRequest.SearchCondition searchCondition, final GetRequest.SearchPaging searchPaging) {
        return noticeBoardService.getNoticeBoards(
                searchDate.startDate(),
                searchDate.endDate(),
                searchCondition.category(),
                searchCondition.searchText(),
                buildPagable(searchPaging)
        );
    }

    private PageRequest buildPagable(final GetRequest.SearchPaging searchPaging) {
        return PageRequest.of(
                searchPaging.currentPage(),
                searchPaging.pageSize(),
                Sort.by(searchPaging.convertOrderToSortDirection(), searchPaging.sort(), "id")
        );
    }

    @GetMapping("/categories")
    public ApiResponse<List<NoticeBoardCategoryResponse>> getCategories() {
        return ApiResponse.ok(noticeBoardService.getCategories());
    }

    @GetMapping("/{boardId}")
    public ApiResponse<NoticeBoardResponse> getNoticeBoard(@PathVariable final Long boardId) {
        return ApiResponse.ok(noticeBoardService.getNoticeBoard(boardId));
    }

    @PostMapping
    public ApiResponse<Long> postNoticeBoard(@RequestBody final PostRequest postRequest, @LoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(noticeBoardService.postNoticeBoard(postRequest, member));
    }
}
