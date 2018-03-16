package ru.vpcb.footballassistant.notifications;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.lang.ref.WeakReference;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_INT_VALUE;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_NOTIFICATION;
import static ru.vpcb.footballassistant.utils.Config.EMPTY_NOTIFICATION_ID;
import static ru.vpcb.footballassistant.utils.Config.NT_ACTION_CREATE;
import static ru.vpcb.footballassistant.utils.Config.NT_BUNDLE_INTENT_NOTIFICATION_BODY;
import static ru.vpcb.footballassistant.utils.Config.NT_BUNDLE_INTENT_NOTIFICATION_ID;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 13-Feb-18
 * Email: vadim.v.voronov@gmail.com
 */
public class NotificationJobService extends JobService implements INotification {
    private static NotificationAsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        Bundle bundle = job.getExtras();
        String s = EMPTY_NOTIFICATION;
        int id = EMPTY_INT_VALUE;
        if (bundle != null) {
            s = bundle.getString(NT_BUNDLE_INTENT_NOTIFICATION_BODY);
            id = bundle.getInt(NT_BUNDLE_INTENT_NOTIFICATION_ID);
        }

        mBackgroundTask = new NotificationAsyncTask(this, job, s, id);
        mBackgroundTask.execute();


        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }

    @Override
    public void onCallback(JobParameters jobParameters) {
        jobFinished(jobParameters, false);
    }

    public static class NotificationAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> weakContext;
        private com.firebase.jobdispatcher.JobParameters jobParameters;
        private String s;
        private int id;

        NotificationAsyncTask(Context context,
                              com.firebase.jobdispatcher.JobParameters jobParameters,
                              String s, int id) {
            this.weakContext = new WeakReference<>(context);
            this.jobParameters = jobParameters;
            this.s = s;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Context context = weakContext.get();
            if (context != null) {
                NotificationUtils.executeTask(context, NT_ACTION_CREATE, s, id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
                /*
                 * Once the AsyncTask is finished, the job is finished. To inform JobManager that
                 * you're done, you call jobFinished with the jobParameters that were passed to your
                 * job and a boolean representing whether the job needs to be rescheduled. This is
                 * usually if something didn't work and you want the job to try running again.
                 */
            Context context = weakContext.get();
            if (context == null) return;
            ((INotification) context).onCallback(jobParameters);

        }
    }
}
