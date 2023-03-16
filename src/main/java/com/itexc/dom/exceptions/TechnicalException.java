package com.itexc.dom.exceptions;

import com.itexc.dom.domain.enums.ERROR_CODE;

public class TechnicalException extends DomRuntimeException {
    private static final long serialVersionUID = 1L;
    private Exception cause;

    public Exception getCause() {
        return cause;
    }

    public TechnicalException(String message) {
        super(message);
        this.errorCode = ERROR_CODE.BAD_REQUEST;
    }

    public TechnicalException(Exception e, ERROR_CODE errorCode) {
        super(e.getMessage());
        this.errorCode = errorCode;
        this.cause = e;
    }

    public TechnicalException(Exception e) {
        super(e.getMessage());
        this.cause = e;
    }

    public TechnicalException(ERROR_CODE errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public TechnicalException(ERROR_CODE errorCode) {
        super(errorCode.getValue());
        this.errorCode = errorCode;
    }

    public TechnicalException(String message, Object... params) {
        super(ERROR_CODE.BAD_REQUEST, message, params);
    }

}
