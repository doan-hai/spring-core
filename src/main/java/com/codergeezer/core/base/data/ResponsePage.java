package com.codergeezer.core.base.data;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Getter
public class ResponsePage<T> {

    private final int pageNo;

    private final int pageSize;

    private final long totalCount;

    private final int totalPage;

    private final List<T> data;

    public ResponsePage(int pageNo, int pageSize, long totalCount, int totalPage, List<T> data) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.data = data;
    }

    public ResponsePage(Page<T> page) {
        this(page, page.getContent());
    }

    @SuppressWarnings("rawtypes")
    public ResponsePage(Page page, List<T> data) {
        this(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages(), data);
    }
}
