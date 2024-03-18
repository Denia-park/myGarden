package org.hyunggi.mygardenbe.boards.notice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hyunggi.mygardenbe.boards.notice.entity.NoticeBoardEntity;
import org.hyunggi.mygardenbe.common.querydsl.support.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.function.Function;

import static org.hyunggi.mygardenbe.boards.notice.entity.QNoticeBoardEntity.noticeBoardEntity;

/**
 * 공지사항 게시판 Repository Custom 구현체
 */
public class NoticeBoardRepositoryCustomImpl extends Querydsl4RepositorySupport implements NoticeBoardRepositoryCustom {
    public NoticeBoardRepositoryCustomImpl() {
        super(NoticeBoardEntity.class);
    }

    /**
     * 공지사항 게시판 목록 조회 (중요 여부 X인 공지사항)
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @param pageable      페이징
     * @return 공지사항 게시판 목록 (페이지)
     */
    @Override
    public Page<NoticeBoardEntity> searchNoticeBoards(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText, final Pageable pageable) {
        return applyPagination(
                pageable,
                getContentQuery(startDateTime, endDateTime, category, searchText),
                getCountQuery(startDateTime, endDateTime, category, searchText)
        );
    }

    /**
     * 공지사항 게시판 목록 조회 쿼리 (중요 여부 X인 공지사항)
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @return 공지사항 게시판 목록
     */
    private Function<JPAQueryFactory, JPAQuery> getContentQuery(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText) {
        return queryFactory -> queryFactory
                .selectFrom(noticeBoardEntity)
                .join(noticeBoardEntity.category).fetchJoin()
                .where(
                        writtenAtBetween(startDateTime, endDateTime),
                        categoryEquals(category),
                        searchTextContains(searchText),
                        isNotImportant()
                );
    }

    /**
     * 공지사항 게시글 수 조회 쿼리
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @return 공지사항 게시글 수
     */
    private Function<JPAQueryFactory, JPAQuery<Long>> getCountQuery(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText) {
        return queryFactory -> queryFactory
                .select(noticeBoardEntity.count())
                .from(noticeBoardEntity)
                .where(
                        writtenAtBetween(startDateTime, endDateTime),
                        categoryEquals(category),
                        searchTextContains(searchText),
                        isNotImportant()
                );
    }

    /**
     * 작성일 범위 조건
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @return 작성일 범위 조건
     */
    private BooleanExpression writtenAtBetween(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return noticeBoardEntity.writtenAt.between(startDateTime, endDateTime);
    }

    /**
     * 분류 조건
     *
     * @param category 조회할 분류
     * @return 분류 조건
     */
    private BooleanExpression categoryEquals(final String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }

        return noticeBoardEntity.category.eq(category);
    }

    /**
     * 검색어 포함 조건
     *
     * @param searchText 검색어
     * @return 검색어 포함 조건
     */
    private BooleanExpression searchTextContains(final String searchText) {
        if (!StringUtils.hasText(searchText)) {
            return null;
        }

        return noticeBoardEntity.title.contains(searchText).or(noticeBoardEntity.content.contains(searchText));
    }

    /**
     * 중요 여부 조건
     *
     * @return 중요 여부 조건
     */
    private BooleanExpression isNotImportant() {
        return noticeBoardEntity.isImportant.eq(false);
    }
}
