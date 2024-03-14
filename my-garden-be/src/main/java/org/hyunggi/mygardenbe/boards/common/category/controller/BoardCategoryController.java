package org.hyunggi.mygardenbe.boards.common.category.controller;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.category.response.BoardCategoryResponse;
import org.hyunggi.mygardenbe.boards.common.category.service.BoardCategoryService;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 게시판 분류 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/categories")
public class BoardCategoryController {
    private final BoardCategoryService boardCategoryService;

    /**
     * 게시판 분류 목록 조회
     *
     * @param boardType 분류를 조회할 게시판 타입
     * @return 게시판 분류 목록
     */
    @GetMapping
    public ApiResponse<List<BoardCategoryResponse>> getCategories(@RequestParam final String boardType) {
        return ApiResponse.ok(boardCategoryService.getCategories(boardType));
    }
}
