package ru.practicum.event;

import java.util.Optional;

public enum Sort {
    EVENT_DATE,
    VIEWS;

    public static Optional<Sort> from(String stateAction) {
        for (Sort state : Sort.values()) {
            if (state.name().equalsIgnoreCase(stateAction)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
