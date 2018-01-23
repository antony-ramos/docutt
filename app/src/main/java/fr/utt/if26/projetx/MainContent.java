package fr.utt.if26.projetx;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import fr.utt.if26.projetx.utils.HttpUtils;

/**
 * Created by Antony RAMOS on 10/11/2017.
 */

public class MainContent extends Fragment {

    TextView textHeuresEffectuees;
    TextView textView;
    ListView prochainesHeures;
    ArrayList<String> futuresCreneaux;
    int heuresEffectues;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.content_infos_main, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Docutt");
        futuresCreneaux = new ArrayList<>();
        textHeuresEffectuees = getActivity().findViewById(R.id.textview_heures_effectues);
        prochainesHeures = getActivity().findViewById(R.id.listView_nexthours);
        textView = getActivity().findViewById(R.id.textView4);
        getNextCreneau();
    }

    private void getNextCreneau() {
        HttpUtils.get("utilisateurs/info", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    if (response.has("dureeEffectuee")) {
                        heuresEffectues = response.getInt("dureeEffectuee");
                        JSONArray creneaux = response.getJSONArray("creneauxAfaire");
                        for (int i = 0; i < creneaux.length(); i++) {
                            String ue = creneaux.getJSONObject(i).getString("ue");
                            String date = creneaux.getJSONObject(i).getString("date");
                            int heure_debut = creneaux.getJSONObject(i).getInt("heure_debut");
                            int heure_fin = heure_debut + creneaux.getJSONObject(i).getInt("duree");
                            futuresCreneaux.add(ue + ": " + date + " - " + heure_debut + "h - " + heure_fin + "h");
                        }
                        populateContent();
                    } else {
                        textView.setVisibility(TextView.INVISIBLE);
                        textHeuresEffectuees.setText("Vous avez " + response.getString("candidaturesATraiter") + " candidatures à traiter.");
                    }
                } catch (JSONException err) {
                    err.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("object", response.toString());
            }
        });
    }

    private void populateContent() {
        if (prochainesHeures.getAdapter() != null) {
            prochainesHeures.removeAllViews();
        }
        prochainesHeures.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, futuresCreneaux));
        textHeuresEffectuees.setText("Vous avez déjà effectué " + heuresEffectues + " heures.");
    }
}
