package com.codergeezer.core.base.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author haidv
 * @version 1.0
 */
public class BodyRequest {

    private final Logger logger = LoggerFactory.getLogger(BodyRequest.class);

    private boolean isSecurity;

    private String data;

    public boolean isSecurity() {
        return isSecurity;
    }

    public void setSecurity(boolean security) {
        isSecurity = security;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public <T> T get(Class<T> valueType) {
        if (data == null) {
            return null;
        }
        String json = null;
        if (isSecurity) {
            // TODO decode data
        } else {
            json = data;
        }
        T object;
        try {
            object = new ObjectMapper().readValue(json, valueType);
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.warn(e.getMessage());
            }
            throw new IllegalArgumentException(e);
        }
        return object;
    }
}
