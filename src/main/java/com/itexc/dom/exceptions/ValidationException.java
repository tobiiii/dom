package com.itexc.dom.exceptions;

import com.itexc.dom.domain.enums.ERROR_CODE;

public class ValidationException extends DomException {
    private static final long serialVersionUID = 1L;


    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(ERROR_CODE errorCode) {
        super(errorCode.getValue());
        this.errorCode = errorCode;
    }

    public ValidationException(ERROR_CODE error_code, Object... params) {
        super(error_code.name());
        errorCode = error_code;
        this.params = params;
    }


}
