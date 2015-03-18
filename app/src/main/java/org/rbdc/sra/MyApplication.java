package org.rbdc.sra;

import android.app.Application;

import com.acra.slack.sample.acra.CrashSender;
import com.acra.slack.sample.network.ClientApi;

import org.acra.ACRA;
import org.acra.*;
import org.acra.annotation.ReportsCrashes;



@ReportsCrashes(
        formUri = "https://intense-inferno-7741.firebaseio.com/bugs",
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text,
        reportType = org.acra.sender.HttpSender.Type.JSON
)
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        ClientApi.initialize();

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        ACRA.getErrorReporter().setReportSender(new CrashSender());
        super.onCreate();
    }
}
