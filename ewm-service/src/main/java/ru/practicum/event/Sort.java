package ru.practicum.event;

import java.util.Optional;

public enum Sort {
    EVENT_DATE,
    VIEWS;

    public static Optional<UserStateAction> from(String stateAction) {
        for (UserStateAction state : UserStateAction.values()) {
            if (state.name().equalsIgnoreCase(stateAction)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
