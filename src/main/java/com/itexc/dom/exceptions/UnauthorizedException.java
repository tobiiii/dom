package com.itexc.dom.exceptions;

import com.itexc.dom.domain.enums.ERROR_CODE;

public class UnauthorizedException extends DomRuntimeException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String msg) {
        super(msg);
        this.errorCode = ERROR_CODE.BAD_REQUEST;
    }

    public UnauthorizedException(ERROR_CODE errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public UnauthorizedException(ERROR_CODE errorCode) {
        super(errorCode.getValue());
        this.errorCode = errorCode;
    }

    public UnauthorizedException(String message, Object... params) {
        super(ERROR_CODE.BAD_REQUEST, message, params);
    }

}
