package fr.utt.if26.projetx;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import fr.utt.if26.projetx.utils.HttpUtils;

/**
 * Created by raphael on 22/01/2018.
 */

public class CreneauListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<HashMap<String, Object>> creneau = new ArrayList<>();
    private Context context;

    public CreneauListAdapter(ArrayList<HashMap<String, Object>> candidatures, Context context) {
        this.creneau = candidatures;
        this.context = context;
    }

    @Override
    public int getCount() {
        return creneau.size();
    }

    @Override
    public Object getItem(int pos) {
        return creneau.get(pos);
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
        textView.setText((String) creneau.get(position).get("creneau"));

        Button button = view.findViewById(R.id.button_list_textview_button);
        button.setText("Supprimer");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) creneau.get(position).get("id");
                HttpUtils.delete("creneau/" + id, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response is JSONObject instead of expected JSONArray
                        Toast.makeText(context, "Vous avez bien supprimé ce créneau.", Toast.LENGTH_LONG).show();
                        creneau.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // If the response is JSONObject instead of expected JSONArray
                        Toast.makeText(context, "Vous avez bien supprimé ce créneau.", Toast.LENGTH_LONG).show();
                        creneau.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }

}
