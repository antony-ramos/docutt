package fr.utt.if26.projetx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import fr.utt.if26.projetx.database.Filtre;

public class CandidateFragment extends Fragment {

    private ListView creneauList;
    private Button btnCandidater;
    private String name = null;

    private ArrayList<String> UE = new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> horairesNonVoulus = new HashMap<>();
    private ArrayList<Map<String, Object>> creneaux = new ArrayList<>();

    private boolean charged = false;

    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_candidate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Candidate");
        creneauList = getActivity().findViewById(R.id.creneau_list);
        btnCandidater = getActivity().findViewById(R.id.btn_candidater);
        prepareFilter();
        getCreneauFromFilter();
    }

    private void prepareFilter() {
        name = getArguments().getString("filterName");
        Filtre filtre = Filtre.find(Filtre.class, "name = ?", name).get(0);
        UE = gson.fromJson(filtre.getUeChoisies(), ArrayList.class);
        final HashMap<String, ArrayList<Double>> horairesNonVoulusFromJson = gson.fromJson(filtre.getHorairesNonVoulus(), horairesNonVoulus.getClass());

        final String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
        for (int i = 0; i < jours.length; i++) {
            horairesNonVoulus.put(jours[i], new ArrayList<Integer>());
            for (int j = 0; j < horairesNonVoulusFromJson.get(jours[i]).size(); j++)
                horairesNonVoulus.get(jours[i]).add(horairesNonVoulusFromJson.get(jours[i]).get(j).intValue());
        }
    }

    private void getCreneauFromFilter() {
        HashMap filtre = new HashMap();
        filtre.put("ue", UE);
        filtre.put("horairesNonVoulus", horairesNonVoulus);
        String json = gson.toJson(filtre);
        try {
            StringEntity entity = new StringEntity(json);
            HttpUtils.post(getContext(), "creneau/filtre", null, entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    // If the response is JSONObject instead of expected JSONArray
                    Log.d("object", response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // If the response is JSONObject instead of expected JSONArray
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Map<String, Object> creneau = new HashMap();
                            creneau.put("id", response.getJSONObject(i).getInt("id"));
                            creneau.put("date", response.getJSONObject(i).getString("date"));
                            creneau.put("heure_debut", response.getJSONObject(i).getInt("heure_debut"));
                            creneau.put("duree", response.getJSONObject(i).getInt("duree"));
                            creneau.put("ue", response.getJSONObject(i).getString("ue"));
                            creneaux.add(creneau);
                        } catch (JSONException err) {
                            err.printStackTrace();
                        }
                    }
                }
            });
        } catch (UnsupportedEncodingException err) {
            err.printStackTrace();
        }

    }
}
