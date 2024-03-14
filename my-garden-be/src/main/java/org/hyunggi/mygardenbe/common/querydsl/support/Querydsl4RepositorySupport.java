package org.hyunggi.mygardenbe.common.querydsl.support;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;

/**
 * Querydsl 4.x 버전에 맞춘 Querydsl 지원 라이브러리
 *
 * @author Younghan Kim
 * @see org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
 */
@Repository
public abstract class Querydsl4RepositorySupport {
    private final Class domainClass;
    private Querydsl querydsl;
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    protected Querydsl4RepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null!");
        this.domainClass = domainClass;
    }

    /**
     * Setter injection이 제대로 되었는지 검증
     */
    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }

    /**
     * JPAQueryFactory를 반환
     *
     * @return JPAQueryFactory
     */
    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

    /**
     * Querydsl을 반환
     *
     * @return Querydsl
     */
    protected Querydsl getQuerydsl() {
        return querydsl;
    }

    /**
     * EntityManager를 반환
     *
     * @return EntityManager
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * EntityManager를 주입하고, Querydsl과 JPAQueryFactory를 초기화
     */
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!");

        JpaEntityInformation entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath path = resolver.createPath(entityInformation.getJavaType());

        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * JPAQueryFactory를 이용하여 JPAQuery의 select 쿼리를 생성
     *
     * @param expr select할 Expression
     * @param <T>  반환 타입
     * @return JPAQuery
     */
    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return getQueryFactory().select(expr);
    }

    /**
     * JPAQueryFactory를 이용하여 JPAQuery의 selectFrom 쿼리를 생성
     *
     * @param from select할 EntityPath
     * @param <T>  반환 타입
     * @return JPAQuery
     */
    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return getQueryFactory().selectFrom(from);
    }

    /**
     * Querydsl을 이용하여 페이징 처리
     *
     * @param pageable     페이징 정보
     * @param contentQuery 콘텐츠 조회 쿼리
     * @param countQuery   콘텐츠 개수 조회 쿼리
     * @param <T>          반환 타입
     * @return 페이징 처리된 결과
     */
    protected <T> Page<T> applyPagination(Pageable pageable,
                                          Function<JPAQueryFactory, JPAQuery> contentQuery,
                                          Function<JPAQueryFactory, JPAQuery<Long>> countQuery) {
        JPAQuery jpaContentQuery = contentQuery.apply(getQueryFactory());
        List<T> content = getQuerydsl().applyPagination(pageable, jpaContentQuery).fetch();
        JPAQuery<Long> countResult = countQuery.apply(getQueryFactory());

        return PageableExecutionUtils.getPage(content, pageable, countResult::fetchOne);
    }
}
