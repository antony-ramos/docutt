package fr.utt.if26.projetx;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// TODO : Mettre en place un système de notifications

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Créer la jonction avec la db auth
        // On récupère le type d'utilisateur et on met un listener si jamais c'est modifié

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabaseUserType = mDatabase.child("users").child(user.getUid()).child("type");
        DatabaseReference mDatabaseName = mDatabase.child("users").child(user.getUid()).child("nom");
        DatabaseReference mDatabaseEmail = mDatabase.child("users").child(user.getUid()).child("email");


        // Générer le bon menu de navigation latéral suivant l'information

        final NavigationView navigationView = findViewById(R.id.nav_view);

        ValueEventListener typeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int post = dataSnapshot.getValue(Integer.class);
                modifyNavigationMenuView(navigationView, post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        mDatabaseUserType.addValueEventListener(typeListener);


        final TextView mail = navigationView.getHeaderView(0).findViewById(R.id.textView_nav_mail);
        final TextView name = navigationView.getHeaderView(0).findViewById(R.id.textView_nav_name);

        ValueEventListener nameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String post = dataSnapshot.getValue(String.class);
                name.setText(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                name.setText("error name");
            }
        };
        mDatabaseName.addValueEventListener(nameListener);

        ValueEventListener emailListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String post = dataSnapshot.getValue(String.class);
                mail.setText(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mail.setText("error name");
            }
        };
        mDatabaseEmail.addValueEventListener(emailListener);


        //mail.setText();
        //name.setText(mDatabase.get);

        LinearLayout header = (LinearLayout) navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show(); // TODO : TEST ONLY
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        // On lance la création du menu latéral suivant les informations ci-dessus

        navigationView.setNavigationItemSelectedListener(this);

        // On lance la création du fragment principal

        fragment = new MainContent();
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        // TODO : bouton pour home
    }

    // TODO : Faire la gestion du menu latéral
    private void modifyNavigationMenuView(NavigationView navigationView, int type) {
        navigationView.getMenu().clear();
        if (type == 1) {
            navigationView.inflateMenu(R.menu.activity_main_drawer_doctorant);
        } else if (type == 2) {
            navigationView.inflateMenu(R.menu.activity_main_drawer_administration);
        } else {
            navigationView.inflateMenu(R.menu.activity_main_drawer_professeur);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    // TODO Faire la gestion des boutons du menu latéral
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        /* Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_candidature) {
            setContentView(R.layout.content_candidatures);
            ExpandableListView expListView = (ExpandableListView) findViewById(R.id.elvCandidatures);
            prepareListData();
            ExpandableListCandidaturesAdapter listAdapter = new ExpandableListCandidaturesAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        // TODO : Créer les appels de fragments
        //initializing the fragment object which is selected
        Bundle args = new Bundle();
        switch (itemId) {
            case R.id.nav_candidature:
                fragment = new ChoiceCandidatureTypeFragment();
                args.putString("from", "nav_candidature");
                break;
            case R.id.nav_add_filter:
                fragment = new FilterFragment();
                args.putString("from", "nav_add_filter");
                break;
            case R.id.nav_candidater:
                fragment = new ChoiceFilterFragment();
                args.putString("from", "nav_candidater");
                break;
            case R.id.nav_filters:
                fragment = new ChoiceFilterFragment();
                args.putString("from", "nav_filters");
                break;
            case R.id.nav_ajouter_ue:
                fragment = new AjouterUeFragment();
                args.putString("from", "nav_filters");
                break;
            case R.id.nav_ajouter_creneau:
                fragment = new CreneauFragment();
                args.putString("from", "nav_ajouter_creneau");
                break;
            case R.id.nav_professor_candidatures:
                fragment = new ChoiceUeFragment();
                args.putString("from", "nav_professor_candidatures");
                break;
            case R.id.nav_creneaux:
                fragment = new ChoiceUeFragment();
                args.putString("from", "nav_creneaux");
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            fragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager fm = getSupportFragmentManager();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
