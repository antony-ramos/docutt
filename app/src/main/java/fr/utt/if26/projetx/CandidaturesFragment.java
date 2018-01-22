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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import fr.utt.if26.projetx.database.Filtre;
import fr.utt.if26.projetx.utils.HttpUtils;

public class CandidaturesFragment extends Fragment {

    private ListView candidaturesList;
    private ArrayList<String> candidaturesFormatees = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> candidatures = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_candidatures, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Candidatures");
        candidaturesList = getActivity().findViewById(R.id.liste_candidatures);
        getCandidatures();
    }

    private void getCandidatures() {
        String url = "";
        switch (getArguments().getString("candidatureType")) {
            case "todo":
                url = "/candidature/my/todo";
                break;
            case "validate":
                url = "/candidature/my/validate";
                break;
            case "done":
                url = "/candidature/my/done";
                break;
        }
        HttpUtils.get(url, null, new JsonHttpResponseHandler() {
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
                        String ue = response.getJSONObject(i).getJSONObject("creneau").getString("ue");
                        String date = response.getJSONObject(i).getJSONObject("creneau").getString("date");
                        int heure_debut = response.getJSONObject(i).getJSONObject("creneau").getInt("heure_debut");
                        int heure_fin = heure_debut + response.getJSONObject(i).getJSONObject("creneau").getInt("editDuree");
                        candidaturesFormatees.add(ue + ": " + date + " - " + heure_debut + "h - " + heure_fin + "h");
                        HashMap<String, Object> candidature = new HashMap<>();
                        candidature.put("id", response.getJSONObject(i).getInt("id"));
                        candidature.put("candidature", ue + ": " + date + " - " + heure_debut + "h - " + heure_fin + "h");
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
        if(getArguments().getString("candidatureType") == "validate") {
            CandidatureListAdapter adapter = new CandidatureListAdapter(candidatures, "done", getContext());
            candidaturesList.setAdapter(adapter);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, candidaturesFormatees);
            candidaturesList.setAdapter(adapter);
        }
    }

}
