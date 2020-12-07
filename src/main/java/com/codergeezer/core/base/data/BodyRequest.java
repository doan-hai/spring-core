package com.codergeezer.core.base.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
public class BodyRequest<T> {

    private String encryptData;

    private T rawData;
}
