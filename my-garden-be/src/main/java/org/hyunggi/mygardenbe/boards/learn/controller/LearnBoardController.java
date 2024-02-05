package org.hyunggi.mygardenbe.boards.learn.controller;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.category.request.GetRequest;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.learn.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.learn.service.LearnBoardService;
import org.hyunggi.mygardenbe.boards.learn.service.response.LearnBoardResponse;
import org.hyunggi.mygardenbe.common.auth.LoginUserEntity;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/learn")
@RequiredArgsConstructor
public class LearnBoardController {
    private final LearnBoardService learnBoardService;

    @GetMapping("/list")
    public ApiResponse<CustomPage<LearnBoardResponse>> getDailyRoutine(@ModelAttribute final GetRequest getRequest) {
        final CustomPage<LearnBoardResponse> learnBoardResponses = getLearnBoards(getRequest.getSearchDate(), getRequest.getSearchCondition(), getRequest.getSearchPaging());

        return ApiResponse.ok(learnBoardResponses);
    }

    private CustomPage<LearnBoardResponse> getLearnBoards(final GetRequest.SearchDate searchDate, final GetRequest.SearchCondition searchCondition, final GetRequest.SearchPaging searchPaging) {
        return learnBoardService.getLearnBoards(
                searchDate.startDate(),
                searchDate.endDate(),
                searchCondition.category(),
                searchCondition.searchText(),
                buildPageable(searchPaging)
        );
    }

    private PageRequest buildPageable(final GetRequest.SearchPaging searchPaging) {
        return PageRequest.of(
                searchPaging.currentPage(),
                searchPaging.pageSize(),
                Sort.by(searchPaging.convertOrderToSortDirection(), searchPaging.sort(), "id")
        );
    }

    @GetMapping("/{boardId}")
    public ApiResponse<LearnBoardResponse> getLearnBoard(@PathVariable final Long boardId) {
        return ApiResponse.ok(learnBoardService.getLearnBoard(boardId));
    }

    @PostMapping
    public ApiResponse<Long> postLearnBoard(@RequestBody final PostRequest postRequest, @LoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(learnBoardService.postLearnBoard(postRequest, member));
    }

    @PutMapping("/{boardId}")
    public ApiResponse<Long> putLearnBoard(@PathVariable final Long boardId, @RequestBody final PostRequest postRequest, @LoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(learnBoardService.putLearnBoard(boardId, postRequest, member));
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<Long> deleteLearnBoard(@PathVariable final Long boardId, @LoginUserEntity final MemberEntity member) {
        return ApiResponse.ok(learnBoardService.deleteLearnBoard(boardId, member));
    }
}
