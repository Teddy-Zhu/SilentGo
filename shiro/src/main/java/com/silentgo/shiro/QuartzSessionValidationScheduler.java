package com.silentgo.shiro;

import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by teddyzhu on 16/4/13.
 */
public class QuartzSessionValidationScheduler implements SessionValidationScheduler {
    public static final long DEFAULT_SESSION_VALIDATION_INTERVAL = DefaultSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL;
    private static final String JOB_NAME = "SessionValidationJob";

    private static final Log LOGGER = LogFactory.get();
    private static final String SESSION_MANAGER_KEY = QuartzSessionValidationJob.SESSION_MANAGER_KEY;
    private Scheduler scheduler;
    private boolean schedulerImplicitlyCreated = false;

    private boolean enabled = false;
    private ValidatingSessionManager sessionManager;
    private long sessionValidationInterval = DEFAULT_SESSION_VALIDATION_INTERVAL;

    public QuartzSessionValidationScheduler() {
    }

    public QuartzSessionValidationScheduler(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    protected Scheduler getScheduler() throws SchedulerException {
        if (this.scheduler == null) {
            this.scheduler = StdSchedulerFactory.getDefaultScheduler();
            this.schedulerImplicitlyCreated = true;
        }
        return this.scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setSessionManager(ValidatingSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setSessionValidationInterval(long sessionValidationInterval) {
        this.sessionValidationInterval = sessionValidationInterval;
    }

    public void enableSessionValidation() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Scheduling session validation job using Quartz with session validation interval of ["
                    + this.sessionValidationInterval + "]ms...");
        }

        try {
            SimpleTrigger trigger = TriggerBuilder.newTrigger().startNow().withIdentity(JOB_NAME, Scheduler.DEFAULT_GROUP)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(sessionValidationInterval))
                    .build();

            JobDetail detail = JobBuilder.newJob(QuartzSessionValidationJob.class)
                    .withIdentity(JOB_NAME, Scheduler.DEFAULT_GROUP).build();
            detail.getJobDataMap().put(SESSION_MANAGER_KEY, this.sessionManager);
            Scheduler scheduler = getScheduler();

            scheduler.scheduleJob(detail, trigger);
            if (this.schedulerImplicitlyCreated) {
                scheduler.start();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Successfully started implicitly created Quartz Scheduler instance.");
                }
            }
            this.enabled = true;

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Session validation job successfully scheduled with Quartz.");
        } catch (SchedulerException e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error(e, "Error starting the Quartz session validation job.  Session validation may not occur.");
        }
    }

    public void disableSessionValidation() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Stopping Quartz session validation job...");
        }
        Scheduler scheduler;
        try {
            scheduler = getScheduler();
            if (scheduler == null) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("getScheduler() method returned a null Quartz scheduler, which is unexpected.  Please check your configuration and/or implementation.  Returning quietly since there is no validation job to remove (scheduler does not exist).");
                }

                return;
            }
        } catch (SchedulerException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Unable to acquire Quartz Scheduler.  Ignoring and returning (already stopped?)", e);
            }
            return;
        }
        try {
            scheduler.unscheduleJob(new TriggerKey("SessionValidationJob", "DEFAULT"));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Quartz session validation job stopped successfully.");
        } catch (SchedulerException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Could not cleanly remove SessionValidationJob from Quartz scheduler.  Ignoring and stopping.", e);
            }

        }

        this.enabled = false;

        if (this.schedulerImplicitlyCreated)
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                if (LOGGER.isWarnEnabled())
                    LOGGER.warn("Unable to cleanly shutdown implicitly created Quartz Scheduler instance.", e);
            } finally {
                setScheduler(null);
                this.schedulerImplicitlyCreated = false;
            }
    }
}
