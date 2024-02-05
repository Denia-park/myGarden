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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/categories")
public class BoardCategoryController {
    private final BoardCategoryService boardCategoryService;

    @GetMapping
    public ApiResponse<List<BoardCategoryResponse>> getCategories(@RequestParam final String boardType) {
        return ApiResponse.ok(boardCategoryService.getCategories(boardType));
    }
}
