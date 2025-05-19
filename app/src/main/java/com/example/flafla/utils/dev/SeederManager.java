package com.example.flafla.utils.dev;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SeederManager {

    private static final String PREFS_NAME = "dev_seeder_prefs";
    private static final String KEY_DATA_SEEDED = "data_seeded";

    public static void runOnce(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean alreadySeeded = prefs.getBoolean(KEY_DATA_SEEDED, false);

        if (!alreadySeeded) {
            Runnable onComplete = () -> {
                prefs.edit().putBoolean(KEY_DATA_SEEDED, true).apply();
                Log.d("SeederManager", "Datos de desarrollo insertados correctamente.");
            };
        }

        Log.d("SeederManager", "Datos de desarrollo ya insertados.");
    }
}
