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

public class NoticeBoardRepositoryCustomImpl extends Querydsl4RepositorySupport implements NoticeBoardRepositoryCustom {
    public NoticeBoardRepositoryCustomImpl() {
        super(NoticeBoardEntity.class);
    }

    @Override
    public Page<NoticeBoardEntity> searchNoticeBoards(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText, final Pageable pageable) {
        return applyPagination(
                pageable,
                getContentQuery(startDateTime, endDateTime, category, searchText),
                getCountQuery(startDateTime, endDateTime, category, searchText)
        );
    }

    private Function<JPAQueryFactory, JPAQuery> getContentQuery(final LocalDateTime startDateTime, final LocalDateTime endDateTime, final String category, final String searchText) {
        return queryFactory -> queryFactory
                .selectFrom(noticeBoardEntity)
                .where(
                        writtenAtBetween(startDateTime, endDateTime),
                        categoryEquals(category),
                        searchTextContains(searchText),
                        isNotImportant()
                );
    }

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

    private BooleanExpression writtenAtBetween(final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return noticeBoardEntity.writtenAt.between(startDateTime, endDateTime);
    }

    private BooleanExpression categoryEquals(final String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }

        return noticeBoardEntity.category.eq(category);
    }

    private BooleanExpression searchTextContains(final String searchText) {
        if (!StringUtils.hasText(searchText)) {
            return null;
        }

        return noticeBoardEntity.title.contains(searchText).or(noticeBoardEntity.content.contains(searchText));
    }

    private BooleanExpression isNotImportant() {
        return noticeBoardEntity.isImportant.eq(false);
    }
}
