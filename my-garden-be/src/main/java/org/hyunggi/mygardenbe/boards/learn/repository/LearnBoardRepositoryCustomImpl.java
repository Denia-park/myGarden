package org.hyunggi.mygardenbe.boards.learn.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.hyunggi.mygardenbe.common.querydsl.support.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.function.Function;

import static org.hyunggi.mygardenbe.boards.learn.entity.QLearnBoardEntity.learnBoardEntity;

/**
 * TIL 게시판 Entity Repository Custom 구현체
 */
public class LearnBoardRepositoryCustomImpl extends Querydsl4RepositorySupport implements LearnBoardRepositoryCustom {
    public LearnBoardRepositoryCustomImpl() {
        super(LearnBoardEntity.class);
    }

    /**
     * TIL 게시글 Entity 검색
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @param pageable      페이징
     * @return 게시글 목록 (페이지)
     */
    @Override
    public Page<LearnBoardEntity> searchLearnBoards(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText, final Pageable pageable) {
        return applyPagination(
                pageable,
                getContentQuery(startDateTime, endDateTime, category, searchText),
                getCountQuery(startDateTime, endDateTime, category, searchText)
        );
    }

    /**
     * 조건에 맞는 TIL 게시글 조회 쿼리
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @return 조건에 맞는 TIL 게시글 조회 쿼리
     */
    private Function<JPAQueryFactory, JPAQuery> getContentQuery(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText) {
        return queryFactory -> queryFactory
                .selectFrom(learnBoardEntity)
                .join(learnBoardEntity.category).fetchJoin()
                .where(
                        writtenAtBetween(startDateTime, endDateTime),
                        categoryEquals(category),
                        searchTextContains(searchText)
                );
    }

    /**
     * 조건에 맞는 TIL 게시글 개수 조회 쿼리
     *
     * @param startDateTime 조회 시작일
     * @param endDateTime   조회 종료일
     * @param category      조회할 분류
     * @param searchText    검색어
     * @return 조건에 맞는 TIL 게시글 개수
     */
    private Function<JPAQueryFactory, JPAQuery<Long>> getCountQuery(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText) {
        return queryFactory -> queryFactory
                .select(learnBoardEntity.count())
                .from(learnBoardEntity)
                .where(
                        writtenAtBetween(startDateTime, endDateTime),
                        categoryEquals(category),
                        searchTextContains(searchText)
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
        return learnBoardEntity.writtenAt.between(startDateTime, endDateTime);
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

        return learnBoardEntity.category.code.eq(category);
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

        return learnBoardEntity.title.contains(searchText).or(learnBoardEntity.content.contains(searchText));
    }
}
