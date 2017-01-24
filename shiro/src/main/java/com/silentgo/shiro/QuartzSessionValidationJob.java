package com.silentgo.shiro;

import com.silentgo.utils.log.Log;
import com.silentgo.utils.log.LogFactory;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by teddyzhu on 16/4/13.
 */
public class QuartzSessionValidationJob implements Job {

    /**
     * Key used to store the session manager in the job data map for this job.
     */
    public static final String SESSION_MANAGER_KEY = "sessionManager";


    private static final Log LOGGER = LogFactory.get();

    /**
     * Called when the job is executed by quartz. This method delegates to the <tt>validateSessions()</tt> method on the
     * associated session manager.
     *
     * @param context the Quartz job execution context for this execution.
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getMergedJobDataMap();
        ValidatingSessionManager sessionManager = (ValidatingSessionManager) jobDataMap.get(SESSION_MANAGER_KEY);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing session validation Quartz job...");
        }

        sessionManager.validateSessions();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Session validation Quartz job complete.");
        }
    }
}
