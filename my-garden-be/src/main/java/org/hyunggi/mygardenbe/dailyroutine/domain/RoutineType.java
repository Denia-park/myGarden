package org.hyunggi.mygardenbe.dailyroutine.domain;

public enum RoutineType {
    EXERCISE("운동"),
    SLEEP("수면"),
    EAT("식사"),
    STUDY("공부"),
    REST("휴식"),
    GAME("게임"),
    ETC("기타");

    private String description;

    RoutineType(String description) {
        this.description = description;
    }
}
