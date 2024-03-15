package org.hyunggi.mygardenbe.boards.comment.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 댓글 작성시에 사용되는 Request
 *
 * @param content 댓글 내용
 */
@Builder
public record CommentRequest(
        @NotBlank(message = "내용은 비어있을 수 없습니다.")
        @Size(max = 300, message = "내용은 300자를 넘을 수 없습니다.")
        String content
) {
}
