package fr.utt.if26.projetx;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import fr.utt.if26.projetx.database.Filtre;
import fr.utt.if26.projetx.utils.HttpUtils;

public class CandidateFragment extends Fragment {

    private ListView creneauList;
    private Button btnCandidater;

    private CheckboxAdapter adapter;

    private ArrayList<String> UE = new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> horairesNonVoulus = new HashMap<>();
    private ArrayList<HashMap<String, Object>> creneaux = new ArrayList<>();

    private ArrayList<Integer> idsCreneaux = new ArrayList<>();

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
        getActivity().setTitle("Candidater");
        creneauList = getActivity().findViewById(R.id.creneau_list);
        btnCandidater = getActivity().findViewById(R.id.btn_candidater);
        btnCandidater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candidater();
            }
        });
        prepareFilter();
        getCreneauFromFilter();
    }

    private void prepareFilter() {
        Filtre filtre = Filtre.find(Filtre.class, "name = ?", getArguments().getString("filterName")).get(0);
        UE = gson.fromJson(filtre.getUeChoisies(), ArrayList.class);
        final HashMap<String, ArrayList<Double>> horairesNonVoulusFromJson = gson.fromJson(filtre.getHorairesNonVoulus(), horairesNonVoulus.getClass());

        final String[] jours = {"lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"};
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
        Log.d("json", json);
        try {
            StringEntity entity = new StringEntity(json);
            HttpUtils.post(getContext(), "creneau/filtre", null, entity, new JsonHttpResponseHandler() {
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
                            HashMap<String, Object> creneau = new HashMap();
                            creneau.put("id", response.getJSONObject(i).getInt("id"));
                            creneau.put("date", response.getJSONObject(i).getString("date"));
                            creneau.put("heure_debut", response.getJSONObject(i).getInt("heure_debut"));
                            creneau.put("editDuree", response.getJSONObject(i).getInt("editDuree"));
                            creneau.put("ue", response.getJSONObject(i).getString("ue"));
                            creneaux.add(creneau);
                        } catch (JSONException err) {
                            err.printStackTrace();
                        }
                    }
                    populateCreneau();
                }
            });
        } catch (UnsupportedEncodingException err) {
            err.printStackTrace();
        }

    }

    private void populateCreneau() {
        adapter = new CheckboxAdapter(creneaux, getContext());
        creneauList.setAdapter(adapter);
    }


    private void refresh() {
        creneaux = new ArrayList<>();
        idsCreneaux = new ArrayList<>();
        getCreneauFromFilter();
        adapter.notifyDataSetChanged();
    }

    private void candidater() {
        HashMap candidatures = new HashMap();
        candidatures.put("creneaux", idsCreneaux);
        String json = gson.toJson(candidatures);
        try {
            StringEntity entity = new StringEntity(json);
            HttpUtils.post(getContext(), "creneau/candidate", null, entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Toast.makeText(getContext(), "Vous avez bien candidaté à ces creneaux.", Toast.LENGTH_LONG).show();
                    refresh();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Toast.makeText(getContext(), "Vous avez bien candidaté à ces creneaux.", Toast.LENGTH_LONG).show();
                    refresh();
                }
            });
        } catch (UnsupportedEncodingException err) {
            err.printStackTrace();
        }
    }

    private class CheckboxAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<HashMap<String, Object>> creneaux;
        private Context context;

        public CheckboxAdapter(ArrayList<HashMap<String, Object>> creneaux, Context context) {
            this.creneaux = creneaux;
            this.context = context;
        }

        @Override
        public int getCount() {
            return creneaux.size();
        }

        @Override
        public Object getItem(int pos) {
            return creneaux.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return 0;
            //just return 0 if your list items do not have an Id variable.
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_creneau, null);
            }

            CheckBox checkbox = view.findViewById(R.id.checkbox_creneau);
            String ueName = (String)creneaux.get(position).get("ue");
            String date = (String)creneaux.get(position).get("date");
            int heure_debut = (int)creneaux.get(position).get("heure_debut");
            int heure_fin = (int)creneaux.get(position).get("heure_debut") + (int)creneaux.get(position).get("editDuree");
            String texte = ueName + ": " + date + " - " + heure_debut + "h - " + heure_fin + "h";
            checkbox.setText(texte);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idsCreneaux.add((Integer)creneaux.get(position).get("id"));
                }
            });

            return view;
        }

    }

}