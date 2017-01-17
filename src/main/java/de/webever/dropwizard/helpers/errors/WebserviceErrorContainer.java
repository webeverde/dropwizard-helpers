package de.webever.dropwizard.helpers.errors;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Serializable container for errors with more specific and semantic infos than
 * normally provided by DropWizard.
 * 
 * @author Richard Naeve
 *
 */
public class WebserviceErrorContainer {
    private final int errorCode;

    private final String description;

    private final String param;

    private final int status;

    protected WebserviceErrorContainer(String field, WebserviceError error, Object... args) {
	this.param = field;
	this.description = error.getDescription(args);
	this.errorCode = error.getCode();
	this.status = error.getStatus();
    }

    @JsonProperty
    public int getErrorCode() {
	return errorCode;
    }

    @JsonProperty
    public int getStatus() {
	return status;
    }

    @JsonProperty
    public String getDescription() {
	return description;
    }

    @JsonProperty
    public String getParam() {
	return param;
    }
}