package ru.practicum.request;

import java.util.Optional;

public enum Status {

    PENDING,
    CONFIRMED,
    REJECTED;

    public static Optional<Status> from(String stringStatus) {
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(stringStatus)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
