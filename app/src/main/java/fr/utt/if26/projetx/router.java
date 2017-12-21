package fr.utt.if26.projetx;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Raphael on 21/12/2017.
 */

public class router {

    public static void replaceFragment(String from, String to, Bundle args, Context context) {
        Fragment fragment = null;
        switch (to){
            case "FilterFragment":
                fragment = new FilterFragment();
                break;
            case "CandidateFragment":
                fragment = new FilterFragment();
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
