package org.hyunggi.mygardenbe.boards.common.category.response;

import lombok.Builder;
import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;

@Builder
public record BoardCategoryResponse(String code, String text) {
    public static BoardCategoryResponse of(BoardCategoryEntity boardCategoryEntity) {
        return new BoardCategoryResponse(boardCategoryEntity.getCode(), boardCategoryEntity.getText());
    }
}
