package com.susankya.yubahunkar.application;

import android.app.Application;

import com.susankya.yubahunkar.generic.NotificationTapHandler;
import com.susankya.yubahunkar.generic.Posts;
import com.onesignal.OneSignal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostsApplication extends Application {

    private static String URL = Posts.BASE_URL;

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationTapHandler tapHandler= new NotificationTapHandler(this.getApplicationContext());
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(tapHandler)
                .init();
    }

    public static Retrofit ClientRetrofit() {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }
}
