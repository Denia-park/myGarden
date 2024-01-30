package org.hyunggi.mygardenbe.boards.notice.service;

import jakarta.persistence.EntityNotFoundException;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.notice.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardCategoryEntity;
import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.hyunggi.mygardenbe.boards.notice.repository.NoticeBoardCategoryRepository;
import org.hyunggi.mygardenbe.boards.notice.repository.NoticeBoardRepository;
import org.hyunggi.mygardenbe.boards.notice.service.response.NoticeBoardResponse;
import org.hyunggi.mygardenbe.member.domain.Member;
import org.hyunggi.mygardenbe.member.domain.Role;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;

@Transactional
class NoticeBoardServiceTest extends IntegrationTestSupport {
    @Autowired
    NoticeBoardRepository noticeBoardRepository;
    @Autowired
    NoticeBoardService noticeBoardService;
    @Autowired
    NoticeBoardCategoryRepository noticeBoardCategoryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MemberEntity member;

    @BeforeEach
    void setUpEntity() {
        Member memberDomain = new Member("test@test.com", "test1234!", Role.ADMIN, true);
        member = memberRepository.save(MemberEntity.of(memberDomain, passwordEncoder));

        NoticeBoardCategoryEntity noticeBoardCategoryEntity = new NoticeBoardCategoryEntity("project", "프로젝트");
        noticeBoardCategoryRepository.save(noticeBoardCategoryEntity);
    }

