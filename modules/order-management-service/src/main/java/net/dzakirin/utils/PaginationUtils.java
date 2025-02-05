package net.dzakirin.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PaginationUtils {

    public static Pageable getPageRequest(int page, int size, String[] sort) {
        return PageRequest.of(page - 1, size, Sort.by(sort).descending());
    }
}
