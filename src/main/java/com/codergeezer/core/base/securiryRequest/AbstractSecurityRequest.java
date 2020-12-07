package com.codergeezer.core.base.securiryRequest;

/**
 * @author haidv
 * @version 1.0
 */
public abstract class AbstractSecurityRequest {

    public abstract String encryptData(String decryptStr);

    public abstract String decryptData(String encryptStr);
}
