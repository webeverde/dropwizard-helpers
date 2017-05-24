package de.webever.dropwizard.helpers.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Worker implements Runnable {

	protected final Logger LOGGER;

	public abstract void work();

	protected Worker() {
		LOGGER = LoggerFactory.getLogger(getClass());
	}

	@Override
	public void run() {
		try {
			work();
		} catch (Throwable t) {
			LOGGER.error("Error!", t);
		}

	}

}
