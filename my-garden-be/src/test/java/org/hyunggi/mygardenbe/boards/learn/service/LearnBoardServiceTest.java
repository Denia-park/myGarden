package org.hyunggi.mygardenbe.boards.learn.service;

import jakarta.persistence.EntityNotFoundException;
import org.hyunggi.mygardenbe.IntegrationTestSupport;
import org.hyunggi.mygardenbe.boards.common.category.entity.BoardCategoryEntity;
import org.hyunggi.mygardenbe.boards.common.category.repository.BoardCategoryRepository;
import org.hyunggi.mygardenbe.boards.common.response.CustomPage;
import org.hyunggi.mygardenbe.boards.learn.controller.request.PostRequest;
import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.hyunggi.mygardenbe.boards.learn.repository.LearnBoardRepository;
import org.hyunggi.mygardenbe.boards.learn.service.response.LearnBoardResponse;
import org.hyunggi.mygardenbe.member.domain.Member;
import org.hyunggi.mygardenbe.member.domain.Role;
import org.hyunggi.mygardenbe.member.entity.MemberEntity;
import org.hyunggi.mygardenbe.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class LearnBoardServiceTest extends IntegrationTestSupport {
    @Autowired
    LearnBoardRepository learnBoardRepository;
    @Autowired
    LearnBoardService learnBoardService;
    @Autowired
    BoardCategoryRepository boardCategoryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MemberEntity member;
    private MemberEntity anotherMember;

    @BeforeEach
    void setUpEntity() {
        final Member memberDomain = new Member("test@test.com", "test1234!", Role.ADMIN, true);
        member = memberRepository.save(MemberEntity.of(memberDomain, passwordEncoder));

        final Member anotherMemberDomain = new Member("test2@test.com", "test1234!", Role.ADMIN, true);
        anotherMember = memberRepository.save(MemberEntity.of(anotherMemberDomain, passwordEncoder));

        final BoardCategoryEntity boardCategoryEntity = new BoardCategoryEntity("project", "프로젝트", "learn");
        boardCategoryRepository.save(boardCategoryEntity);
    }

    @Test
    @DisplayName("category 및 searchText 없이 조회를 하면, 기간 내의 모든 TIL을 조회할 수 있다.")
    void getLearnBoardsWithoutCategoryAndSearchText() {
        // given
        final LocalDate startDate = LocalDate.of(2024, 1, 26);
        final LocalDate endDate = startDate.plusDays(1);
        final String category = "";
        final String searchText = "";
        final Pageable pageable = PageRequest.of(0, 10);

        learnBoardRepository.save(buildLearnBoardWith("title1", "content1", "category1"));
        learnBoardRepository.save(buildLearnBoardWith("title2", "content2", "category2"));
        learnBoardRepository.save(buildLearnBoardWith("title3", "content3", "category3"));

        // when
        final CustomPage<LearnBoardResponse> learnBoards = learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable);

        // then
        assertThat(learnBoards.getTotalElements()).isEqualTo(3);
        assertThat(learnBoards.getCurrentPage()).isEqualTo(1);
        assertThat(learnBoards.getTotalPages()).isEqualTo(1);
        assertThat(learnBoards.getPageSize()).isEqualTo(10);
        assertThat(learnBoards.getContent()).hasSize(3)
                .extracting("title", "content", "category")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1", "category1"),
                        tuple("title2", "content2", "category2"),
                        tuple("title3", "content3", "category3")
                );
    }

    private LearnBoardEntity buildLearnBoardWith(final String title, final String content, final String category) {
        return LearnBoardEntity.of(
                title,
                content,
                category,
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                1L
        );
    }

    @Test
    @DisplayName("category가 있고, searchText가 없으면, 기간 내의 해당 분류의 TIL을 조회할 수 있다.")
    void getLearnBoardsWithCategory() {
        // given
        final LocalDate startDate = LocalDate.of(2024, 1, 26);
        final LocalDate endDate = startDate.plusDays(1);
        final String category = "category1";
        final String searchText = "";
        final Pageable pageable = PageRequest.of(0, 10);

        learnBoardRepository.save(buildLearnBoardWith("title1", "content1", "category1"));
        learnBoardRepository.save(buildLearnBoardWith("title2", "content2", "category2"));
        learnBoardRepository.save(buildLearnBoardWith("title3", "content3", "category3"));

        // when
        final CustomPage<LearnBoardResponse> learnBoards = learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable);

        // then
        assertThat(learnBoards.getTotalElements()).isEqualTo(1);
        assertThat(learnBoards.getCurrentPage()).isEqualTo(1);
        assertThat(learnBoards.getTotalPages()).isEqualTo(1);
        assertThat(learnBoards.getPageSize()).isEqualTo(10);
        assertThat(learnBoards.getContent()).hasSize(1)
                .extracting("title", "content", "category")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1", "category1")
                );
    }

    @Test
    @DisplayName("category가 없고, searchText가 있으면, 기간 내의 해당 검색어가 포함된 TIL을 조회할 수 있다.")
    void getLearnBoardsWithSearchText() {
        // given
        final LocalDate startDate = LocalDate.of(2024, 1, 26);
        final LocalDate endDate = startDate.plusDays(1);
        final String category = "";
        final String searchText = "11";
        final Pageable pageable = PageRequest.of(0, 10);

        learnBoardRepository.save(buildLearnBoardWith("title11", "content", "category1"));
        learnBoardRepository.save(buildLearnBoardWith("title2", "content2", "category2"));
        learnBoardRepository.save(buildLearnBoardWith("title3", "content11", "category3"));

        // when
        final CustomPage<LearnBoardResponse> learnBoards = learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable);

        // then
        assertThat(learnBoards.getTotalElements()).isEqualTo(2);
        assertThat(learnBoards.getCurrentPage()).isEqualTo(1);
        assertThat(learnBoards.getTotalPages()).isEqualTo(1);
        assertThat(learnBoards.getPageSize()).isEqualTo(10);
        assertThat(learnBoards.getContent()).hasSize(2)
                .extracting("title", "content", "category")
                .containsExactlyInAnyOrder(
                        tuple("title11", "content", "category1"),
                        tuple("title3", "content11", "category3")
                );
    }

    @Test
    @DisplayName("category가 있고, searchText가 있으면, 기간 내의 해당 분류의 검색어가 포함된 TIL을 조회할 수 있다.")
    void getLearnBoardsWithCategoryAndSearchText() {
        // given
        final LocalDate startDate = LocalDate.of(2024, 1, 26);
        final LocalDate endDate = startDate.plusDays(1);
        final String category = "category1";
        final String searchText = "11";
        final Pageable pageable = PageRequest.of(0, 10);

        learnBoardRepository.save(buildLearnBoardWith("title11", "content", "category1"));
        learnBoardRepository.save(buildLearnBoardWith("title2", "content2", "category2"));
        learnBoardRepository.save(buildLearnBoardWith("title3", "content11", "category3"));

        // when
        final CustomPage<LearnBoardResponse> learnBoards = learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable);

        // then
        assertThat(learnBoards.getTotalElements()).isEqualTo(1);
        assertThat(learnBoards.getCurrentPage()).isEqualTo(1);
        assertThat(learnBoards.getTotalPages()).isEqualTo(1);
        assertThat(learnBoards.getPageSize()).isEqualTo(10);
        assertThat(learnBoards.getContent()).hasSize(1)
                .extracting("title", "content", "category")
                .containsExactlyInAnyOrder(
                        tuple("title11", "content", "category1")
                );
    }

    @Test
    @DisplayName("startDate가 null이면, IllegalArgumentException이 발생한다.")
    void getLearnBoardsWithNullStartDate() {
        // given
        final LocalDate startDate = null;
        final LocalDate endDate = LocalDate.of(2024, 1, 26);
        final String category = "";
        final String searchText = "";
        final Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작일은 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("endDate가 null이면, IllegalArgumentException이 발생한다.")
    void getLearnBoardsWithNullEndDate() {
        // given
        final LocalDate startDate = LocalDate.of(2024, 1, 26);
        final LocalDate endDate = null;
        final String category = "";
        final String searchText = "";
        final Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료일은 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("startDate가 endDate보다 늦으면, IllegalArgumentException이 발생한다.")
    void getLearnBoardsWithStartDateAfterEndDate() {
        // given
        final LocalDate startDate = LocalDate.of(2024, 1, 26);
        final LocalDate endDate = startDate.minusDays(1);
        final String category = "";
        final String searchText = "";
        final Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작일은 종료일보다 느릴 수 없습니다.");
    }

    @Test
    @DisplayName("category가 null이면, IllegalArgumentException이 발생한다.")
    void getLearnBoardsWithNullCategory() {
        // given
        final LocalDate startDate = LocalDate.of(2024, 1, 26);
        final LocalDate endDate = LocalDate.of(2024, 1, 26);
        final String category = null;
        final String searchText = "";
        final Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("분류는 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("searchText가 null이면, IllegalArgumentException이 발생한다.")
    void getLearnBoardsWithNullSearchText() {
        // given
        final LocalDate startDate = LocalDate.of(2024, 1, 26);
        final LocalDate endDate = LocalDate.of(2024, 1, 26);
        final String category = "";
        final String searchText = null;
        final Pageable pageable = PageRequest.of(0, 10);

        // when,then
        assertThatThrownBy(() -> learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("검색어는 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("pageable이 null이면, IllegalArgumentException이 발생한다.")
    void getLearnBoardsWithNullPageable() {
        // given
        final LocalDate startDate = LocalDate.of(2024, 1, 26);
        final LocalDate endDate = LocalDate.of(2024, 1, 26);
        final String category = "";
        final String searchText = "";
        final Pageable pageable = null;

        // when,then
        assertThatThrownBy(() -> learnBoardService.getLearnBoards(startDate, endDate, category, searchText, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("페이징 정보는 null이 될 수 없습니다.");
    }

    @Test
    @DisplayName("TIL을 조회한다.")
    void getLearnBoard() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                1L
        );
        learnBoardRepository.save(learnBoardEntity);

        // when
        final LearnBoardResponse learnBoard = learnBoardService.getLearnBoard(learnBoardEntity.getId());

        // then
        assertThat(learnBoard.getId()).isEqualTo(learnBoardEntity.getId());
        assertThat(learnBoard.getTitle()).isEqualTo(learnBoardEntity.getTitle());
        assertThat(learnBoard.getContent()).isEqualTo(learnBoardEntity.getContent());
        assertThat(learnBoard.getCategory()).isEqualTo(learnBoardEntity.getCategory());
        assertThat(learnBoard.getWriter()).isEqualTo(learnBoardEntity.getWriter());
        assertThat(learnBoard.getWrittenAt()).isEqualTo(learnBoardEntity.getWrittenAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(learnBoard.getViews()).isEqualTo(learnBoardEntity.getViews());
    }

    @Test
    @DisplayName("TIL을 조회하면, 조회 수가 1 증가한다.")
    void getLearnBoardWithIncreaseViewCount() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                1L
        );
        learnBoardRepository.save(learnBoardEntity);
        int beforeViews = learnBoardEntity.getViews();

        // when
        final LearnBoardResponse learnBoard = learnBoardService.getLearnBoard(learnBoardEntity.getId());

        // then
        assertThat(learnBoard.getViews()).isEqualTo(beforeViews + 1);
    }

    @ParameterizedTest
    @CsvSource(value = {"0", "-1", ","})
    @DisplayName("TIL을 조회할 때, boardId가 null혹은 0보다 작으면, IllegalArgumentException이 발생한다.")
    void getLearnBoardWithNullBoardId(final Long boardId) {
        // when,then
        assertThatThrownBy(() -> learnBoardService.getLearnBoard(boardId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("boardId는 null이 될 수 없고 0보다 커야합니다.");
    }

    @Test
    @DisplayName("TIL을 조회할 때, 존재하지 않는 TIL이면, EntityNotFoundException이 발생한다.")
    void getLearnBoardWithNonExistLearnBoard() {
        // given
        final Long nonExistLearnBoardId = 1L;

        // when,then
        assertThatThrownBy(() -> learnBoardService.getLearnBoard(nonExistLearnBoardId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("TIL을 등록한다.")
    void postLearnBoard() {
        // given
        final PostRequest postRequest = PostRequest.builder()
                .title("title")
                .content("content")
                .category("project")
                .build();

        // when
        final Long learnBoardId = learnBoardService.postLearnBoard(postRequest.category(), postRequest.title(), postRequest.content(), member);

        // then
        final LearnBoardEntity learnBoardEntity = learnBoardRepository.findById(learnBoardId).get();

        assertThat(learnBoardEntity.getTitle()).isEqualTo("title");
        assertThat(learnBoardEntity.getContent()).isEqualTo("content");
        assertThat(learnBoardEntity.getCategory()).isEqualTo("project");
        assertThat(learnBoardEntity.getWriter()).isEqualTo(member.getEmail().split("@")[0]);
        assertThat(learnBoardEntity.getViews()).isZero();
    }

    @Test
    @DisplayName("TIL을 등록할 때, PostRequest의 category가 존재하지 않으면, EntityNotFoundException이 발생한다.")
    void postLearnBoardWithNonExistCategory() {
        // given
        final PostRequest postRequest = PostRequest.builder()
                .title("title")
                .content("content")
                .category("nonExistCategory")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.postLearnBoard(postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 분류가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("TIL을 등록할 때, PostRequest의 title이 null이면, IllegalArgumentException이 발생한다.")
    void postLearnBoardWithNullTitle() {
        // given
        final PostRequest postRequest = PostRequest.builder()
                .title(null)
                .content("content")
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.postLearnBoard(postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("TIL을 등록할 때, PostRequest의 title이 100자를 넘으면, IllegalArgumentException이 발생한다.")
    void postLearnBoardWithOver100Title() {
        // given
        final PostRequest postRequest = PostRequest.builder()
                .title("a".repeat(101))
                .content("content")
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.postLearnBoard(postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 100자를 넘을 수 없습니다.");
    }

    @Test
    @DisplayName("TIL을 등록할 때, PostRequest의 content가 null이면, IllegalArgumentException이 발생한다.")
    void postLearnBoardWithNullContent() {
        // given
        final PostRequest postRequest = PostRequest.builder()
                .title("title")
                .content(null)
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.postLearnBoard(postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 내용은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("TIL을 등록할 때, PostRequest의 content가 4000자를 넘으면, IllegalArgumentException이 발생한다.")
    void postLearnBoardWithOver4000Content() {
        // given
        final PostRequest postRequest = PostRequest.builder()
                .title("title")
                .content("a".repeat(4001))
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.postLearnBoard(postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 내용은 4000자를 넘을 수 없습니다.");
    }

    @Test
    @DisplayName("TIL을 수정한다.")
    void putLearnBoard() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                member.getId()
        );
        learnBoardRepository.save(learnBoardEntity);

        final PostRequest postRequest = PostRequest.builder()
                .title("title2")
                .content("content2")
                .category("project")
                .build();

        // when
        final Long learnBoardId = learnBoardService.putLearnBoard(learnBoardEntity.getId(), postRequest.category(), postRequest.title(), postRequest.content(), member);

        // then
        final LearnBoardEntity updatedLearnBoardEntity = learnBoardRepository.findById(learnBoardId).get();

        assertThat(updatedLearnBoardEntity.getTitle()).isEqualTo("title2");
        assertThat(updatedLearnBoardEntity.getContent()).isEqualTo("content2");
        assertThat(updatedLearnBoardEntity.getCategory()).isEqualTo("project");
    }

    @Test
    @DisplayName("TIL을 수정할 때, boardId가 null이면, IllegalArgumentException이 발생한다.")
    void putLearnBoardWithNullBoardId() {
        // given
        final Long boardId = null;
        final PostRequest postRequest = PostRequest.builder()
                .title("title2")
                .content("content2")
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.putLearnBoard(boardId, postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("boardId는 null이 될 수 없고 0보다 커야합니다.");
    }

    @Test
    @DisplayName("TIL을 수정할 때, boardId가 0보다 작으면, IllegalArgumentException이 발생한다.")
    void putLearnBoardWithNegativeBoardId() {
        // given
        final Long boardId = -1L;
        final PostRequest postRequest = PostRequest.builder()
                .title("title2")
                .content("content2")
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.putLearnBoard(boardId, postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("boardId는 null이 될 수 없고 0보다 커야합니다.");
    }

    @Test
    @DisplayName("TIL을 수정할 때, PostRequest의 title이 null이면, IllegalArgumentException이 발생한다.")
    void putLearnBoardWithNullTitle() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                member.getId()
        );
        learnBoardRepository.save(learnBoardEntity);

        final PostRequest postRequest = PostRequest.builder()
                .title(null)
                .content("content2")
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.putLearnBoard(learnBoardEntity.getId(), postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("TIL을 수정할 때, PostRequest의 title이 100자를 넘으면, IllegalArgumentException이 발생한다.")
    void putLearnBoardWithOver100Title() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                member.getId()
        );
        learnBoardRepository.save(learnBoardEntity);

        final PostRequest postRequest = PostRequest.builder()
                .title("a".repeat(101))
                .content("content2")
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.putLearnBoard(learnBoardEntity.getId(), postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 100자를 넘을 수 없습니다.");
    }

    @Test
    @DisplayName("TIL을 수정할 때, PostRequest의 content가 null이면, IllegalArgumentException이 발생한다.")
    void putLearnBoardWithNullContent() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                member.getId()
        );
        learnBoardRepository.save(learnBoardEntity);

        final PostRequest postRequest = PostRequest.builder()
                .title("title2")
                .content(null)
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.putLearnBoard(learnBoardEntity.getId(), postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 내용은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("TIL을 수정할 때, PostRequest의 content가 4000자를 넘으면, IllegalArgumentException이 발생한다.")
    void putLearnBoardWithOver4000Content() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                member.getId()
        );
        learnBoardRepository.save(learnBoardEntity);

        final PostRequest postRequest = PostRequest.builder()
                .title("title2")
                .content("a".repeat(4001))
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.putLearnBoard(learnBoardEntity.getId(), postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 내용은 4000자를 넘을 수 없습니다.");
    }

    @Test
    @DisplayName("TIL을 수정할 때, PostRequest의 category가 존재하지 않으면, EntityNotFoundException이 발생한다.")
    void putLearnBoardWithNonExistCategory() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                member.getId()
        );
        learnBoardRepository.save(learnBoardEntity);

        final PostRequest postRequest = PostRequest.builder()
                .title("title2")
                .content("content2")
                .category("nonExistCategory")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.putLearnBoard(learnBoardEntity.getId(), postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 분류가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("TIL을 수정할 때, 존재하지 않는 TIL이면, EntityNotFoundException이 발생한다.")
    void putLearnBoardWithNonExistLearnBoard() {
        // given
        final Long nonExistLearnBoardId = 1L;
        final PostRequest postRequest = PostRequest.builder()
                .title("title2")
                .content("content2")
                .category("project")
                .build();

        // when,then
        assertThatThrownBy(() -> learnBoardService.putLearnBoard(nonExistLearnBoardId, postRequest.category(), postRequest.title(), postRequest.content(), member))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("TIL을 수정할 때, 작성자가 아니면, IllegalArgumentException이 발생한다.")
    void putLearnBoardWithNotWriter() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                member.getId()
        );
        learnBoardRepository.save(learnBoardEntity);

        final PostRequest postRequest = PostRequest.builder()
                .title("title2")
                .content("content2")
                .category("project")
                .build();

        // when, then
        assertThatThrownBy(() -> learnBoardService.putLearnBoard(learnBoardEntity.getId(), postRequest.category(), postRequest.title(), postRequest.content(), anotherMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시글의 작성자가 아닙니다.");
    }

    @Test
    @DisplayName("TIL을 삭제한다.")
    void deleteLearnBoard() {
        // given
        final LearnBoardEntity learnBoardEntity = LearnBoardEntity.of(
                "title",
                "content",
                "category",
                "writer",
                LocalDateTime.of(2024, 1, 27, 12, 0, 0),
                member.getId()
        );
        learnBoardRepository.save(learnBoardEntity);

        // when
        final Long learnBoardId = learnBoardService.deleteLearnBoard(learnBoardEntity.getId(), member);

        // then
        assertThat(learnBoardRepository.findById(learnBoardId)).isEmpty();
    }
}
