package fr.utt.if26.projetx;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    int type = 0;
    private EditText inputEmail, inputPassword, inputName, inputSurname;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private String statut;
    private String labo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        inputEmail = findViewById(R.id.email);
        inputName = findViewById(R.id.name);
        inputSurname = findViewById(R.id.surname);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btnResetPassword = findViewById(R.id.btn_reset_password);


        // Intégration de l'élément spinner du choix du statut de l'utilisateur (doctorant, professeur, administration)

        final Spinner spinnerStatut = findViewById(R.id.spinnerStatut);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterStatut = ArrayAdapter.createFromResource(this,
                R.array.statut_choix, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterStatut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerStatut.setAdapter(adapterStatut);

        spinnerStatut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                statut = spinnerStatut.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "Indiquez votre statut à l'UTT", Toast.LENGTH_SHORT).show();
                return;
            }

        });

        // Intégration de l'élément spinner du choix du laboratoire de l'utilisateur

        final Spinner spinnerLabo = findViewById(R.id.spinnerLabo);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterLabo = ArrayAdapter.createFromResource(this,
                R.array.labo_choix, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterLabo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerLabo.setAdapter(adapterLabo);

        spinnerLabo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                labo = spinnerLabo.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "Indiquez votre laboratoire à l'UTT", Toast.LENGTH_SHORT).show();
                return;
            }

        });

        // Fin des commentaires

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String name = inputName.getText().toString().trim();
                final String surname = inputSurname.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(surname)) {
                    Toast.makeText(getApplicationContext(), "Enter surname!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (labo == "Doctorants") {
                    type = 1;
                }
                if (labo == "Administration") {
                    type = 2;
                }
                if (labo == "Enseignant-Chercheur") {
                    type = 3;
                }



                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // ajout des informations dans la base de données

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("statut").setValue(statut);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("laboratoire").setValue(labo);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nom").setValue(name);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("prenom").setValue(surname);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(email);
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("type").setValue(type);
                                // TODO ajouter le nom et le prénom dans la base de données
                                // nom mDatabase.child("users").child(userId).setValue(user);
                                // prenom mDatabase.child("users").child(userId).setValue(user);

                                // fin des commentaires

                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}