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
public class CreneauListFragment extends Fragment {

    private ListView creneauList;
    private ArrayList<String> creneauxFormates = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> creneaux = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_creneau_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Candidatures");
        creneauList = getActivity().findViewById(R.id.liste_creneaux);
        getCreneaux();
    }

    private void getCreneaux() {
        HttpUtils.get("creneau/" + getArguments().getString("ue"), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("object", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // If the response is JSONObject instead of expected JSONArray
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String date = response.getJSONObject(i).getString("date");
                        int heure_debut = response.getJSONObject(i).getInt("heure_debut");
                        int heure_fin = heure_debut + response.getJSONObject(i).getInt("duree");
                        creneauxFormates.add(date + " - " + heure_debut + "h - " + heure_fin + "h");
                        HashMap<String, Object> candidature = new HashMap<>();
                        candidature.put("id", response.getJSONObject(i).getInt("id"));
                        candidature.put("creneau", date + " - " + heure_debut + "h - " + heure_fin + "h");
                        creneaux.add(candidature);
                    } catch (JSONException err) {
                        err.printStackTrace();
                    }
                }
                populateCandidatures();
            }
        });
    }

    private void populateCandidatures() {
        CreneauListAdapter adapter = new CreneauListAdapter(creneaux, getContext());
        if(creneauList.getAdapter() != null) creneauList.removeAllViews();
        creneauList.setAdapter(adapter);
    }

}

