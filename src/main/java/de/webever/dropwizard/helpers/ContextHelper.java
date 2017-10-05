package de.webever.dropwizard.helpers;

import javax.ws.rs.container.ContainerRequestContext;

public class ContextHelper {
    public static String getUserAgent(ContainerRequestContext request) {
  	return request.getHeaderString("user-agent");
      }

      public static String getRemoteAdress(ContainerRequestContext request) {
  	String forwarded = request.getHeaderString("x-forwarded-for");
  	String remoteIP;
  	if (forwarded != null) {
  	    int i = forwarded.indexOf(",");
  	    if (i > -1) {
  		remoteIP = forwarded.substring(0, i);
  	    } else {
  		remoteIP = forwarded;
  	    }
  	} else {
  	    Object p = request.getProperty(IPFilter.REMOTE_IP);
  	    if (p != null) {
  		remoteIP = p.toString();
  	    } else {
  		remoteIP = "0.0.0.0";
  	    }
  	}
  	return remoteIP;
      }
}
