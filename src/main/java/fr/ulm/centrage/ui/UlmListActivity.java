package fr.ulm.centrage.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import fr.ulm.centrage.R;
import fr.ulm.centrage.data.Ulm;
import fr.ulm.centrage.data.UlmListSingleton;
import fr.ulm.centrage.data.UlmSaver;

public class UlmListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private UlmAdapter ulmAdapter;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.avion_list);

        ulmAdapter = new UlmAdapter(this, R.layout.avion_text_view);
        ulmAdapter.addAll(UlmListSingleton.getUlmList(getApplicationContext()));
        ListView ulmList = findViewById(R.id.avionList);
        ulmList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        ulmList.setAdapter(ulmAdapter);
        ulmList.setOnItemClickListener(this);

        findViewById(R.id.delete).setOnClickListener(this::btnDeleteulm);

        findViewById(R.id.add).setOnClickListener(this::btnAddulm);

        findViewById(R.id.edit).setOnClickListener(this::btnEditulm);

        findViewById(R.id.watch).setOnClickListener(this::btnWatchulm);

        findViewById(R.id.shareBtn).setOnClickListener(this::btnShareulmFile);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType() != null) {
            handleSendFile(intent);
        }
    }

    public void onResume() {
        super.onResume();
        ulmAdapter.clear();
        ulmAdapter.addAll(UlmListSingleton.getUlmList(getApplicationContext()));
    }


   /* private void reloadList() {
        if (!ulmSaver.loadulmsFromStorage())
            Toast.makeText(getApplicationContext(), "Impossible de charger une sauvegarde", Toast.LENGTH_SHORT).show();
        ulmAdapter.clear();
        ulmAdapter.addAll(ulmSaver.getulms());
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ulmAdapter.setSelectedIndex(position);
        // Log.i("ulmListActivity", "Selected " + selectedulm.getNom());
    }


    private void btnShareulmFile(View view) {

        File file = UlmSaver.getUlmFile(getApplicationContext());

        if (file.exists()) {
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), "fr.ulm.centrage.provider", file);
            Intent shareIntent = new Intent();
            shareIntent.setType("*/*")
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_STREAM, uri).setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .putExtra(Intent.EXTRA_TITLE, file.getAbsolutePath());
            startActivity(shareIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Aucun fichier trouvé", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnAddulm(View view) {
        Intent intent = new Intent(UlmListActivity.this, UlmEditorActivity.class);
        startActivity(intent);
    }

    public void btnEditulm(View view) {
        if (ulmAdapter.getSelectedIndex() != -1) {
            Intent intent = new Intent(UlmListActivity.this, UlmEditorActivity.class);
            intent.putExtra("ulm", ulmAdapter.getSelectedIndex());
            startActivity(intent);
        } else
            Toast.makeText(getApplicationContext(), "Selectionnez un ulm", Toast.LENGTH_SHORT).show();
    }

    public void btnWatchulm(View view) {
        if (ulmAdapter.getSelectedIndex() != -1) {
           /* Intent intent = new Intent(ulmListActivity.this, CentrageActivity.class);
            startActivity(intent);*/

            Intent intent = new Intent(UlmListActivity.this, CentrageActivity.class);
            intent.putExtra("ulm", ulmAdapter.getSelectedIndex());

            startActivity(intent);
            /*getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, CentrageFragment.class, bundle)
                    .commit();*/
        } else
            Toast.makeText(getApplicationContext(), "Selectionnez un ulm", Toast.LENGTH_SHORT).show();
    }

    public void btnDeleteulm(View view) {
        int index = ulmAdapter.getSelectedIndex();
        if (index != -1) {

            ArrayList<Ulm> list = UlmListSingleton.getUlmList(getApplicationContext());
            list.remove(index);
            UlmSaver.saveFile(list, getApplicationContext());

            ulmAdapter.remove(ulmAdapter.getItem(index));
        } else
            Toast.makeText(getApplicationContext(), "Selectionnez un ulm", Toast.LENGTH_SHORT).show();
    }

    private void handleSendFile(Intent intent) {
        Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (uri == null)
            return;

        boolean success = false;

        ObjectInputStream objInput = null;
        try {

            InputStream inputStream = getContentResolver().openInputStream(uri); // On ouvre le fichier
            objInput = new ObjectInputStream(inputStream); // On verifie que le fichier peut être lu

            ArrayList<Ulm> sharedulms = (ArrayList<Ulm>) objInput.readObject();

            ArrayList<Ulm> ulms = UlmListSingleton.getUlmList(getApplicationContext());

            // Si deux ulms ont les mêmes noms, on remplace
            // Sinon on l'ajoute au fichier
            for (int i = 0; i < sharedulms.size(); i++) {
                for (int j = 0; j < ulms.size(); j++) {
                    if (ulms.get(j).getNom().equals(sharedulms.get(i).getNom())) {
                        ulms.set(j, sharedulms.get(i));
                        sharedulms.remove(i);
                    }
                }
            }

            ulms.addAll(sharedulms); // Ajoute les nouveaux ulms
            success = UlmSaver.saveFile(ulms, getApplicationContext());

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

        if (success)
            Toast.makeText(getApplicationContext(), "Fichier chargé", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Erreur de chargement", Toast.LENGTH_SHORT).show();

    }


}
