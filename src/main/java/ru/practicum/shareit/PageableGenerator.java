package ru.practicum.shareit;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class PageableGenerator {
    public static Pageable getPageable(int from, int size) {
        return PageRequest.of(from == 0 ? 0 : from / size, size);
    }
}
