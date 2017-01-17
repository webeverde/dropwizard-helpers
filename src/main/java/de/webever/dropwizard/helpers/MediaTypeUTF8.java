package de.webever.dropwizard.helpers;

import javax.ws.rs.core.MediaType;

public class MediaTypeUTF8 {
    private static final String CHARSET_UTF8 = "; charset=utf-8";
    public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON + CHARSET_UTF8;
    public static final String APPLICATION_FORM_URLENCODED = MediaType.APPLICATION_FORM_URLENCODED + CHARSET_UTF8;
    public static final String APPLICATION_XML = MediaType.APPLICATION_XML + CHARSET_UTF8;
    public static final String APPLICATION_XHTML_XML = MediaType.APPLICATION_XHTML_XML + CHARSET_UTF8;
    public static final String MULTIPART_FORM_DATA = MediaType.MULTIPART_FORM_DATA + CHARSET_UTF8;
    public static final String TEXT_HTML = MediaType.TEXT_HTML + CHARSET_UTF8;
    public static final String TEXT_PLAIN = MediaType.TEXT_PLAIN + CHARSET_UTF8;
    public static final String TEXT_XML = MediaType.TEXT_XML + CHARSET_UTF8;
}
