package org.hyunggi.mygardenbe.boards.common.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;

/**
 * 게시판 분류 Entity
 */
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class BoardCategoryEntity extends BaseEntity {
    /**
     * 분류 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 분류 코드
     */
    @Column(nullable = false, length = 30)
    private String code;

    /**
     * 분류 명
     */
    @Column(nullable = false, length = 30)
    private String text;

    /**
     * 게시판 타입
     */
    @Column(nullable = false, length = 30)
    private String boardType;

    public BoardCategoryEntity(String code, String text, String boardType) {
        validateConstructor(code, text, boardType);

        this.code = code;
        this.text = text;
        this.boardType = boardType;
    }

    /**
     * 생성자 유효성 검사
     *
     * @param code      분류 코드
     * @param text      분류 명
     * @param boardType 게시판 타입
     */
    private void validateConstructor(final String code, final String text, final String boardType) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code는 null이거나 비어있을 수 없습니다.");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("text는 null이거나 비어있을 수 없습니다.");
        }
        if (boardType == null || boardType.isBlank()) {
            throw new IllegalArgumentException("boardType은 null이거나 비어있을 수 없습니다.");
        }
    }
}
