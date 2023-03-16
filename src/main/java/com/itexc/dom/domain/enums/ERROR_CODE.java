package com.itexc.dom.domain.enums;

import com.itexc.dom.utils.ContextHolder;

import java.text.MessageFormat;

public enum ERROR_CODE {
    BAD_REQUEST("BAD_REQUEST"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    INEXISTANT_USER("INEXISTANT_USER"),
    EMAIL_EXISTS("EMAIL_EXIST"),
    REFERENCED_USER("REFERENCED_USER");


    private String value;

    ERROR_CODE(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }


    private final String message;

    ERROR_CODE(String message) {
        this.message = message;
    }

    public String getMessage(Object[] params) {
        // parse params
        if (params == null) {
            return message;
        }
        int index = 0;
        for (Object param : params) {
            Object object = ContextHolder.get(param.toString());
            if (object != null) {
                params[index] = object;
            }
            index++;

        }
        return MessageFormat.format(message, params);
    }

    public String getMessageWithErrorCode(Object[] params) {

        if (params == null) {
            return name() + ":" + message;
        }

        int index = 0;

        for (Object param : params) {
            if (param != null) {
                Object object = ContextHolder.get(param.toString());
                if (object != null) {
                    params[index] = object;
                }
            }
            index++;
        }
        return name() + ":" + MessageFormat.format(message, params);
    }

}
