package org.hyunggi.mygardenbe.boards.notice.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PostRequest(
        @NotBlank(message = "제목은 비어있을 수 없습니다.")
        @Size(max = 100, message = "제목은 100자를 넘을 수 없습니다.")
        String title,
        @NotBlank(message = "분류는 비어있을 수 없습니다.")
        @Size(max = 20, message = "분류는 20자를 넘을 수 없습니다.")
        String category,
        @NotBlank(message = "내용은 비어있을 수 없습니다.")
        @Size(max = 4000, message = "내용은 4000자를 넘을 수 없습니다.")
        String content,
        @NotNull(message = "중요 여부는 비어있을 수 없습니다.")
        Boolean isImportant
) {
}
