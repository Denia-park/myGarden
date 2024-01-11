package org.hyunggi.mygardenbe.dailyroutine.domain;

import lombok.Getter;

@Getter
public enum RoutineType {
    STUDY("공부"),
    REST("휴식"),
    ETC("기타"),
    EXERCISE("운동"),
    SLEEP("수면"),
    MEAL("식사"),
    GAME("게임");

    private String description;

    RoutineType(final String description) {
        this.description = description;
    }
}
