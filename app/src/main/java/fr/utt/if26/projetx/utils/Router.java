package fr.utt.if26.projetx.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import fr.utt.if26.projetx.CandidateFragment;
import fr.utt.if26.projetx.CandidatureProfFragment;
import fr.utt.if26.projetx.CandidaturesFragment;
import fr.utt.if26.projetx.ChoiceCandidatureTypeFragment;
import fr.utt.if26.projetx.ChoiceFilterFragment;
import fr.utt.if26.projetx.CreneauFragment;
import fr.utt.if26.projetx.CreneauListFragment;
import fr.utt.if26.projetx.FilterFragment;
import fr.utt.if26.projetx.MainContent;
import fr.utt.if26.projetx.R;

/**
 * Created by Raphael on 21/12/2017.
 */

public class Router {

    public static void replaceFragment(String from, String to, Bundle args, Context context) {
        Fragment fragment = null;
        switch (to){
            case "FilterFragment":
                fragment = new FilterFragment();
                break;
            case "CandidateFragment":
                fragment = new CandidateFragment();
                break;
            case "ChoiceFilterFragment":
                fragment = new ChoiceFilterFragment();
                break;
            case "ChoiceCandidatureTypeFragment":
                fragment = new ChoiceCandidatureTypeFragment();
                break;
            case "CandidaturesFragment":
                fragment = new CandidaturesFragment();
                break;
            case "CreneauFragment":
                fragment = new CreneauFragment();
                break;
            case "CreneauListFragment":
                fragment = new CreneauListFragment();
                break;
            case "CandidatureProfFragment":
                fragment = new CandidatureProfFragment();
                break;
            default:
                fragment = new MainContent();
                break;
        }
        if(args == null) args = new Bundle();
        args.putString("from", from);
        fragment.setArguments(args);
        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
