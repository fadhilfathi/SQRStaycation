package com.juan.sqrstaycation.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.juan.sqrstaycation.Model.Penginapan;
import com.juan.sqrstaycation.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPenginapanActivity extends AppCompatActivity {

    EditText judul, alamat, kamar, harga;
    Button tambah;
    DatabaseReference mDatabaseRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_penginapan);

        judul = (EditText) findViewById(R.id.judul);
        alamat = (EditText) findViewById(R.id.Alamat);
        kamar = (EditText) findViewById(R.id.Kamar);
        harga = findViewById(R.id.harga);
        tambah = (Button) findViewById(R.id.tambah);
        mAuth = FirebaseAuth.getInstance();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!judul.getText().toString().isEmpty() && !alamat.getText().toString().isEmpty() && !kamar.getText().toString().isEmpty() && !harga.getText().toString().isEmpty()) {
                    if (Integer.parseInt(kamar.getText().toString()) > 1) {
                        Penginapan penginapan = new Penginapan(judul.getText().toString(), alamat.getText().toString(), kamar.getText().toString(), harga.getText().toString(), "0", mAuth.getCurrentUser().getUid());
                        mDatabaseRef = database.getReference("Penginapan");
                        String child = mAuth.getCurrentUser().getEmail();
                        String childN = child.substring(0, child.indexOf('@'));
                        mDatabaseRef.child(childN + judul.getText().toString()).setValue(penginapan).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddPenginapanActivity.this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(AddPenginapanActivity.this, MainActivity.class);
                                startActivity(home);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddPenginapanActivity.this, "Penmabahan data gagal, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(AddPenginapanActivity.this, "Minimal kamar ada 2", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddPenginapanActivity.this, "Please fill empty cells", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}