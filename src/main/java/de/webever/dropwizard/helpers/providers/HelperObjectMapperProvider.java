package de.webever.dropwizard.helpers.providers;

import java.text.SimpleDateFormat;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class HelperObjectMapperProvider implements ContextResolver<ObjectMapper> {

    final ObjectMapper objectMapper;

    public HelperObjectMapperProvider(ObjectMapper objectMapper, String dateFormat) {
	this.objectMapper = objectMapper;
	if (dateFormat != null) {
	    this.objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
	}
	/* We want dates to be treated as ISO8601 not timestamps. */
	objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> arg0) {
	return objectMapper;
    }
}