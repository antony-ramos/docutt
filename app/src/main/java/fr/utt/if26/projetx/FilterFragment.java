package fr.utt.if26.projetx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.utt.if26.projetx.database.Filtre;

public class FilterFragment extends Fragment {

    EditText filterName;
    Button validate;
    RecyclerView recyclerView;
    ArrayList<String> UE = new ArrayList<>();
    ArrayList<String> horairesNonVoulus = new ArrayList<>();
    GridLayout gridLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Filtres");
        filterName = getActivity().findViewById(R.id.filter_name);
        validate = getActivity().findViewById(R.id.validate_filter);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterName.getText().toString().trim().length() > 0) {
                    Filtre filtre = new Filtre(filterName.getText().toString(), UE, horairesNonVoulus);
                    filtre.save();
                    Toast.makeText(getContext(), "Filtre enregistré", Toast.LENGTH_LONG).show();
                } else Toast.makeText(getContext(), "Veuillez donner un nom à votre filtre.", Toast.LENGTH_LONG).show();
            }
        });
        gridLayout = getActivity().findViewById(R.id.grid_horaires);
        populateHoraires();
        recyclerView = getActivity().findViewById(R.id.ue_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
        prepareListData();
        ChipsAdapter adapter = new ChipsAdapter(UE);
        recyclerView.setAdapter(adapter);
    }

    public void populateHoraires() {
        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
        final String[] horaires = {"8-10h", "10-12h", "12-14h", "14-16h", "16-18h", "18-20h"};
        for (int i = 0; i < jours.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(jours[i]);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.rightMargin = 5;
            param.topMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(0);
            param.rowSpec = GridLayout.spec(i + 1);
            textView.setLayoutParams(param);
            gridLayout.addView(textView);
        }

        for (int i = 0; i < horaires.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(horaires[i]);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.rightMargin = 5;
            param.topMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(i + 1);
            param.rowSpec = GridLayout.spec(0);
            textView.setLayoutParams(param);
            gridLayout.addView(textView);
        }

        for (int r = 1; r < jours.length; r++)
            for (int c = 1; c < horaires.length + 1; c++) {
                    final String horaireNonVoulu = jours[r-1] + "$" + horaires[c - 1];
                    CheckBox checkBox = new CheckBox(getContext());
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) horairesNonVoulus.add(horaireNonVoulu);
                            else horairesNonVoulus.remove(horaireNonVoulu);
                        }
                    });
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                    param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.rightMargin = 5;
                    param.topMargin = 5;
                    param.setGravity(Gravity.CENTER);
                    param.columnSpec = GridLayout.spec(c);
                    param.rowSpec = GridLayout.spec(r);
                    checkBox.setLayoutParams(param);
                    gridLayout.addView(checkBox);
            }

        for (int c = 1; c < 3; c++) {
            final String horaireNonVoulu = jours[5] + "$" + horaires[c - 1];
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) horairesNonVoulus.add(horaireNonVoulu);
                    else horairesNonVoulus.remove(horaireNonVoulu);

                }
            });
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.rightMargin = 5;
            param.topMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(6);
            checkBox.setLayoutParams(param);
            gridLayout.addView(checkBox);
        }
    }

    private void prepareListData() {
        UE.add("LO02");
        UE.add("IF26");
        UE.add("RE12");
        UE.add("AAAA");
        UE.add("BBBB");
        UE.add("CCCC");
        UE.add("DDDD");
        UE.add("FFFF");
    }
}
