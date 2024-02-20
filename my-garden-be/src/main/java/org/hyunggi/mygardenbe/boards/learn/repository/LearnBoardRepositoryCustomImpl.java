package org.hyunggi.mygardenbe.boards.learn.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.hyunggi.mygardenbe.boards.learn.entity.LearnBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.hyunggi.mygardenbe.boards.learn.entity.QLearnBoardEntity.learnBoardEntity;

public class LearnBoardRepositoryCustomImpl implements LearnBoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public LearnBoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<LearnBoardEntity> searchLearnBoards(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText, final Pageable pageable) {
        final List<LearnBoardEntity> content = getContent(startDateTime, endDateTime, category, searchText, pageable);
        final JPAQuery<Long> countQuery = getJpaQuery(startDateTime, endDateTime, category, searchText);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private List<LearnBoardEntity> getContent(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText, final Pageable pageable) {
        final var query = queryFactory
                .selectFrom(learnBoardEntity)
                .where(
                        writtenAtBetween(startDateTime, endDateTime),
                        categoryEquals(category),
                        searchTextContains(searchText)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        addOrderBy(query, pageable);

        return query.fetch();
    }

    private JPAQuery<Long> getJpaQuery(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText) {
        return queryFactory
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

    private void addOrderBy(final JPAQuery<LearnBoardEntity> query, final Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(learnBoardEntity.getType(), learnBoardEntity.getMetadata());

            query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(order.getProperty())));
        }
    }
}
