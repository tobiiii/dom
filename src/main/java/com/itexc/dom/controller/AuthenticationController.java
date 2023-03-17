package com.itexc.dom.controller;

import com.itexc.dom.domain.DTO.AuthenticationRequest;
import com.itexc.dom.domain.DTO.AuthenticationResponse;
import com.itexc.dom.exceptions.ValidationException;
import com.itexc.dom.sevice.DBSessionService;
import com.itexc.dom.sevice.UserService;
import com.itexc.dom.utils.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/auth")
@Validated
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private DBSessionService dbSessionService;

    /**
     * Admin authentication
     *
     * @param authenticationRequest authentication request (username, password)
     * @return JSON response
     * @throws ValidationException validation exception
     */
    @PostMapping(value = "/authentification")
    public JsonResponse authentication(@Valid @RequestBody final AuthenticationRequest authenticationRequest,
                                       final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse)
            throws Throwable {
        AuthenticationResponse response = userService.authentication(authenticationRequest, httpServletRequest, httpServletResponse);
        return JsonResponse.builder()
                .data(response)
                .status(JsonResponse.STATUS.SUCCESS).build();

    }

    /**
     * Admin refresh token
     *
     * @param tokenId id expired token request (refresh token)
     * @return JSON response
     * @throws ValidationException validation exception
     */
    @PostMapping(value = "/refresh_token")
    public JsonResponse refreshToken(
            @NotNull(message = "Token {REQUIRED}") String tokenId,
            HttpServletRequest request) throws Throwable {
        AuthenticationResponse authenticationResponse = userService.renewSession(tokenId, request);
        return JsonResponse.builder().data(authenticationResponse)
                .status(JsonResponse.STATUS.SUCCESS).build();
    }

    /**
     * Logout admin user
     *
     * @return JSON response
     * @throws ValidationException validation exception
     */
    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping(value = "/logout")
    public JsonResponse logout() throws ValidationException {
        dbSessionService.logout();
        return JsonResponse.builder().data(null)
                .status(JsonResponse.STATUS.SUCCESS).build();
    }
}
