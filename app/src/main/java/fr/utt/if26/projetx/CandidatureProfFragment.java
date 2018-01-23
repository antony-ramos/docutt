package fr.utt.if26.projetx;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import fr.utt.if26.projetx.utils.HttpUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class CandidatureProfFragment extends Fragment {

    private ListView candidaturesList;
    private ArrayList<String> candidaturesFormatees;
    private ArrayList<HashMap<String, Object>> candidatures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_candidature_prof, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Candidatures");
        candidaturesFormatees = new ArrayList<>();
        candidatures = new ArrayList<>();
        candidaturesList = getActivity().findViewById(R.id.liste_candidatures_prof);
        getCandidatures();
    }

    private void getCandidatures() {
        HttpUtils.get("candidature/todo/" + getArguments().getString("ue"), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("object", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("object", response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String nomDoctorant = response.getJSONObject(i).getJSONObject("utilisateur").getString("nom");
                        String prenomDoctorant = response.getJSONObject(i).getJSONObject("utilisateur").getString("prenom");
                        String date = response.getJSONObject(i).getJSONObject("creneau").getString("date");
                        int heure_debut = response.getJSONObject(i).getJSONObject("creneau").getInt("heure_debut");
                        int heure_fin = heure_debut + response.getJSONObject(i).getJSONObject("creneau").getInt("duree");
                        candidaturesFormatees.add(nomDoctorant + " " + prenomDoctorant + ": " + date + " - " + heure_debut + "h - " + heure_fin + "h");
                        HashMap<String, Object> candidature = new HashMap<>();
                        candidature.put("id", response.getJSONObject(i).getInt("id"));
                        candidature.put("candidature", nomDoctorant + " " + prenomDoctorant + ":\n" + date + " - " + heure_debut + "h - " + heure_fin + "h");
                        candidatures.add(candidature);
                    } catch (JSONException err) {
                        err.printStackTrace();
                    }
                }
                populateCandidatures();
            }
        });
    }

    private void populateCandidatures() {
        CandidatureProfAdapter adapter = new CandidatureProfAdapter(candidatures, getContext());
        candidaturesList.setAdapter(adapter);
    }

}
