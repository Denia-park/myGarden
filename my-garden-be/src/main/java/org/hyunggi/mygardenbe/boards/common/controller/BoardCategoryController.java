package org.hyunggi.mygardenbe.boards.common.controller;

import lombok.RequiredArgsConstructor;
import org.hyunggi.mygardenbe.boards.common.response.BoardCategoryResponse;
import org.hyunggi.mygardenbe.boards.common.service.BoardCategoryService;
import org.hyunggi.mygardenbe.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/categories")
public class BoardCategoryController {
    private final BoardCategoryService noticeBoardService;

    @GetMapping
    public ApiResponse<List<BoardCategoryResponse>> getCategories(@RequestParam final String boardType) {
        return ApiResponse.ok(noticeBoardService.getCategories(boardType));
    }
}
