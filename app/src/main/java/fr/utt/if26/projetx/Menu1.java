package fr.utt.if26.projetx;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Antony RAMOS on 10/11/2017.
 */

public class Menu1 extends Fragment {


    ExpandableListCandidaturesAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.content_candidatures, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Candidatures");
        ExpandableListView expListView = (ExpandableListView) getActivity().findViewById(R.id.elvCandidatures);
        prepareListData();
        ExpandableListCandidaturesAdapter listAdapter = new ExpandableListCandidaturesAdapter(this.getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("GS11 | 24 UTP | En cours");
        listDataHeader.add("GS13 | 18 UTP | Refusée");
        listDataHeader.add("RE12 | 12 UTP | Validée");

        // Adding child data
        List<String> GS11 = new ArrayList<String>();
        GS11.add("10/11/2017 | 16-18 | TP");
        GS11.add("17/11/2017 | 16-18 | TP");
        GS11.add("24/11/2017 | 16-18 | TP");
        GS11.add("03/12/2017 | 16-18 | TP");

        List<String> RE12 = new ArrayList<String>();
        RE12.add("10/11/2017 | 16-18 | TP");
        RE12.add("10/11/2017 | 16-18 | TP");
        RE12.add("10/11/2017 | 16-18 | TP");
        RE12.add("10/11/2017 | 16-18 | TP");
        RE12.add("10/11/2017 | 16-18 | TP");
        RE12.add("10/11/2017 | 16-18 | TP");

        List<String> GS13 = new ArrayList<String>();
        GS13.add("10/11/2017 | 16-18 | TP");
        GS13.add("10/11/2017 | 16-18 | TP");
        GS13.add("10/11/2017 | 16-18 | TP");
        GS13.add("10/11/2017 | 16-18 | TP");
        GS13.add("10/11/2017 | 16-18 | TP");

        listDataChild.put(listDataHeader.get(0), GS11); // Header, Child data
        listDataChild.put(listDataHeader.get(1), RE12);
        listDataChild.put(listDataHeader.get(2), GS13);
    }
}
