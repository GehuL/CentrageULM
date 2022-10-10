package fr.ulm.centrage.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import fr.ulm.centrage.R;
import fr.ulm.centrage.data.Ulm;

public class UlmAdapter extends ArrayAdapter<Ulm> {

    private final int mRessourceLayout;
    private final Context mContext;

    private int selectedIndex = -1;

    public UlmAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.mRessourceLayout = resource;
        mContext = context;
    }

    public void setSelectedIndex(int i) {
        selectedIndex = i;
        notifyDataSetChanged();
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(mRessourceLayout, null);
        }

        Ulm p = getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.item_name);
            TextView tt2 = v.findViewById(R.id.item_modif);
            TextView tt3 = v.findViewById(R.id.item_info);
            TextView tt4 = v.findViewById(R.id.item_masse);

            if (tt1 != null) {
                tt1.setText(p.getNom());
            }

            if (tt2 != null) {
                tt2.setText(String.format("CG=%.2f cm", p.getCentreGravite()).replace(".00", ""));
                if (p.getCentreGravite() == 0f) {
                    tt2.setText("CG non calculé");
                    tt2.setTextColor(getContext().getResources().getColor(R.color.yellow));
                } else if (p.isCGValid()) {
                    tt2.setTextColor(getContext().getResources().getColor(R.color.green));
                } else {
                    tt2.setTextColor(getContext().getResources().getColor(R.color.red));
                }
            }

            if (tt3 != null) {
                tt3.setText(String.valueOf(p.getDateModif()));
            }

            if (tt4 != null) {
                float masse = p.calculerMasseTT();
                tt4.setText(String.format("Masse TT=%.2f kg", masse).replace(".00", ""));
                if (masse > p.getMasseMax()) {
                    tt4.setTextColor(getContext().getResources().getColor(R.color.red));
                } else {
                    tt4.setTextColor(getContext().getResources().getColor(R.color.green));
                }
            }

            if (selectedIndex != -1 && position == selectedIndex) {
                v.setBackgroundColor(mContext.getResources().getColor(R.color.gray_400));
            } else {
                v.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }

        }
        return v;
    }


}
