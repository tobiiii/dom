package com.itexc.dom.security;

import com.itexc.dom.domain.DBSession;
import com.itexc.dom.domain.User;
import com.itexc.dom.domain.enums.ERROR_CODE;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.service_impl.UserDetailsServiceImpl;
import com.itexc.dom.sevice.DBSessionService;
import com.itexc.dom.sevice.UserService;
import com.itexc.dom.utils.JsonResponse;
import com.itexc.dom.utils.ParamsProvider;
import com.itexc.dom.utils.Utils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private ParamsProvider paramsProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private TokenProvider jwtUtil;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DBSessionService dbSessionService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.info(request.getRemoteAddr() + "\t" + request.getMethod() + "\t" + request.getRequestURI());
        log.info("--------------------------------");

        String token = null;

        final String tokenHeader = request.getHeader(paramsProvider.getAuthorizationHeader());
        if (tokenHeader != null && tokenHeader.startsWith(paramsProvider.getTokenPrefix())) {

            //Check token validation
            try {
                token = jwtUtil.getTokenFromRequest(request);
                jwtUtil.validateToken(token);
            } catch (ValidationException e) {
                if (request.getRequestURI().endsWith("/refresh_token") &&
                    ERROR_CODE.valueOf(e.getErrorCode()).equals(ERROR_CODE.EXPIRED_TOKEN)) {
                    //Let him pass filter
                    chain.doFilter(request, response);
                    return;
                }

                generateFailResponse(request, response, HttpStatus.UNAUTHORIZED, ERROR_CODE.valueOf(e.getErrorCode()), e.getParams());
                return;
            }

            //Get username from token
            String email = jwtUtil.getUsernameFromToken(token);
            if (StringUtils.isBlank(email)) {
                generateFailResponse(request, response, HttpStatus.UNAUTHORIZED, ERROR_CODE.INVALID_SESSION);
                return;
            }
            User user = null;
            try {
                user = userService.checkEmail(email);
            } catch (ValidationException e) {
                // TODO close sessions if exist
                generateFailResponse(request, response, HttpStatus.UNAUTHORIZED, ERROR_CODE.valueOf(e.getErrorCode()), e.getParams());
                return;
            }


            //Check session
            final var idToken = jwtUtil.getIdFromToken(token);
            Optional<DBSession> optionalSession;

            try {
                optionalSession = dbSessionService.findBy("token", idToken);
                if (optionalSession.isEmpty() || !optionalSession.get().getEmail().equals(user.getEmailAddress())) {
                    throw new ValidationException(ERROR_CODE.INVALID_SESSION);
                }
                dbSessionService.checkSession(optionalSession);
            } catch (ValidationException e) {
                generateFailResponse(request, response, HttpStatus.UNAUTHORIZED, ERROR_CODE.valueOf(e.getErrorCode()), e.getParams());
                return;
            }

            //Set security context holder
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = null;
                if (optionalSession.isPresent()) {
                    userDetails = userDetailsServiceImpl.loadUserByUsername(email, optionalSession.get());

                } else {
                    userDetails = userDetailsServiceImpl.loadUserByUsername(email);
                }

                var authentication = jwtUtil.getAuthenticationToken(token, userDetails);
                var webDetails = new DomWebAuthenticationDetails(request);
                authentication.setDetails(webDetails);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }


        chain.doFilter(request, response);
    }

    private void generateFailResponse(HttpServletRequest req, HttpServletResponse res, HttpStatus httpStatus,
                                      ERROR_CODE errorCode, Object... params) {
        res.setContentType("application/json");
        res.setStatus(httpStatus.value());
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setStatus(JsonResponse.STATUS.FAILED);
        jsonResponse.setErrorCode(errorCode.name());
        String lang = req.getHeader("Accept-Language");

        try {
//            Locale locale = StringUtils.isNotBlank(lang) ? new Locale(lang) : new Locale("fr");
            Locale locale = new Locale("fr");
            jsonResponse.setErrorMsg(messageSource.getMessage(jsonResponse.getErrorCode(), params, locale));
        } catch (NoSuchMessageException e2) {
//            jsonResponse.setErrorMsg("");
        }
        try {
            res.getWriter().write(Utils.convertObjectToJson(jsonResponse));
            res.getWriter().flush();
        } catch (IOException e) {
            log.warn("generateFailResponse", e.getMessage());
        }
    }

}
