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
 * Created by raphael on 21/01/2018.
 */

public class CandidatureListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<HashMap<String, Object>> candidatures = new ArrayList<>();
    private Context context;
    private String action;
    private Gson gson = new Gson();

    public CandidatureListAdapter(ArrayList<HashMap<String, Object>> candidatures, String action, Context context) {
        this.candidatures = candidatures;
        this.context = context;
        this.action = action;
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
            view = inflater.inflate(R.layout.list_textview_button, null);
        }

        //Handle buttons and add onClickListeners
        TextView textView = view.findViewById(R.id.text_list_textview_button);
        textView.setText((String)candidatures.get(position).get("candidature"));

        Button button = view.findViewById(R.id.button_list_textview_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Integer[]> candidatureId = new HashMap<>();
                Integer[] ids = {(Integer)candidatures.get(position).get("id")};
                candidatureId.put("candidatures", ids);
                String json = gson.toJson(candidatureId);
                Log.d("body", json);
                try {
                    StringEntity entity = new StringEntity(json);
                    HttpUtils.patch(context, "candidature/" + action, null, entity, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // If the response is JSONObject instead of expected JSONArray
                            Toast.makeText(context, "Vous avez bien validé ce créneau.", Toast.LENGTH_LONG).show();
                            candidatures.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // If the response is JSONObject instead of expected JSONArray
                            Toast.makeText(context, "Vous avez bien validé ce créneau.", Toast.LENGTH_LONG).show();
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
