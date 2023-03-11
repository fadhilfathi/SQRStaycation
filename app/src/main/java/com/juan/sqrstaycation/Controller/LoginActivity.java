package com.juan.sqrstaycation.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juan.sqrstaycation.Model.Register;
import com.juan.sqrstaycation.R;
import com.juan.sqrstaycation.Storage.Storage;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView daftar;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReferenceCustomer = FirebaseDatabase.getInstance().getReference("Customer");
    DatabaseReference databaseReferenceOwner = FirebaseDatabase.getInstance().getReference("Owner");
    public static Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (storage == null) {
            storage = new Storage();
        }

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.button_login);
        daftar = (TextView) findViewById(R.id.daftar);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftar();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    if (haveNetworkConnection()) {
                        if (email.getText().toString().equals("admin")) {
                            Intent bukti = new Intent(LoginActivity.this, BuktiTransferActivity.class);
                            startActivity(bukti);
                        } else {
                            login(email.getText().toString(), password.getText().toString());
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Please fill empty cells", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void daftar(){
        Intent regist = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(regist);
    }

    private void login (String username, String password){
        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent home = new Intent(LoginActivity.this, MainActivity.class);
                    databaseReferenceCustomer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(firebaseAuth.getCurrentUser().getUid())){
                                Log.d("CUSTOMER", "COY");
                                home.putExtra("jenis", "customer");
                                storage.setJenis("customer");
                                startActivity(home);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReferenceOwner.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(firebaseAuth.getCurrentUser().getUid())){
                                Log.d("OWNER", "COY");
                                home.putExtra("jenis", "owner");
                                String child = firebaseAuth.getCurrentUser().getEmail();
                                String childN = child.substring(0, child.indexOf('@'));
                                home.putExtra("nama",childN);
                                storage.setJenis("owner");
                                storage.setNama(childN);
                                Log.d("NAMANYA : ", snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("nama").getValue().toString());
                                startActivity(home);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else {
                    Toast.makeText(LoginActivity.this, "Please Re-Check your Email & Password", Toast.LENGTH_SHORT).show();
                }
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