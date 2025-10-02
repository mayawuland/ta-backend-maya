package com.indomarco.indostore.utility;

import java.util.Collections;
import java.util.List;

/**
 * Utility class for handling pagination of lists.
 */
public class PaginationUtils {
    /**
     * Returns a paginated sublist of the given list.
     *
     * @param list The original list to paginate.
     * @param page The page number (0-based).
     * @param size The number of items per page.
     * @param <T>  The type of elements in the list.
     * @return A sublist containing the items for the requested page.
     *         Returns an empty list if the page is out of bounds.
     */
    public static <T> List<T> paginate(List<T> list, int page, int size) {
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, list.size());
        if (fromIndex >= list.size()) return Collections.emptyList();
        return list.subList(fromIndex, toIndex);
    }
}

