package com.itexc.dom.exceptions;

import com.itexc.dom.domain.enums.ERROR_CODE;

import java.text.MessageFormat;

public abstract class DomRuntimeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    ERROR_CODE errorCode;
    Object[] params;

    public DomRuntimeException(ERROR_CODE error_code, String message, Object... params) {
        super(message);
        errorCode = error_code;
        this.params = params;
    }

    public DomRuntimeException(String message) {
        super(message);
    }

    public DomRuntimeException(ERROR_CODE error_code, Exception e) {
        super(e);
        this.errorCode = error_code;
    }

    public String getErrorCode() {
        return errorCode != null ? errorCode.name() : null;
    }

    public Object[] getParams() {
        return params;
    }

    public void setErrorCode(ERROR_CODE errorCode) {
        this.errorCode = errorCode;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String formatMessage() {
        if (errorCode == null) {
            return super.getMessage();
        } else {
            return MessageFormat.format(getMessage(), params);
        }
    }

    public String formatMessageWithErrorCode() {
        return errorCode.name() + ":" + formatMessage();
    }

}
