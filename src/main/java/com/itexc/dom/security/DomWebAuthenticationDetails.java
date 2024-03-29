package com.itexc.dom.security;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.web.authentication.WebAuthenticationDetails;


public class DomWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	private String originIP;

	public DomWebAuthenticationDetails(HttpServletRequest req) {
		super(req);
		this.originIP = StringUtils.isNotEmpty(req.getHeader("X-Forwarded-For")) ? req.getHeader("X-Forwarded-For")
				: req.getRemoteAddr();
	}

	@Override
	public String getRemoteAddress() {
		return originIP;
	}
}
