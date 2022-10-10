package fr.ulm.centrage.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import fr.ulm.centrage.R;
import fr.ulm.centrage.data.Ulm;
import fr.ulm.centrage.data.UlmListSingleton;
import fr.ulm.centrage.data.UlmSaver;
import fr.ulm.centrage.util.Utils;

public class UlmEditorActivity extends AppCompatActivity {

    private EditText editNom, editRoulette, editRoues, editPax, editEssAil, editEssAv, editBag, editMax, editMin, editMasseMax;

    private TextInputLayout nomLayout;

    private int index = -1;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.create_avion_layout);

        editNom = findViewById(R.id.avionName);
        editRoues = findViewById(R.id.rouesDistance);
        editRoulette = findViewById(R.id.rouletteDistance);
        editPax = findViewById(R.id.passagersDistance);
        editEssAil = findViewById(R.id.essenceAilesDistance);
        editEssAv = findViewById(R.id.essenceAvant);
        editBag = findViewById(R.id.bagDistance);
        editMax = findViewById(R.id.max);
        editMin = findViewById(R.id.min);
        editMasseMax = findViewById(R.id.masseMax);

        editMasseMax.setHint("Masse max authorisé");

        nomLayout = findViewById(R.id.avion_name_layout);
        nomLayout.setErrorEnabled(true);

        // Si l'index ne correspond pas à un élément, on créer alors un nouveau ULM.
        try {
            index = getIntent().getIntExtra("ulm", -1);
            Ulm ulm = UlmListSingleton.getUlmList(getApplicationContext()).get(index);

            editNom.setText(ulm.getNom());
            editRoues.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.ROUES).levier));
            editRoulette.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.ROULETTE).levier));

            editPax.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.PAX).levier));

            editEssAil.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.ESS_AIL).levier));
            editEssAv.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.ESS_AV).levier));
            editBag.setText(String.valueOf(ulm.getElement(Ulm.ElementDefaut.BAG).levier));
            editMin.setText(String.valueOf(ulm.getMin()));
            editMax.setText(String.valueOf(ulm.getMax()));
            editMasseMax.setText(String.valueOf(ulm.getMasseMax()));

        } catch (ArrayIndexOutOfBoundsException e) {
            ((TextView) findViewById(R.id.titre)).setText("Créer un ULM");
        }

        findViewById(R.id.retour_centrage).setOnClickListener(v ->
                finish());
        findViewById(R.id.save).setOnClickListener(this::save);
    }

    private void save(View view) {

        String nom = editNom.getText().toString().trim();
        String roulette = editRoulette.getText().toString();
        String roues = editRoues.getText().toString();
        String min = editMin.getText().toString();
        String max = editMax.getText().toString();

        // Verifie les inputs
        if (nom.length() == 0 || nom.length() > 15) {
            nomLayout.requestFocus();
            return;
        }

        Ulm ulm = new Ulm();

        ulm.setNom(nom);
        ulm.getElement(Ulm.ElementDefaut.ROUES).levier = Utils.parseFloat(roues);
        ulm.getElement(Ulm.ElementDefaut.ROULETTE).levier = Utils.parseFloat(roulette);
        ulm.getElement(Ulm.ElementDefaut.ESS_AV).levier = Utils.parseFloat(editEssAv.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.ESS_AIL).levier = Utils.parseFloat(editEssAil.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.BAG).levier = Utils.parseFloat(editBag.getText().toString());

        // Le bras de levier est identique pour le passager et le pilote
        float distancePersonnes = Utils.parseFloat(editPax.getText().toString());
        ulm.getElement(Ulm.ElementDefaut.PAX).levier = distancePersonnes;
        ulm.getElement(Ulm.ElementDefaut.PILOTE).levier = distancePersonnes;

        ulm.setMin(Utils.parseInt(min));
        ulm.setMax(Utils.parseInt(max));
        ulm.setMasseMax(Utils.parseFloat(editMasseMax.getText().toString()));

        ulm.setDateModif(Utils.getDate());

        ArrayList<Ulm> avions = UlmListSingleton.getUlmList(getApplicationContext());
        try {
            avions.set(index, ulm); // On effectue une modification à un avion
        } catch (ArrayIndexOutOfBoundsException e) {
            avions.add(ulm); // On ajoute l'avion à la liste
        }

        if (!UlmSaver.saveFile(avions, getApplicationContext()))
            Toast.makeText(getApplicationContext(), "Impossible de sauvegarder", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Ulm sauvegardé", Toast.LENGTH_SHORT).show();

        view.setEnabled(false);
    }

}
