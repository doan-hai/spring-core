package com.codergeezer.core.base.data;

import com.codergeezer.core.base.annotations.ConditionString;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

/**
 * Resource list acquisition request
 *
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
public class ListRequest {

    private final Logger logger = LoggerFactory.getLogger(ListRequest.class);

    @ConditionString
    private final String condition;

    private Integer pageSize;

    private Integer pageNo;

    @Pattern(regexp = "^[a-z0-9]+:(asc|desc)$", flags = CASE_INSENSITIVE)
    private String sort;

    public ListRequest(Integer pageSize, Integer pageNo, String sort, String condition) {
        this.pageSize = (pageSize == null || pageSize <= 0 || pageSize > 1000) ? 1000 : pageSize;
        this.pageNo = (pageNo == null || pageNo <= 0) ? 1 : pageNo;
        this.sort = sort;
        this.condition = condition;
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

    public <T> T get(Class<T> valueType) {
        return get(valueType, true);
    }

    public <T> T get(Class<T> valueType, boolean ignoreParseErrors) {
        if (condition == null) {
            return null;
        }
        String json;
        try {
            json = new String(Base64.getDecoder().decode(condition), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            if (logger.isDebugEnabled()) {
                logger.warn(e.getMessage());
            }
            if (!ignoreParseErrors) {
                throw e;
            }
            return null;
        }
        T object;
        try {
            object = new ObjectMapper().readValue(json, valueType);
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.warn(e.getMessage());
            }
            if (!ignoreParseErrors) {
                throw new IllegalArgumentException(e);
            }
            return null;
        }

        return object;
    }
}
