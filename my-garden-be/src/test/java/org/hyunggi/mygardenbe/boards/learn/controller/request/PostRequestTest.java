package org.hyunggi.mygardenbe.boards.learn.controller.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

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

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("PostRequest 객체를 생성할 때, title이 비어있으면 예외가 발생한다.")
    void constructor_withEmptyTitle(final String title) {
        //given
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

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("PostRequest 객체를 생성할 때, category가 비어있으면 예외가 발생한다.")
    void constructor_withEmptyCategory(final String category) {
        //given
        final String title = "제목";
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

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("PostRequest 객체를 생성할 때, content가 비어있으면 예외가 발생한다.")
    void constructor_withEmptyContent(final String content) {
        //given
        final String title = "제목";
        final String category = "분류";

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

    @Test
    @DisplayName("PostRequest 객체를 생성할 때, title이 100자를 넘으면 예외가 발생한다.")
    void constructor_withTitleOver100() {
        //given
        final String title = "제목".repeat(100);
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

    @Test
    @DisplayName("PostRequest 객체를 생성할 때, category가 20자를 넘으면 예외가 발생한다.")
    void constructor_withCategoryOver20() {
        //given
        final String title = "제목";
        final String category = "분류".repeat(20);
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

    @Test
    @DisplayName("PostRequest 객체를 생성할 때, content가 4000자를 넘으면 예외가 발생한다.")
    void constructor_withContentOver4000() {
        //given
        final String title = "제목";
        final String category = "분류";
        final String content = "내용".repeat(2010);

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
