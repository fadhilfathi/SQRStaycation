package com.juan.sqrstaycation.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.juan.sqrstaycation.Model.Register;
import com.juan.sqrstaycation.R;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, nama;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button daftar;
    DatabaseReference mDatabaseRef;
    Spinner spinner;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        spinner = (Spinner) findViewById(R.id.spinner);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        nama = (EditText) findViewById(R.id.nama);
        daftar = (Button) findViewById(R.id.daftar);

        String[] member = {"Customer", "Owner"};
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, member);
        spinner.setAdapter(arrayAdapter);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString() != null && password.getText().toString() != null && nama.getText().toString() != null) {
                    if (haveNetworkConnection()) {
                        if (password.getText().length() >= 6) {
                            String jenis = spinner.getSelectedItem().toString();
                            Register(email.getText().toString(), password.getText().toString(), nama.getText().toString(), jenis);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Password minimum 6 character", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Not Connected to Internet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Please fill empty cells", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Register(String email, String password, String nama, String jenis){
        mDatabaseRef = database.getReference(jenis);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String uid = mAuth.getCurrentUser().getUid();
//                        String registerId = mDatabaseRef.push().getKey();
                        Register register = new Register(uid, email, password, nama, jenis);
                        mDatabaseRef.child(uid).setValue(register).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                    Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(login);
                                    finish();
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}