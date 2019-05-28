package de.webever.dropwizard.helpers.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
	final String id;
	Runnable runnable;
	final long delay;
	final long interval;
	final TimeUnit timeUnit;

	public IntervalQueueItem(String id, Runnable runnable, long delay, long interval, TimeUnit timeUnit) {
	    super();
	    this.id = id;
	    this.runnable = runnable;
	    this.delay = delay;
	    this.interval = interval;
	    this.timeUnit = timeUnit;
	}

    }

    private static class SubmitQueueItem {
	final String id;
	Runnable runnable;

	public SubmitQueueItem(String id, Runnable runnable) {
	    super();
	    this.id = id;
	    this.runnable = runnable;
	}

    }

    private List<IntervalQueueItem> intervalQueue = new ArrayList<>();
    private List<SubmitQueueItem> runnableQueue = new ArrayList<>();

    private Map<String, Future<?>> futures = new HashMap<>();
    private Map<String, ScheduledFuture<?>> scheduledFutures = new HashMap<>();

    private ScheduledExecutorService executor;

    public ManagedWorkerPool() {
    }

    /**
     * Submits a Runnable task for execution. Execution starts once the pool is
     * started.
     * 
     * @param runnable
     *            the runnable task.
     * @return id of the future for this runnable
     */
    public String submit(Runnable runnable) {
	String id = UUID.randomUUID().toString();
	if (executor != null) {
	    Future<?> future = executor.submit(runnable);
	    futures.put(id, future);
	} else {
	    runnableQueue.add(new SubmitQueueItem(id, runnable));
	}

	return id;
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
     * @return id of the future for this runnable
     */
    public String interval(Runnable runnable, long delay, long interval, TimeUnit timeUnit) {
	String id = UUID.randomUUID().toString();
	if (executor != null) {
	    ScheduledFuture<?> future = executor.scheduleAtFixedRate(runnable, delay, interval, timeUnit);
	    scheduledFutures.put(id, future);
	} else {
	    intervalQueue.add(new IntervalQueueItem(id, runnable, delay, interval, timeUnit));
	}
	return id;
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
     * @return id of the future for this runnable
     */
    public String interval(Runnable runnable, long delay, long interval) {
	String id = UUID.randomUUID().toString();
	if (executor != null) {
	    ScheduledFuture<?> future = executor.scheduleAtFixedRate(runnable, delay, interval, TimeUnit.MINUTES);
	    scheduledFutures.put(id, future);
	} else {
	    intervalQueue.add(new IntervalQueueItem(id, runnable, delay, interval, TimeUnit.MINUTES));
	}
	return id;
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
     * @return id of the future for this runnable
     */
    public String at(Runnable runnable, int hours, int minutes, long interval) {
	DateTime now = new DateTime();
	DateTime next = now.withHourOfDay(hours);
	next = now.withMinuteOfHour(minutes);
	if (next.isBefore(now)) {
	    next.plusDays(1);
	}
	long millis = next.getMillis() - now.getMillis();
	return interval(runnable, millis, interval, TimeUnit.MILLISECONDS);

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
     *            the minutes of first execution
     * @return id of the future for this runnable
     */
    public String onceADayAt(Runnable runnable, int hours, int minutes) {
	DateTime now = new DateTime();
	DateTime next = now.withHourOfDay(hours);
	next = next.withMinuteOfHour(minutes);
	if (next.isBefore(now)) {
	    next = next.plusDays(1);
	}
	long millis = next.getMillis() - now.getMillis();
	long interval = 24 * 60 * 60 * 1000;
	return interval(runnable, millis, interval, TimeUnit.MILLISECONDS);

    }

    /**
     * Get the future for the supplied id.
     * 
     * @param id
     *            the id of the future
     * @return the future or null
     */
    @SuppressWarnings("unchecked")
    public <T> Future<T> getFuture(String id) {
	return (Future<T>) futures.get(id);
    }

    /**
     * Get the scheduled future for the supplied id.
     * 
     * @param id
     *            the id of the future
     * @return the future or null
     */
    @SuppressWarnings("unchecked")
    public <T> ScheduledFuture<T> getScheduledFuture(String id) {
	return (ScheduledFuture<T>) scheduledFutures.get(id);
    }

    @Override
    public void start() throws Exception {
	int cores = Runtime.getRuntime().availableProcessors();
	if (cores < 3)
	    cores = 2;
	executor = Executors.newScheduledThreadPool(cores);
	for (IntervalQueueItem intervalQueueItem : intervalQueue) {
	    ScheduledFuture<?> future = executor.scheduleAtFixedRate(intervalQueueItem.runnable,
		    intervalQueueItem.delay, intervalQueueItem.interval, intervalQueueItem.timeUnit);
	    scheduledFutures.put(intervalQueueItem.id, future);
	}

	intervalQueue.clear();

	for (SubmitQueueItem item : runnableQueue) {
	    Future<?> future = executor.submit(item.runnable);
	    futures.put(item.id, future);
	}

	runnableQueue.clear();
	LOGGER.info("Pool started");
    }

    @Override
    public void stop() throws Exception {
	executor.shutdown();
    }

}
