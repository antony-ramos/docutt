package fr.utt.if26.projetx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import fr.utt.if26.projetx.database.Filtre;
import fr.utt.if26.projetx.utils.Router;

public class FilterFragment extends Fragment {

    private EditText filterName;
    private Button validate;
    private RecyclerView recyclerView;
    private GridLayout gridLayout;

    private ArrayList<String> UE = new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> horairesNonVoulus = new HashMap<>();

    public final String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
    public final String[] horaires = {"8-10h", "10-12h", "12-14h", "14-16h", "16-18h", "18-20h"};
    public final Integer[] heureDebut = {8, 10, 12, 14, 16, 18};

    private Gson gson = new Gson();
    private boolean editing = false;
    private Filtre filtre = null;

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
                if (filterName.getText().toString().trim().length() > 0) {
                    if (editing == true) editFilter();
                    else createFilter();
                } else
                    Toast.makeText(getContext(), "Veuillez donner un nom à votre filtre.", Toast.LENGTH_LONG).show();
            }
        });
        gridLayout = getActivity().findViewById(R.id.grid_horaires);
        recyclerView = getActivity().findViewById(R.id.ue_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
        if (getArguments().getString("filterName") != null)
            prepareFilter(getArguments().getString("filterName"));
        else prepareListData();
        populateHoraires();
        ChipsAdapter adapter = new ChipsAdapter(UE);
        recyclerView.setAdapter(adapter);
    }

    public void populateHoraires() {
        if (horairesNonVoulus.isEmpty())
            for (int i = 0; i < jours.length; i++)
                horairesNonVoulus.put(jours[i], new ArrayList<Integer>());

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
                final String jourNonVoulu = jours[r - 1];
                final Integer heureDebutNonVoulue = heureDebut[c - 1];
                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) horairesNonVoulus.get(jourNonVoulu).add(heureDebutNonVoulue);
                        else horairesNonVoulus.get(jourNonVoulu).remove(heureDebutNonVoulue);
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
                if (horairesNonVoulus.get(jourNonVoulu).contains(heureDebutNonVoulue))
                    checkBox.setChecked(true);
                gridLayout.addView(checkBox);
            }

        for (int c = 1; c < 3; c++) {
            final String jourNonVoulu = jours[5];
            final Integer heureDebutNonVoulue = heureDebut[c - 1];
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) horairesNonVoulus.get(jourNonVoulu).add(heureDebutNonVoulue);
                    else horairesNonVoulus.get(jourNonVoulu).remove(heureDebutNonVoulue);

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
            if (horairesNonVoulus.get(jourNonVoulu).contains(heureDebutNonVoulue))
                checkBox.setChecked(true);
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

    private void prepareFilter(String name) {
        editing = true;
        filterName.setText(name);
        filtre = Filtre.find(Filtre.class, "name = ?", name).get(0);
        UE = gson.fromJson(filtre.getUeChoisies(), ArrayList.class);
        final HashMap<String, ArrayList<Double>> horairesNonVoulusFromJson = gson.fromJson(filtre.getHorairesNonVoulus(), horairesNonVoulus.getClass());

        for (int i = 0; i < jours.length; i++) {
            horairesNonVoulus.put(jours[i], new ArrayList<Integer>());
            for (int j = 0; j < horairesNonVoulusFromJson.get(jours[i]).size(); j++)
                horairesNonVoulus.get(jours[i]).add(horairesNonVoulusFromJson.get(jours[i]).get(j).intValue());
        }
    }

    private void createFilter() {
        filtre = new Filtre(filterName.getText().toString(), gson.toJson(UE), gson.toJson(horairesNonVoulus));
        filtre.save();
        Toast.makeText(getContext(), "Filtre enregistré", Toast.LENGTH_LONG).show();
        Router.replaceFragment("FilterFragment", "ChoiceFilterFragment", null, getContext());
    }

    private void editFilter() {
        filtre.setHorairesNonVoulus(gson.toJson(horairesNonVoulus));
        filtre.setName(filterName.getText().toString());
        filtre.setUeChoisies(gson.toJson(UE));
        filtre.save();
    }
}
