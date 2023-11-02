package ru.practicum.comment;

import java.util.Optional;

public enum CommentSort {

    DESC,
    ASC;

    public static Optional<CommentSort> from(String sortString) {
        for (CommentSort sort : CommentSort.values()) {
            if (sort.name().equalsIgnoreCase(sortString)) {
                return Optional.of(sort);
            }
        }
        return Optional.empty();
    }

}
