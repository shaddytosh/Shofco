package ke.co.shofcosacco.ui.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
public class SmsBroadcastReceiver extends BroadcastReceiver {
    SmsBroadcastReceiverListener smsBroadcastReceiverListener;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (smsBroadcastReceiverListener != null) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                    if (status != null) {
                        switch (status.getStatusCode()) {
                            case CommonStatusCodes.SUCCESS:
                                // Message retrieval successful
                                Intent messageIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                                smsBroadcastReceiverListener.onSuccess(messageIntent);
                                break;
                            case CommonStatusCodes.TIMEOUT:
                                // Message retrieval timed out
                                smsBroadcastReceiverListener.onFailure();
                                break;
                            // Add more cases as needed
                        }
                    }
                }
            }
        }
    }

    public interface SmsBroadcastReceiverListener {
        void onSuccess(Intent intent);

        void onFailure();
    }
}