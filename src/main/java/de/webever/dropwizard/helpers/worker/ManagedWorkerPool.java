package de.webever.dropwizard.helpers.worker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
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
	long delay;
	long interval;
	TimeUnit timeUnit;

	public IntervalQueueItem(Runnable runnable, long delay, long interval, TimeUnit timeUnit) {
	    super();
	    this.runnable = runnable;
	    this.delay = delay;
	    this.interval = interval;
	    this.timeUnit = timeUnit;
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
     * @param timeUnit
     *            the timeunit fo the interval
     */
    public void interval(Runnable runnable, long delay, long interval, TimeUnit timeUnit) {
	if (executor != null) {
	    executor.scheduleAtFixedRate(runnable, delay, interval, timeUnit);
	} else {
	    intervalQueue.add(new IntervalQueueItem(runnable, delay, interval, timeUnit));
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
    public void interval(Runnable runnable, long delay, long interval) {
	if (executor != null) {
	    executor.scheduleAtFixedRate(runnable, delay, interval, TimeUnit.MINUTES);
	} else {
	    intervalQueue.add(new IntervalQueueItem(runnable, delay, interval, TimeUnit.MINUTES));
	}
    }

    /**
     * Execute a runnable in an interval. Starts at time specified by hours and
     * minutes.
     * 
     * @param runnable
     *            the runnable
     * @param hours
     *            the hours of first execution
     * @param minutes
     *            the minuts of first execution
     * @param interval
     *            the interval to execute in, in milliseconds.
     */
    public void at(Runnable runnable, int hours, int minutes, long interval) {
	DateTime now = new DateTime();
	DateTime next = now.withHourOfDay(hours);
	next = now.withMinuteOfHour(minutes);
	if (next.isBefore(now)) {
	    next.plusDays(1);
	}
	long millis = next.getMillis() - now.getMillis();
	interval(runnable, millis, interval, TimeUnit.MILLISECONDS);

    }

    /**
     * Execute a runnable every 24 hours. Starts at time specified by hours and
     * minutes.
     * 
     * @param runnable
     *            the runnable
     * @param hours
     *            the hours of first execution
     * @param minutes
     *            the minuts of first execution
     */
    public void onceADayAt(Runnable runnable, int hours, int minutes) {
	DateTime now = new DateTime();
	DateTime next = now.withHourOfDay(hours);
	next = next.withMinuteOfHour(minutes);
	if (next.isBefore(now)) {
	    next = next.plusDays(1);
	}
	long millis = next.getMillis() - now.getMillis();
	long interval = 24 * 60 * 60 * 1000;
	interval(runnable, millis, interval, TimeUnit.MILLISECONDS);

    }

    @Override
    public void start() throws Exception {
	executor = Executors.newScheduledThreadPool(2);
	for (IntervalQueueItem intervalQueueItem : intervalQueue) {
	    interval(intervalQueueItem.runnable, intervalQueueItem.delay, intervalQueueItem.interval,
		    intervalQueueItem.timeUnit);
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