    @Test
    @DisplayName("category 및 searchText 없이 조회를 하면, 기간 내의 모든 공지사항을 조회할 수 있다.")
    void getNoticeBoardsWithoutCategoryAndSearchText() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 26);
        LocalDate endDate = startDate.plusDays(1);
        String category = "";
        String searchText = "";
        Pageable pageable = PageRequest.of(0, 10);

        noticeBoardRepository.save(buildNoticeBoardWith("title1", "content1", "category1"));
        noticeBoardRepository.save(buildNoticeBoardWith("title2", "content2", "category2"));
        noticeBoardRepository.save(buildNoticeBoardWith("title3", "content3", "category3"));

        // when
        final CustomPage<NoticeBoardResponse> noticeBoards = noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable);

        // then
        assertThat(noticeBoards.getTotalElements()).isEqualTo(3);
        assertThat(noticeBoards.getCurrentPage()).isEqualTo(1);
        assertThat(noticeBoards.getTotalPages()).isEqualTo(1);
        assertThat(noticeBoards.getPageSize()).isEqualTo(10);
        assertThat(noticeBoards.getContent()).hasSize(3)
                .extracting("title", "content", "category")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1", "category1"),
                        tuple("title2", "content2", "category2"),
                        tuple("title3", "content3", "category3")
                );
    }

    private NoticeBoardEntity buildNoticeBoardWith(final String title, final String content, final String category) {
        return NoticeBoardEntity.of(
                title,
                content,
                category,
                true,
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                1L
        );
    }

    @Test
    @DisplayName("category가 있고, searchText가 없으면, 기간 내의 해당 카테고리의 공지사항을 조회할 수 있다.")
    void getNoticeBoardsWithCategory() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 26);
        LocalDate endDate = startDate.plusDays(1);
        String category = "category1";
        String searchText = "";
        Pageable pageable = PageRequest.of(0, 10);

        noticeBoardRepository.save(buildNoticeBoardWith("title1", "content1", "category1"));
        noticeBoardRepository.save(buildNoticeBoardWith("title2", "content2", "category2"));
        noticeBoardRepository.save(buildNoticeBoardWith("title3", "content3", "category3"));

        // when
        final CustomPage<NoticeBoardResponse> noticeBoards = noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable);

        // then
        assertThat(noticeBoards.getTotalElements()).isEqualTo(1);
        assertThat(noticeBoards.getCurrentPage()).isEqualTo(1);
        assertThat(noticeBoards.getTotalPages()).isEqualTo(1);
        assertThat(noticeBoards.getPageSize()).isEqualTo(10);
        assertThat(noticeBoards.getContent()).hasSize(1)
                .extracting("title", "content", "category")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1", "category1")
                );
    }

    @Test
    @DisplayName("category가 없고, searchText가 있으면, 기간 내의 해당 검색어가 포함된 공지사항을 조회할 수 있다.")
    void getNoticeBoardsWithSearchText() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 26);
        LocalDate endDate = startDate.plusDays(1);
        String category = "";
        String searchText = "11";
        Pageable pageable = PageRequest.of(0, 10);

        noticeBoardRepository.save(buildNoticeBoardWith("title11", "content", "category1"));
        noticeBoardRepository.save(buildNoticeBoardWith("title2", "content2", "category2"));
        noticeBoardRepository.save(buildNoticeBoardWith("title3", "content11", "category3"));

        // when
        final CustomPage<NoticeBoardResponse> noticeBoards = noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable);

        // then
        assertThat(noticeBoards.getTotalElements()).isEqualTo(2);
        assertThat(noticeBoards.getCurrentPage()).isEqualTo(1);
        assertThat(noticeBoards.getTotalPages()).isEqualTo(1);
        assertThat(noticeBoards.getPageSize()).isEqualTo(10);
        assertThat(noticeBoards.getContent()).hasSize(2)
                .extracting("title", "content", "category")
                .containsExactlyInAnyOrder(
                        tuple("title11", "content", "category1"),
                        tuple("title3", "content11", "category3")
                );
    }

    @Test
    @DisplayName("category가 있고, searchText가 있으면, 기간 내의 해당 카테고리의 검색어가 포함된 공지사항을 조회할 수 있다.")
    void getNoticeBoardsWithCategoryAndSearchText() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 26);
        LocalDate endDate = startDate.plusDays(1);
        String category = "category1";
        String searchText = "11";
        Pageable pageable = PageRequest.of(0, 10);

        noticeBoardRepository.save(buildNoticeBoardWith("title11", "content", "category1"));
        noticeBoardRepository.save(buildNoticeBoardWith("title2", "content2", "category2"));
        noticeBoardRepository.save(buildNoticeBoardWith("title3", "content11", "category3"));

        // when
        final CustomPage<NoticeBoardResponse> noticeBoards = noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable);

        // then
        assertThat(noticeBoards.getTotalElements()).isEqualTo(1);
        assertThat(noticeBoards.getCurrentPage()).isEqualTo(1);
        assertThat(noticeBoards.getTotalPages()).isEqualTo(1);
        assertThat(noticeBoards.getPageSize()).isEqualTo(10);
        assertThat(noticeBoards.getContent()).hasSize(1)
                .extracting("title", "content", "category")
                .containsExactlyInAnyOrder(
                        tuple("title11", "content", "category1")
                );
    }

    @Test
    @DisplayName("startDate가 null이면, IllegalArgumentException이 발생한다.")
    void getNoticeBoardsWithNullStartDate() {
        // given
        LocalDate startDate = null;
        LocalDate endDate = LocalDate.of(2024, 1, 26);
        String category = "";
        String searchText = "";
        Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작일은 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("endDate가 null이면, IllegalArgumentException이 발생한다.")
    void getNoticeBoardsWithNullEndDate() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 26);
        LocalDate endDate = null;
        String category = "";
        String searchText = "";
        Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료일은 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("startDate가 endDate보다 늦으면, IllegalArgumentException이 발생한다.")
    void getNoticeBoardsWithStartDateAfterEndDate() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 26);
        LocalDate endDate = startDate.minusDays(1);
        String category = "";
        String searchText = "";
        Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작일은 종료일보다 느릴 수 없습니다.");
    }

    @Test
    @DisplayName("category가 null이면, IllegalArgumentException이 발생한다.")
    void getNoticeBoardsWithNullCategory() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 26);
        LocalDate endDate = LocalDate.of(2024, 1, 26);
        String category = null;
        String searchText = "";
        Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("카테고리는 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("searchText가 null이면, IllegalArgumentException이 발생한다.")
    void getNoticeBoardsWithNullSearchText() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 26);
        LocalDate endDate = LocalDate.of(2024, 1, 26);
        String category = "";
        String searchText = null;
        Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("검색어는 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("pageable이 null이면, IllegalArgumentException이 발생한다.")
    void getNoticeBoardsWithNullPageable() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 26);
        LocalDate endDate = LocalDate.of(2024, 1, 26);
        String category = "";
        String searchText = "";
        Pageable pageable = null;

        // when,then
        assertThatThrownBy(() -> noticeBoardService.getNoticeBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("페이징 정보는 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("공지사항을 조회한다.")
    void getNoticeBoard() {
        // given
        NoticeBoardEntity noticeBoardEntity = NoticeBoardEntity.of(
                "title",
                "content",
                "category",
                true,
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                1L
        );
        noticeBoardRepository.save(noticeBoardEntity);

        // when
        final NoticeBoardResponse noticeBoard = noticeBoardService.getNoticeBoard(noticeBoardEntity.getId());

        // then
        assertThat(noticeBoard.getId()).isEqualTo(noticeBoardEntity.getId());
        assertThat(noticeBoard.getTitle()).isEqualTo(noticeBoardEntity.getTitle());
        assertThat(noticeBoard.getContent()).isEqualTo(noticeBoardEntity.getContent());
        assertThat(noticeBoard.getCategory()).isEqualTo(noticeBoardEntity.getCategory());
        assertThat(noticeBoard.getIsImportant()).isEqualTo(noticeBoardEntity.getIsImportant());
        assertThat(noticeBoard.getWriter()).isEqualTo(noticeBoardEntity.getWriter());
        assertThat(noticeBoard.getWrittenAt()).isEqualTo(noticeBoardEntity.getWrittenAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(noticeBoard.getViews()).isEqualTo(noticeBoardEntity.getViews());
    }

    @Test
    @DisplayName("공지사항을 조회할 때, 존재하지 않는 공지사항이면, EntityNotFoundException이 발생한다.")
    void getNoticeBoardWithNonExistNoticeBoard() {
        // given
        Long nonExistNoticeBoardId = 1L;

        // when,then
        assertThatThrownBy(() -> noticeBoardService.getNoticeBoard(nonExistNoticeBoardId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("공지사항을 등록한다.")
    void postNoticeBoard() {
        // given
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .content("content")
                .category("project")
                .isImportant(true)
                .build();

        // when
        final Long noticeBoardId = noticeBoardService.postNoticeBoard(postRequest, member);

        // then
        final NoticeBoardEntity noticeBoardEntity = noticeBoardRepository.findById(noticeBoardId).get();

        assertThat(noticeBoardEntity.getTitle()).isEqualTo("title");
        assertThat(noticeBoardEntity.getContent()).isEqualTo("content");
        assertThat(noticeBoardEntity.getCategory()).isEqualTo("project");
        assertThat(noticeBoardEntity.getIsImportant()).isEqualTo((Boolean) true);
        assertThat(noticeBoardEntity.getWriter()).isEqualTo(member.getEmail().split("@")[0]);
        assertThat(noticeBoardEntity.getViews()).isZero();
    }

    @Test
    @DisplayName("공지사항을 등록할 때, PostRequest가 null이면, IllegalArgumentException이 발생한다.")
    void postNoticeBoardWithNullPostRequest() {
        // given
        PostRequest postRequest = null;

        // when,then
        assertThatThrownBy(() -> noticeBoardService.postNoticeBoard(postRequest, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PostRequest는 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("공지사항을 등록할 때, PostRequest의 category가 존재하지 않으면, EntityNotFoundException이 발생한다.")
    void postNoticeBoardWithNonExistCategory() {
        // given
        PostRequest postRequest = PostRequest.builder()
                .title("title")
                .content("content")
                .category("nonExistCategory")
                .isImportant(true)
                .build();

        // when,then
        assertThatThrownBy(() -> noticeBoardService.postNoticeBoard(postRequest, member))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 분류가 존재하지 않습니다.");
    }
}
