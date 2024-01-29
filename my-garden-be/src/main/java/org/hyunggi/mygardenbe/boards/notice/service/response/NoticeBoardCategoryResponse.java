package org.hyunggi.mygardenbe.boards.notice.service.response;

import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardCategoryEntity;

public record NoticeBoardCategoryResponse(String code, String text) {
    public static NoticeBoardCategoryResponse of(NoticeBoardCategoryEntity noticeBoardCategoryEntity) {
        return new NoticeBoardCategoryResponse(noticeBoardCategoryEntity.getCode(), noticeBoardCategoryEntity.getText());
    }
}
