package fr.utt.if26.projetx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import fr.utt.if26.projetx.database.Filtre;
import fr.utt.if26.projetx.utils.Router;

public class ChoiceCandidatureTypeFragment extends Fragment {

    private Button todo;
    private Button validate;
    private Button done;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_choice_candidature_type, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Choisir un type de candidature");
        todo = getActivity().findViewById(R.id.candidature_type_todo);
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("candidatureType", "todo");
                Router.replaceFragment("ChoiceCandidatureTypeFragment", "CandidaturesFragment", args , getContext());
            }
        });
        validate = getActivity().findViewById(R.id.candidature_type_validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("candidatureType", "validate");
                Router.replaceFragment("ChoiceCandidatureTypeFragment", "CandidaturesFragment", args , getContext());
            }
        });
        done = getActivity().findViewById(R.id.candidature_type_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("candidatureType", "done");
                Router.replaceFragment("ChoiceCandidatureTypeFragment", "CandidaturesFragment", args , getContext());
            }
        });
    }

}
