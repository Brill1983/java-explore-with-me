package ru.practicum.event;

import java.util.Optional;

public enum AdminStateAction {
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static Optional<AdminStateAction> from(String stateAction) {
        for (AdminStateAction state : AdminStateAction.values()) {
            if (state.name().equalsIgnoreCase(stateAction)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
