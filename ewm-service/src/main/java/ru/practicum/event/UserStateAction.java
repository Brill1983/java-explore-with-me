package ru.practicum.event;

import java.util.Optional;

public enum UserStateAction {
    SEND_TO_REVIEW,
    CANCEL_REVIEW;

    public static Optional<UserStateAction> from(String stateAction) {
        for (UserStateAction state : UserStateAction.values()) {
            if (state.name().equalsIgnoreCase(stateAction)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
