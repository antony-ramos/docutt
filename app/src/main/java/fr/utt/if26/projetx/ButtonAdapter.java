package fr.utt.if26.projetx;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.List;

import fr.utt.if26.projetx.database.Filtre;

/**
 * Created by Raphael on 08/12/2017.
 */


public class ButtonAdapter extends BaseAdapter implements ListAdapter {
    private List<Filtre> list;
    private Context context;
    private String from;
    private String to;

    public ButtonAdapter(List<Filtre> list, Context context, String from, String to) {
        this.list = list;
        this.context = context;
        this.from = from;
        this.to = to;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_button, null);
        }

        //Handle buttons and add onClickListeners
        Button button = view.findViewById(R.id.listButton);
        button.setText(list.get(position).getName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "On devrait arriver sur une liste d'UE", Toast.LENGTH_LONG).show();
                Bundle args = new Bundle();
                args.putString("filterName", list.get(position).getName());
                router.replaceFragment(from, to, args , context);
            }
        });

        return view;
    }

}