package de.webever.dropwizard.helpers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public class IPFilter implements ContainerRequestFilter {

	public static final String REMOTE_IP = "RemoteIP";
	
	@Context
	private HttpServletRequest sr;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String ip = sr.getRemoteAddr();
		requestContext.setProperty(REMOTE_IP, ip);
	}

}
