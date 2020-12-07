package com.codergeezer.core.base.swagger;

/**
 * @author haidv
 * @version 1.0
 */
public class TokenInfo {

    private String name;

    private String keyName;

    private String passAs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getPassAs() {
        return passAs;
    }

    public void setPassAs(String passAs) {
        this.passAs = passAs;
    }
}
