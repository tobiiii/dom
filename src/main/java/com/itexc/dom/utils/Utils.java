package com.itexc.dom.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class Utils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    static char num[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    static char nums[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        var mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        return mapper.writeValueAsString(object);
    }

    public static String getFullURL(HttpServletRequest request) {
        var requestURL = new StringBuilder(request.getRequestURL().toString());
        var queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    public static int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }

    public static String getRandomNumiricString(int len) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(nums[(int) Math.floor(Math.random() * 9)]);

        for (int i = 1; i < len; i++) {
            strBuilder.append(num[(int) Math.floor(Math.random() * 10)]);
        }

        return strBuilder.toString();
    }

    public static String getUniqueIdMsg(int length) {
        String timestamp = String.valueOf(new Date().getTime());
        String randomChar = getRandomNumiricString(length - timestamp.length());
        return randomChar + timestamp;
    }

    private Utils() {
        throw new IllegalStateException("Utility class");
    }



    public static void validateString(String stringToValidate, String pattern) throws ValidationException {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(stringToValidate);
        if (m.find()) {
            log.error("String doesn't match pattern");
            throw new ValidationException(ERROR_CODE.INTERNAL_SERVER_ERROR);
        }
    }

    public static void writeFile(String fileName, String fileContent, String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(path + "/" + fileName + ".txt");
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fileContent);
            bw.close();
        } catch (IOException e) {
            log.error("Failed to write mq files", e);
        }

    }


}
