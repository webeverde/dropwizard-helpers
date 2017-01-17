package de.webever.dropwizard.helpers.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import de.webever.dropwizard.helpers.MediaTypeUTF8;

/**
 * TODO Cache responses with etags!
 * 
 * @author richin
 *
 */
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaTypeUTF8.APPLICATION_JSON })
public abstract class AbstractResoure {

    private static <T> T throwException(int status) {
	throw new WebApplicationException(status);
    }

    /**
     * throw 403
     * 
     * @return nothing ever
     */
    protected static <T> T forbidden() {
	return throwException(403);

    }

    /**
     * throw 404 * @return nothing ever
     */
    protected static <T> T notFound() {
	return throwException(404);
    }

    /**
     * throw 400
     * 
     * @return nothing ever
     */
    protected static <T> T badRequest() {

	return throwException(400);
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
