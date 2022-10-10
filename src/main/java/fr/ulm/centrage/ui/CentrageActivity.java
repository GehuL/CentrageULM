package fr.ulm.centrage.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import fr.ulm.centrage.R;
import fr.ulm.centrage.data.Ulm;
import fr.ulm.centrage.data.UlmListSingleton;
import fr.ulm.centrage.data.UlmSaver;
import fr.ulm.centrage.util.Utils;

public class CentrageActivity extends Activity {

    private EditText editRoulette, editRoues, editPax, editEssAil, editEssAv, editBag, editPilote;

    private Ulm ulm;

    private int index = -1;

    private Dialog helpDialog;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.centrage_layout);

        helpDialog = createHelp();

        editRoues = findViewById(R.id.rouesMasse);
        editRoulette = findViewById(R.id.rouletteMasse);
        editPax = findViewById(R.id.passagerMasse);
        editEssAil = findViewById(R.id.essenceAilesMasse);
        editEssAv = findViewById(R.id.essenceAvantMasse);
        editBag = findViewById(R.id.bagMasse);
        editPilote = findViewById(R.id.piloteMasse);

        // Si l'index ne correspond pas à un élément, on créer alors un nouveau ULM.
        try {
            index = getIntent().getIntExtra("ulm", -1);
            ulm = UlmListSingleton.getUlmList(getApplicationContext()).get(index);

            editRoues.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.ROUES).masse));
            editRoulette.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.ROULETTE).masse));
            editPax.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.PAX).masse));
            editEssAil.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.ESS_AIL).masse));
            editEssAv.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.ESS_AV).masse));
            editBag.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.BAG).masse));
            editPilote.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.PILOTE).masse));

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("CentrageFragment: Aucun ulm selectioné");
        }

        findViewById(R.id.btnCG).setOnClickListener(this::calculAndSave);     // SAVE
        findViewById(R.id.retour_centrage).setOnClickListener(v -> finish()); // FINISH
        findViewById(R.id.help).setOnClickListener(v -> helpDialog.show());   // SHOW HELP
    }


    public Dialog createHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CentrageActivity.this);
        builder.setView(getLayoutInflater().inflate(R.layout.help_layout, null));
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        return dialog;
    }

    private void calculAndSave(View view) {

        if (ulm == null)
            return;

        ulm.getElement(Ulm.ElementDefaut.ROUES).masse = Utils.parseFloat(editRoues.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.ROULETTE).masse = Utils.parseFloat(editRoulette.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.PAX).masse = Utils.parseFloat(editPax.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.ESS_AV).masse = Utils.parseFloat(editEssAv.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.ESS_AIL).masse = Utils.parseFloat(editEssAil.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.BAG).masse = Utils.parseFloat(editBag.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.PILOTE).masse = Utils.parseFloat(editPilote.getText().toString());

        Button btn = (Button) view;

        //////////////////////////////////  CONTROLE MASSE /////////////////////////////////////////
        float cg = ulm.calculerCG();
        if (!(cg >= ulm.getMin() && cg < ulm.getMax())) {
            btn.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            btn.setBackgroundColor(getResources().getColor(R.color.green));
        }

        ((Button) view).setText("CG=" + cg + " cm" + " masse TT=" + ulm.calculerMasseTT() + " kg");

        ///////////////////////////////////// SAUVEGARDE ///////////////////////////////////////////
        ulm.setDateModif(Utils.getDate());
        ArrayList<Ulm> ulms = UlmListSingleton.getUlmList(getApplicationContext());
        ulms.set(index, ulm);

        if (!UlmSaver.saveFile(ulms, getApplicationContext()))
            Toast.makeText(getApplicationContext(), "Impossible de sauvegarder", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Ulm sauvegardé", Toast.LENGTH_SHORT).show();
    }

}
