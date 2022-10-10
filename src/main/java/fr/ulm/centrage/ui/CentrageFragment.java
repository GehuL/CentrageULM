package fr.ulm.centrage.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fr.ulm.centrage.R;
import fr.ulm.centrage.data.Ulm;
import fr.ulm.centrage.data.UlmListSingleton;
import fr.ulm.centrage.data.UlmSaver;

public class CentrageFragment extends Fragment {

    private EditText editRoulette, editRoues, editPax, editEssAil, editEssAv, editBag, editPilote;

    private Ulm ulm;

    private int index = -1;

    private AlertDialog helpDialog;

    public CentrageFragment() {
        super(R.layout.centrage_layout);
        //getParentFragmentManager().beginTransaction().remove(this);
    }

    public void onViewCreated(View view, Bundle bundle) {

        helpDialog = createHelp();

        editRoues = getActivity().findViewById(R.id.rouesMasse);
        editRoulette = getActivity().findViewById(R.id.rouletteMasse);
        editPax = getActivity().findViewById(R.id.passagerMasse);
        editEssAil = getActivity().findViewById(R.id.essenceAilesMasse);
        editEssAv = getActivity().findViewById(R.id.essenceAvantMasse);
        editBag = getActivity().findViewById(R.id.bagMasse);
        editPilote = getActivity().findViewById(R.id.piloteMasse);

        // Si l'index ne correspond pas à un élément, on créer alors un nouveau ULM.
        try {
            index = getArguments().getInt("ulm", -1);
            ulm = UlmListSingleton.getUlmList(getActivity().getApplicationContext()).get(index);

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

        getActivity().findViewById(R.id.btnCG).setOnClickListener(this::calculAndSave);
        getActivity().findViewById(R.id.retour_centrage).setOnClickListener(v ->
                getParentFragmentManager().beginTransaction().remove(this).commit());

        getActivity().findViewById(R.id.help).setOnClickListener(v -> helpDialog.show());
    }


    public AlertDialog createHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(getLayoutInflater().inflate(R.layout.help_layout, null)).create();
    }

    private void calculAndSave(View view) {

        ulm.getElement(Ulm.ElementDefaut.ROUES).masse = UlmEditorActivity.parseFloat(editRoues.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.ROULETTE).masse = UlmEditorActivity.parseFloat(editRoulette.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.PAX).masse = UlmEditorActivity.parseFloat(editPax.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.ESS_AV).masse = UlmEditorActivity.parseFloat(editEssAv.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.ESS_AIL).masse = UlmEditorActivity.parseFloat(editEssAil.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.BAG).masse = UlmEditorActivity.parseFloat(editBag.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.PILOTE).masse = UlmEditorActivity.parseFloat(editPilote.getText().toString());

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
        if (ulm != null) {

            ulm.setDateModif(Ulm.getDate());
            ArrayList<Ulm> ulms = UlmListSingleton.getUlmList(getActivity().getApplicationContext());
            ulms.set(index, ulm);

            if (!UlmSaver.saveFile(ulms, getActivity().getApplicationContext()))
                Toast.makeText(getActivity().getApplicationContext(), "Impossible de sauvegarder", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity().getApplicationContext(), "Ulm sauvegardé", Toast.LENGTH_SHORT).show();
        }


    }

}
