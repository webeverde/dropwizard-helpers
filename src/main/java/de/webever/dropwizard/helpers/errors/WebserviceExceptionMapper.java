package de.webever.dropwizard.helpers.errors;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.dropwizard.jersey.validation.JerseyViolationException;

/**
 * Catches {@link JerseyViolationException} and converts the attached
 * {@link ConstraintViolation}s to {@link WebserviceErrorContainer}s.
 * 
 * @author Richard Naeve
 *
 */
@Provider
public class WebserviceExceptionMapper implements ExceptionMapper<JerseyViolationException> {

    @Override
    public Response toResponse(JerseyViolationException exception) {
	Set<ConstraintViolation<?>> set = exception.getConstraintViolations();
	WebserviceException w = new WebserviceException();

	for (ConstraintViolation<?> constraintViolation : set) {
	    w.addError(WebserviceError.forValidation(constraintViolation));
	}
	return w.getResponse();
    }
}
