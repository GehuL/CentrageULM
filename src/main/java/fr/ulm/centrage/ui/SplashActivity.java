package fr.ulm.centrage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import fr.ulm.centrage.R;
import fr.ulm.centrage.data.UlmListSingleton;

public class SplashActivity extends AppCompatActivity {

    private static int DELAY = 1500;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.splash_layout);

        long startTime = System.currentTimeMillis();

        // CHARGEMENT DU FICHIER
        UlmListSingleton.getUlmList(getApplicationContext());

        // LANCEMENT DE L'ACTIVITE
        long elapsedTime = System.currentTimeMillis() - startTime;

        System.err.println("Chargement: " + elapsedTime + " ms");

        long delay = 0;
        if (elapsedTime < DELAY)
            delay = DELAY - elapsedTime;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() ->
        {
            startActivity(new Intent(SplashActivity.this, UlmListActivity.class));
            finish();
        }, delay);
    }

}
