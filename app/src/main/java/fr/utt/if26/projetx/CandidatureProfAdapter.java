package fr.utt.if26.projetx;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import fr.utt.if26.projetx.utils.HttpUtils;

/**
 * Created by raphael on 22/01/2018.
 */

public class CandidatureProfAdapter  extends BaseAdapter implements ListAdapter {
    private ArrayList<HashMap<String, Object>> candidatures = new ArrayList<>();
    private Context context;
    private Gson gson = new Gson();

    public CandidatureProfAdapter(ArrayList<HashMap<String, Object>> candidatures, Context context) {
        this.candidatures = candidatures;
        this.context = context;
    }

    @Override
    public int getCount() {
        return candidatures.size();
    }

    @Override
    public Object getItem(int pos) {
        return candidatures.get(pos);
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
            view = inflater.inflate(R.layout.list_candidature_prof, null);
        }

        //Handle buttons and add onClickListeners
        TextView textView = view.findViewById(R.id.textview_candidature);
        textView.setText((String)candidatures.get(position).get("candidature"));

        Button buttonAccept = view.findViewById(R.id.button_accept_candidature);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Integer[]> candidatureId = new HashMap<>();
                Integer[] ids = {(Integer)candidatures.get(position).get("id")};
                candidatureId.put("candidatures", ids);
                String json = gson.toJson(candidatureId);
                Log.d("body", json);
                try {
                    StringEntity entity = new StringEntity(json);
                    HttpUtils.patch(context, "candidature/validate", null, entity, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // If the response is JSONObject instead of expected JSONArray
                            Toast.makeText(context, "Vous avez bien validé cette candidature.", Toast.LENGTH_LONG).show();
                            candidatures.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // If the response is JSONObject instead of expected JSONArray
                            Toast.makeText(context, "Vous avez bien validé cette candidature.", Toast.LENGTH_LONG).show();
                            candidatures.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                } catch (UnsupportedEncodingException err) {
                    err.printStackTrace();
                }
            }
        });

        Button buttonRefuse = view.findViewById(R.id.button_refuse_candidature);
        buttonRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Integer[]> candidatureId = new HashMap<>();
                Integer[] ids = {(Integer)candidatures.get(position).get("id")};
                candidatureId.put("candidatures", ids);
                String json = gson.toJson(candidatureId);
                Log.d("body", json);
                try {
                    StringEntity entity = new StringEntity(json);
                    HttpUtils.patch(context, "candidature/refuse", null, entity, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // If the response is JSONObject instead of expected JSONArray
                            Toast.makeText(context, "Vous avez bien refusé cette candidature.", Toast.LENGTH_LONG).show();
                            candidatures.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // If the response is JSONObject instead of expected JSONArray
                            Toast.makeText(context, "Vous avez bien refusé cette candidature.", Toast.LENGTH_LONG).show();
                            candidatures.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                } catch (UnsupportedEncodingException err) {
                    err.printStackTrace();
                }
            }
        });

        return view;
    }

}
