package com.codergeezer.core.feign;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
public class BasicAuthProperties {

    private String username;

    private String password;

    private List<String> feignClassName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFeignClassName() {
        return feignClassName.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public void setFeignClassName(List<String> feignClassName) {
        this.feignClassName = feignClassName;
    }
}
