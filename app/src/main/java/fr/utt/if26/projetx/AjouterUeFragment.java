package fr.utt.if26.projetx;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import fr.utt.if26.projetx.utils.HttpUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class AjouterUeFragment extends Fragment {

    private EditText ueName;
    private Button validate;
    private boolean editing = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_ajouter_ue, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Ajouter une UE");

        ueName = getActivity().findViewById(R.id.ue_name);
        validate = getActivity().findViewById(R.id.validate_ue);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ueName.getText().toString().trim().length() > 0) createUe();
                else Toast.makeText(getContext(), "Veuillez donner un nom à votre UE.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void createUe() {
        Gson gson = new Gson();
        HashMap ue = new HashMap();
        ue.put("nom", ueName.getText().toString());
        String json = gson.toJson(ue);
        try {
            StringEntity entity = new StringEntity(json);
            HttpUtils.post(getContext(), "ue", null, entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Toast.makeText(getContext(), ueName.getText().toString() + " créée", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Toast.makeText(getContext(), ueName.getText().toString() + " créée", Toast.LENGTH_LONG).show();
                }
            });
        } catch (UnsupportedEncodingException err) {
            err.printStackTrace();
        }
    }

}
