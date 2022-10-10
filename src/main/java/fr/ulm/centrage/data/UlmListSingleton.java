package fr.ulm.centrage.data;

import android.content.Context;

import java.util.ArrayList;

public final class UlmListSingleton {

    private static ArrayList<Ulm> ulm;

    public static ArrayList<Ulm> getUlmList(Context context) {
        if (ulm == null) {
            ulm = UlmSaver.loadFile(context);
        }
        return ulm;
    }

}
