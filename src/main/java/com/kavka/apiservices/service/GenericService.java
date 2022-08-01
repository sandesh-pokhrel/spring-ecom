package com.kavka.apiservices.service;

import com.kavka.apiservices.common.Constants;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.Objects;

public abstract class GenericService {

    private static final String PAGE = "page";
    private static final String ORDERBY = "orderBy";
    private static final String ORDER = "order";
    private static final String SEARCH = "search";

    private Integer getPageNumber(Map<String, String> paramMap) {
        String pageParam = paramMap.get(PAGE);
        return Objects.nonNull(pageParam) && NumberUtils.isCreatable(pageParam) ?
                Integer.parseInt(pageParam) : 0;
    }

    private String getOrderBy(Map<String, String> paramMap, String defaultOrderBy) {
        String orderBy = paramMap.get(ORDERBY);
        return Objects.nonNull(orderBy) ? orderBy : defaultOrderBy;
    }

    private Sort.Direction getOrder(Map<String, String> paramMap) {
        String order = paramMap.get(ORDER);
        return Objects.nonNull(order) ?
                order.equalsIgnoreCase("ASC")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC
                : Sort.Direction.ASC;
    }

    private String getSearch(Map<String, String> paramMap) {
        return
                Objects.nonNull(paramMap.get(SEARCH)) &&
                        !paramMap.get(SEARCH).equalsIgnoreCase(Strings.EMPTY)
                        ? paramMap.get(SEARCH).toLowerCase() : Strings.EMPTY;
    }

    protected Pageable getPageable(Map<String, String> paramMap, String defaultOrderBy) {
        Integer page = getPageNumber(paramMap);
        String orderBy = getOrderBy(paramMap, defaultOrderBy);
        Sort.Direction order = getOrder(paramMap);
        return PageRequest.of(page, Constants.PAGE_SIZE,
                order, orderBy);
    }

    protected String getSearchString(Map<String, String> paramMap) {
        return getSearch(paramMap);
    }
}
