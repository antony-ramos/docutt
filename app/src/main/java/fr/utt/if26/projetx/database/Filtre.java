package fr.utt.if26.projetx.database;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by Raphael on 14/12/2017.
 */

public class Filtre extends SugarRecord {

    String name;
    String ueChoisies;
    String horairesNonVoulus;

    public Filtre() {
    }

    public Filtre(String name, ArrayList<String> ueChoisies, ArrayList<String> horairesNonVoulus) {
        this.name = name;
        this.ueChoisies = ueChoisies.toString();
        this.horairesNonVoulus = horairesNonVoulus.toString();
    }

    public String getHorairesNonVoulus() {
        return horairesNonVoulus;
    }

    public String getName() {
        return name;
    }

    public String getUeChoisies() {
        return ueChoisies;
    }

    @Override
    public String toString() {
        return "nom: " + name + ", horairesNonVoulus: " + horairesNonVoulus + ", ue choisies: " + ueChoisies;
    }
}
