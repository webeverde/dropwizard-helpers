package de.webever.dropwizard.helpers.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.webever.dropwizard.helpers.MediaTypeUTF8;
import de.webever.dropwizard.helpers.errors.WebserviceError;
import de.webever.dropwizard.helpers.errors.WebserviceException;

/**
 * TODO Cache responses with etags!
 * 
 * @author richin
 *
 */
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaTypeUTF8.APPLICATION_JSON })
public abstract class AbstractResoure {

	private static <T> T throwException(WebserviceError.FIXED error) {
		throw new WebserviceException(error.getContainer(""));
	}

	/**
	 * throw 403
	 * 
	 * @return nothing ever
	 */
	protected static <T> T forbidden() {
		return throwException(WebserviceError.FIXED.FORBIDDEN);

	}

	/**
	 * throw 404 * @return nothing ever
	 */
	protected static <T> T notFound() {
		return throwException(WebserviceError.FIXED.NOT_FOUND);
	}

	/**
	 * throw 400
	 * 
	 * @return nothing ever
	 */
	protected static <T> T badRequest() {

		return throwException(WebserviceError.FIXED.BAD_REQUEST);
	}

	/**
	 * throw 404 if o == null
	 * 
	 * @param o
	 *            Object to check
	 * 
	 */
	protected static void notFoundIfNull(Object o) {
		if (o == null)
			notFound();
	}

}
