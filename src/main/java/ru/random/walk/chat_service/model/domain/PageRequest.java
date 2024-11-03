package ru.random.walk.chat_service.model.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

public record PageRequest(
        int number,
        int size
) implements Pageable {
    public static PageRequest of(int number, int size) {
        return new PageRequest(number, size);
    }

    @Override
    public int getPageNumber() {
        return number;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return (long) number * size;
    }

    @Override
    @NonNull
    public Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    @NonNull
    public Pageable next() {
        return PageRequest.of(getPageNumber() + 1, getPageSize());
    }

    @Override
    @NonNull
    public Pageable previousOrFirst() {
        if (number == 0) {
            return PageRequest.of(number, size);
        }
        return PageRequest.of(number - 1, size);
    }

    @Override
    @NonNull
    public Pageable first() {
        return PageRequest.of(0, size);
    }

    @Override
    @NonNull
    public Pageable withPage(int pageNumber) {
        return PageRequest.of(pageNumber, size);
    }

    @Override
    public boolean hasPrevious() {
        return number > 0;
    }
}
