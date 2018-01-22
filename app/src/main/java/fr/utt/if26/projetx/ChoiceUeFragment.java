package fr.utt.if26.projetx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import fr.utt.if26.projetx.utils.HttpUtils;

public class ChoiceUeFragment extends Fragment {

    private ArrayList<String> UE = new ArrayList<>();
    private ListView listView;

    private ButtonUeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_choice_ue, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Choisir une UE");
        listView = getActivity().findViewById(R.id.choice_ue);
        adapter = new ButtonUeAdapter(UE, getContext(), "ChoiceUeFragment", chooseRedirection());
        listView.setAdapter(adapter);
        populateUe();
    }

    private void populateUe() {
        HttpUtils.get("ue/my", null, new JsonHttpResponseHandler() {
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
                        UE.add(response.getJSONObject(i).getString("nom"));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException err) {
                        err.printStackTrace();
                    }
                }
            }
        });
    }

    private String chooseRedirection() {
        switch (getArguments().getString("from")){
            case "nav_professor_candidatures":
                return "CandidatureProfFragment";
            case "nav_creneaux":
                return "CreneauListFragment";
            default:
                return "ChoiceUeFragment";
        }
    }

}
