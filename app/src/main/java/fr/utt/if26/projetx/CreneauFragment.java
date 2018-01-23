package fr.utt.if26.projetx;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import fr.utt.if26.projetx.utils.HttpUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreneauFragment extends Fragment {

    Spinner spinnerUe;
    EditText editHeureDebut;
    EditText editDuree;
    Button buttonDate;
    Button buttonValidate;
    List<String> spinnerArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creneau, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Créer un créneau");
        spinnerUe = getActivity().findViewById(R.id.spinner_ue);
        populateUeSpinner();
        editHeureDebut = getActivity().findViewById(R.id.heure_debut);
        editDuree = getActivity().findViewById(R.id.duree);
        buttonDate = getActivity().findViewById(R.id.button_date);
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        buttonDate.setText(dt.format(Calendar.getInstance().getTime()));
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        buttonValidate = getActivity().findViewById(R.id.validate_creneau);
        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(editHeureDebut.getText().toString().trim().length() == 0 || editDuree.getText().toString().trim().length() == 0)
                    Toast.makeText(getContext(), "Veuillez remplir tous les champs.", Toast.LENGTH_LONG).show();
                else {
                    String ueName = spinnerUe.getSelectedItem().toString();
                    int heureDebut = Integer.parseInt(editHeureDebut.getText().toString());
                    int duree = Integer.parseInt(editDuree.getText().toString());
                    String date = buttonDate.getText().toString();
                    if(heureDebut > 20 || heureDebut < 8) {
                        Toast.makeText(getContext(), "Veuillez saisir une heure de début valide.", Toast.LENGTH_LONG).show();
                    } else if(duree < 1 || duree > 4)
                        Toast.makeText(getContext(), "Veuillez donner une durée valide", Toast.LENGTH_LONG).show();
                    else createCreneau(ueName, heureDebut, duree, date);
                }
            }
        });
    }

    private void populateUeSpinner() {
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
                        spinnerArray.add(response.getJSONObject(i).getString("nom"));
                    } catch (JSONException err) {
                        err.printStackTrace();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if(spinnerUe.getAdapter() != null) spinnerUe.removeAllViews();
                spinnerUe.setAdapter(adapter);
            }
        });
    }

    private void createCreneau(String ueName, int heureDebut, int duree, String date) {

        Gson gson = new Gson();
        HashMap creneau = new HashMap();
        creneau.put("nom_ue", ueName);
        creneau.put("heure_debut", heureDebut);
        creneau.put("duree", duree);
        try {
            creneau.put("date", new SimpleDateFormat("dd/MM/yyyy").parse(date));
        } catch (ParseException err) {
            err.printStackTrace();
        }
        String json = gson.toJson(creneau);
        Log.d(json, json);
        try {
            StringEntity entity = new StringEntity(json);
            HttpUtils.post(getContext(), "creneau", null, entity, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.d("object", response.toString());
                    Toast.makeText(getContext(), "Créneau créé", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.d("object", response.toString());
                    Toast.makeText(getContext(), "Créneau créé", Toast.LENGTH_LONG).show();
                }
            });
        } catch (UnsupportedEncodingException err) {
            err.printStackTrace();
        }
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            Button buttonDate = getActivity().findViewById(R.id.button_date);
            final Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
            buttonDate.setText(dt.format(calendar.getTime()));
        }

    }

}
