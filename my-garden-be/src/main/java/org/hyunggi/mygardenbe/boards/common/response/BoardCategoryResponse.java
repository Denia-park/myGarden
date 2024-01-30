package org.hyunggi.mygardenbe.boards.common.response;

import org.hyunggi.mygardenbe.boards.common.entity.BoardCategoryEntity;

public record BoardCategoryResponse(String code, String text) {
    public static BoardCategoryResponse of(BoardCategoryEntity boardCategoryEntity) {
        return new BoardCategoryResponse(boardCategoryEntity.getCode(), boardCategoryEntity.getText());
    }
}
