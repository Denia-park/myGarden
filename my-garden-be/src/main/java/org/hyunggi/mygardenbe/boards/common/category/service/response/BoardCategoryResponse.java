package org.hyunggi.mygardenbe.boards.common.category.service.response;

import lombok.Builder;
import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;

/**
 * 게시판 분류 응답
 *
 * @param code 분류 코드
 * @param text 분류 명
 */
@Builder
public record BoardCategoryResponse(String code, String text) {
    public static BoardCategoryResponse of(BoardCategoryEntity boardCategoryEntity) {
        return new BoardCategoryResponse(boardCategoryEntity.getCode(), boardCategoryEntity.getText());
    }
}
