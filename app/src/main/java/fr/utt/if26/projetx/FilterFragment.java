package fr.utt.if26.projetx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnSelectClickListener;

import java.util.ArrayList;

public class FilterFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> UE = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Candidatures");
        recyclerView = getActivity().findViewById(R.id.ue_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);
        prepareListData();
        ChipsAdapter adapter = new ChipsAdapter(UE);
        recyclerView.setAdapter(adapter);
    }


    private void prepareListData() {
        UE.add("LO02");
        UE.add("IF26");
        UE.add("LO02");
        UE.add("IF26");
        UE.add("LO02");
        UE.add("IF26");
        UE.add("LO02");
        UE.add("IF26");
    }
}
