package org.hyunggi.mygardenbe.boards.common.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hyunggi.mygardenbe.common.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class BoardCategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String code;
    @Column(nullable = false, length = 30)
    private String text;
    @Column(nullable = false, length = 30)
    private String boardType;

    public BoardCategoryEntity(String code, String text, String boardType) {
        validateConstructor(code, text, boardType);

        this.code = code;
        this.text = text;
        this.boardType = boardType;
    }

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
