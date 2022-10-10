package fr.ulm.centrage.data;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UlmSaver {

    public final static String FILENAME = "save/ulm.list";

    public static ArrayList<Ulm> loadFile(Context context) {
        ObjectInputStream objInput = null;
        ArrayList<Ulm> avions = new ArrayList<>();
        try {
            File file = getUlmFile(context);
            FileInputStream input = new FileInputStream(file);
            objInput = new ObjectInputStream(input);
            avions = (ArrayList<Ulm>) objInput.readObject();
            System.out.println(file.getName() + " loaded.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objInput != null) {
                try {
                    objInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return avions;
    }

    // Retourne l'emplacement du fichier
    public static File getUlmFile(Context context) {
        return new File(context.getExternalFilesDir(null), FILENAME);
    }

    public static boolean saveFile(List<Ulm> avions, Context context) {
        boolean saved = false;
        ObjectOutputStream objOutput = null;
        FileOutputStream outputCache = null;
        try {

            File file = getUlmFile(context);
            file.getParentFile().mkdirs();

            FileOutputStream output = new FileOutputStream(getUlmFile(context));
            objOutput = new ObjectOutputStream(output);
            objOutput.writeObject(avions);
            saved = true;

            System.out.println(file.getName() + " saved.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objOutput != null) {
                try {
                    objOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return saved;
    }

   /* // Met en cache le ficher de sauvegarde des avions
    public static void cache(Context context) {

        FileInputStream input = null;
        FileOutputStream output = null;


        try {
            input = new FileInputStream(UlmSaver.getAvionFile(context.getApplicationContext()));
            output = new FileOutputStream(new File(context.getCacheDir(), UlmSaver.FILENAME));

            FileUtils.copy(input, output);

            System.out.println("Fichier mis en cache");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

}
