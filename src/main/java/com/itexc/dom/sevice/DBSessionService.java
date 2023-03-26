package com.itexc.dom.sevice;

import com.itexc.dom.domain.DBSession;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.User;
import com.itexc.dom.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;


public interface DBSessionService {

        void disconnectByToken(final String jwtToken);

        void disconnectByProfile(final Profile profile);

        void disconnectByUser(final User user);

        Optional<DBSession> findBy(final String field, final String value);

        void checkSession(Optional<DBSession> dbSession) throws ValidationException;

        void logout() throws ValidationException;

        void startSession(final HttpServletRequest request, final String jwtToken, User user);

        void refreshToken(String expiredToken, final String newToken) throws ValidationException;

}
