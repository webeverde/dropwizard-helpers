package de.webever.dropwizard.helpers;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import de.webever.dropwizard.helpers.errors.WebserviceExceptionMapper;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.jersey.errors.EarlyEofExceptionMapper;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public abstract class HelpersBundle<T extends Configuration> implements ConfiguredBundle<T> {

	@Override
	public void run(T configuration, Environment environment) throws Exception {

		HelpersConfiguration helpersConfiguration = getHelpersConfiguration(configuration);

		environment.jersey().register(new WebserviceExceptionMapper());
		environment.jersey().register(new LoggingExceptionMapper<Throwable>() {
		});
		environment.jersey().register(new JsonProcessingExceptionMapper());
		environment.jersey().register(new EarlyEofExceptionMapper());
		
		Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, helpersConfiguration.corsOrigin);
		filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, helpersConfiguration.corsOrigin);
		filter.setInitParameter("allowedHeaders",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		filter.setInitParameter("allowCredentials", "true");
	}

	public abstract HelpersConfiguration getHelpersConfiguration(T configuration);

	@Override
	public void initialize(Bootstrap<?> bootstrap) {

	}

}
