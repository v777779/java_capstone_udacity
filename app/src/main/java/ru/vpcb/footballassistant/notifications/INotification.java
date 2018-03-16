package ru.vpcb.footballassistant.notifications;

import com.firebase.jobdispatcher.JobParameters;

public  interface INotification {
        void onCallback(JobParameters jobParameters);
    }    