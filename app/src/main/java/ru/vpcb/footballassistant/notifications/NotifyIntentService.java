package ru.vpcb.footballassistant.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import static ru.vpcb.footballassistant.utils.Config.EMPTY_NOTIFICATION_ID;
import static ru.vpcb.footballassistant.utils.Config.NT_BUNDLE_INTENT_NOTIFICATION_BODY;
import static ru.vpcb.footballassistant.utils.Config.NT_BUNDLE_INTENT_NOTIFICATION_ID;

/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 13-Feb-18
 * Email: vadim.v.voronov@gmail.com
 */
public class NotifyIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotifyIntentService() {
        super(NotifyIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        String s = intent.getStringExtra(NT_BUNDLE_INTENT_NOTIFICATION_BODY);
        int id = intent.getIntExtra(NT_BUNDLE_INTENT_NOTIFICATION_ID, EMPTY_NOTIFICATION_ID);
        NotificationUtils.executeTask(this, action, s, id);

    }
}
