package org.hyunggi.mygardenbe.boards.learn.controller.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostRequestTest {
    @Test
    @DisplayName("PostRequest 객체를 생성한다.")
    void constructor() {
        //given
        final String title = "제목";
        final String category = "분류";
        final String content = "내용";

        //when
        final PostRequest postRequest = PostRequest.builder()
                .title(title)
                .category(category)
                .content(content)
                .build();

        //then
        assertEquals(title, postRequest.title());
        assertEquals(category, postRequest.category());
        assertEquals(content, postRequest.content());
    }
}
