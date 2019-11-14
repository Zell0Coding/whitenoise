package com.ponicamedia.android.whitenoise.Controllers;

import android.app.Application;

import com.ponicamedia.android.whitenoise.Utills.Utill;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class whitenoise extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(Utill.API_METRIKA).build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this);

    }
}
