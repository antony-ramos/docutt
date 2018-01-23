package fr.utt.if26.projetx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import fr.utt.if26.projetx.database.Filtre;

public class ChoiceFilterFragment extends Fragment {

    private List<Filtre> filtres;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_choice_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Choisir un filtre");
        filtres = Filtre.find(Filtre.class, null, null);
        listView = getActivity().findViewById(R.id.choice_filter);
        if(listView.getAdapter() != null) listView.removeAllViews();
        listView.setAdapter(new ButtonFiltreAdapter(filtres, getContext(), "ChoiceFilterFragment", chooseRedirection()));
    }

    private String chooseRedirection() {
        switch (getArguments().getString("from")){
            case "nav_candidater":
                return "CandidateFragment";
            default:
                return "FilterFragment";
        }
    }

}
