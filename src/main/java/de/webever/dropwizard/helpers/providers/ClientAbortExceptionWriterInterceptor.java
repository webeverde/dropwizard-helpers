package de.webever.dropwizard.helpers.providers;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Priority;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.apache.commons.io.output.ProxyOutputStream;

/**
 * Ignore exceptions when writing a response, which almost always means the
 * client disconnected before reading the full response.
 */
@Provider
@Priority(1)
public class ClientAbortExceptionWriterInterceptor implements WriterInterceptor {
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException {
	context.setOutputStream(new ClientAbortExceptionOutputStream(context.getOutputStream()));
	try {
	    context.proceed();
	} catch (Throwable t) {
	    for (Throwable cause = t; cause != null; cause = cause.getCause()) {
		if (cause instanceof ClientAbortException) {
		    return;
		}
	    }
	    throw t;
	}
    }

    private static class ClientAbortExceptionOutputStream extends ProxyOutputStream {
	public ClientAbortExceptionOutputStream(OutputStream out) {
	    super(out);
	}

	@Override
	protected void handleIOException(IOException e) throws IOException {
	    throw new ClientAbortException(e);
	}
    }

    @SuppressWarnings("serial")
    private static class ClientAbortException extends IOException {
	public ClientAbortException(IOException e) {
	    super(e);
	}
    }
}