package fr.utt.if26.projetx.database;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by Raphael on 14/12/2017.
 */

public class Filter extends SugarRecord<java.util.logging.Filter> {

    String name;
    ArrayList<String> ueChoisies;
    ArrayList<String> horairesNonVoulus;

    public Filter() {
    }

    public Filter(String name, ArrayList<String> ueChoisies, ArrayList<String> horairesNonVoulus) {
        this.name = name;
        this.ueChoisies = ueChoisies;
        this.horairesNonVoulus = horairesNonVoulus;
    }
}
