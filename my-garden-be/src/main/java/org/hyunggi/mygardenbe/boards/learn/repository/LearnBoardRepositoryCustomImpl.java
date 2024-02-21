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

public class LearnBoardRepositoryCustomImpl extends Querydsl4RepositorySupport implements LearnBoardRepositoryCustom {
    public LearnBoardRepositoryCustomImpl() {
        super(LearnBoardEntity.class);
    }

    @Override
    public Page<LearnBoardEntity> searchLearnBoards(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText, final Pageable pageable) {
        return applyPagination(
                pageable,
                getContentQuery(startDateTime, endDateTime, category, searchText),
                getCountQuery(startDateTime, endDateTime, category, searchText)
        );
    }

    private Function<JPAQueryFactory, JPAQuery> getContentQuery(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText) {
        return queryFactory -> queryFactory
                .selectFrom(learnBoardEntity)
                .where(
                        writtenAtBetween(startDateTime, endDateTime),
                        categoryEquals(category),
                        searchTextContains(searchText)
                );
    }

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

    private BooleanExpression writtenAtBetween(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return learnBoardEntity.writtenAt.between(startDateTime, endDateTime);
    }

    private BooleanExpression categoryEquals(final String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }

        return learnBoardEntity.category.eq(category);
    }

    private BooleanExpression searchTextContains(final String searchText) {
        if (!StringUtils.hasText(searchText)) {
            return null;
        }

        return learnBoardEntity.title.contains(searchText).or(learnBoardEntity.content.contains(searchText));
    }
}
