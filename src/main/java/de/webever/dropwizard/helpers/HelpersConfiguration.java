package de.webever.dropwizard.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class HelpersConfiguration extends Configuration {
    
    @JsonProperty("corsOrigin")
    public String corsOrigin = "*";
    
    @JsonProperty("useTimestamps")
    public boolean useTimestamps = false; 
    
    @JsonProperty("dateFormat")
    public String dateFormat; 
    
    @JsonProperty("ignoreAbortedExceptions")
    public boolean ignoreAbortedExceptions = true;
}
