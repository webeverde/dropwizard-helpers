package de.webever.dropwizard.helpers.worker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.dropwizard.lifecycle.Managed;

public class ManagedWorkerPool implements Managed {

    private ScheduledExecutorService executor;

    public ManagedWorkerPool() {
	executor = Executors.newScheduledThreadPool(2);
    }

    public void submit(Runnable runnable) {
	executor.submit(runnable);
    }

    /**
     * Execute a runnable every X minutes
     * 
     * @param runnable
     *            the runnabel
     * @param delay
     *            the delay for first run
     * @param interval
     *            the interval to run after first delay in minutes.
     */
    public void interval(Runnable runnable, int delay, int interval) {
	executor.scheduleAtFixedRate(runnable, delay, interval, TimeUnit.MINUTES);
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
	executor.shutdown();
    }

}
