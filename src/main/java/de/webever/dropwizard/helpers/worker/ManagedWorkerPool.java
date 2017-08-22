package de.webever.dropwizard.helpers.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.lifecycle.Managed;

/**
 * A managed execution pool.
 * 
 * @author Richard Naeve
 *
 */
public class ManagedWorkerPool implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedWorkerPool.class);

    private static class IntervalQueueItem {
	Runnable runnable;
	int delay;
	int interval;

	public IntervalQueueItem(Runnable runnable, int delay, int interval) {
	    super();
	    this.runnable = runnable;
	    this.delay = delay;
	    this.interval = interval;
	}

    }

    private List<IntervalQueueItem> intervalQueue = new ArrayList<>();
    private List<Runnable> runnableQueue = new ArrayList<>();

    private ScheduledExecutorService executor;

    public ManagedWorkerPool() {

    }

    /**
     * Submits a Runnable task for execution. Execution starts once the pool is
     * started.
     * 
     * @param runnable
     *            the runnable task.
     */
    public void submit(Runnable runnable) {
	if (executor != null) {
	    executor.submit(runnable);
	} else {
	    runnableQueue.add(runnable);
	}
    }

    /**
     * Execute a runnable every X minutes. Execution starts once the pool is
     * started.
     * 
     * @param runnable
     *            the runnabel
     * @param delay
     *            the delay for first run
     * @param interval
     *            the interval to run after first delay in minutes.
     */
    public void interval(Runnable runnable, int delay, int interval) {
	if (executor != null) {
	    executor.scheduleAtFixedRate(runnable, delay, interval, TimeUnit.MINUTES);
	} else {
	    intervalQueue.add(new IntervalQueueItem(runnable, delay, interval));
	}
    }

    @Override
    public void start() throws Exception {
	executor = Executors.newScheduledThreadPool(2);
	for (IntervalQueueItem intervalQueueItem : intervalQueue) {
	    interval(intervalQueueItem.runnable, intervalQueueItem.delay, intervalQueueItem.interval);
	}

	intervalQueue.clear();

	for (Runnable runnable : runnableQueue) {
	    submit(runnable);
	}

	runnableQueue.clear();
	LOGGER.info("Pool started");
    }

    @Override
    public void stop() throws Exception {
	executor.shutdown();
    }

}
