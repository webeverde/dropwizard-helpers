package de.webever.dropwizard.helpers.errors;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Exception that generates the error response and adds the attached
 * {@link WebserviceErrorContainer}s to the body.
 * 
 * @author Richard Naeve
 *
 */
public class WebserviceException extends WebApplicationException {

    private static final long serialVersionUID = -8090200466820494207L;

    private List<WebserviceErrorContainer> errors;

    public WebserviceException() {
	this.errors = new ArrayList<WebserviceErrorContainer>();
    }

    public WebserviceException(List<WebserviceErrorContainer> errors) {
	this.errors = errors;
    }

    public WebserviceException(WebserviceErrorContainer error) {
	this();
	errors.add(error);
    }

    public void addError(WebserviceErrorContainer error) {
	if (!errors.contains(error)) {
	    errors.add(error);
	}
    }

    @Override
    public Response getResponse() {
	if (errors.size() == 1) {
	    return Response.status(errors.get(0).getStatus()).entity(errors).build();
	}
	return Response.status(400).entity(errors).build();
    }

}
