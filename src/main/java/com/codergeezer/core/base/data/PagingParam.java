package com.codergeezer.core.base.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Resource list acquisition request
 *
 * @author haidv
 * @version 1.0
 */
public class PagingParam {

    private Integer pageSize;

    private Integer pageNo;

    private String sort;

    public PagingParam(Integer pageSize, Integer pageNo, String sort) {
        this.pageSize = (pageSize == null || pageSize <= 0 || pageSize > 1000) ? 1000 : pageSize;
        this.pageNo = (pageNo == null || pageNo <= 0) ? 1 : pageNo;
        this.sort = sort;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Pageable pageable() {
        Sort srt = Sort.unsorted();
        if (this.sort != null) {
            String[] part = this.sort.split("_");
            for (String s : part) {
                String[] tmp = s.split(":");
                if (tmp.length == 2) {
                    srt.and(Sort.by(Sort.Direction.fromString(tmp[1]), tmp[0]));
                }
            }
        }
        return PageRequest.of(this.pageNo - 1, this.pageSize, srt);
    }
}
