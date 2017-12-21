package fr.utt.if26.projetx.database;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by Raphael on 14/12/2017.
 */

public class Filtre extends SugarRecord {

    private String name;
    private String ueChoisies;
    private String horairesNonVoulus;

    public Filtre() {
    }

    public Filtre(String name, String ueChoisies, String horairesNonVoulus) {
        this.name = name;
        this.ueChoisies = ueChoisies;
        this.horairesNonVoulus = horairesNonVoulus;
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

    public void setHorairesNonVoulus(String horairesNonVoulus) {
        this.horairesNonVoulus = horairesNonVoulus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUeChoisies(String ueChoisies) {
        this.ueChoisies = ueChoisies;
    }

    @Override
    public String toString() {
        return "nom: " + name + ", horairesNonVoulus: " + horairesNonVoulus + ", ue choisies: " + ueChoisies;
    }
}
