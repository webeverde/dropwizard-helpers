package de.webever.dropwizard.helpers.errors;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    private final HashMap<String, String> data;

    public WebserviceErrorContainer(String field, WebserviceError error, Object[] args) {
	this(field, error, null, args);
    }

    public WebserviceErrorContainer(String field, WebserviceError error, HashMap<String, String> data, Object... args) {
	this.param = field;
	this.description = error.getDescription(args);
	this.errorCode = error.getCode();
	this.status = error.getStatus();
	this.data = data;
    }

    @JsonCreator
    public WebserviceErrorContainer(@JsonProperty("errorCode") int errorCode,
	    @JsonProperty("description") String description, @JsonProperty("param") String param,
	    @JsonProperty("status") int status, @JsonProperty("data") HashMap<String, String> data) {
	super();
	this.errorCode = errorCode;
	this.description = description;
	this.param = param;
	this.status = status;
	this.data = data;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "WebserviceErrorContainer [errorCode=" + errorCode + ", description=" + description + ", param=" + param
		+ ", status=" + status + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + errorCode;
	result = prime * result + ((param == null) ? 0 : param.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	WebserviceErrorContainer other = (WebserviceErrorContainer) obj;
	if (errorCode != other.errorCode) {
	    return false;
	}
	if (param == null) {
	    if (other.param != null) {
		return false;
	    }
	} else if (!param.equals(other.param)) {
	    return false;
	}
	return true;
    }

    public HashMap<String, String> getData() {
	return data;
    }

}