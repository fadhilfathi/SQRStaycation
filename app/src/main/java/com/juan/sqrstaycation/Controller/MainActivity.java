package com.juan.sqrstaycation.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juan.sqrstaycation.Adapter.listAdapter;
import com.juan.sqrstaycation.Model.Penginapan;
import com.juan.sqrstaycation.R;
import com.juan.sqrstaycation.Storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LoginActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReferenceCustomer = FirebaseDatabase.getInstance().getReference("Customer");
    DatabaseReference databaseReferenceOwner = FirebaseDatabase.getInstance().getReference("Owner");
    DatabaseReference databaseReferencePenginapan = FirebaseDatabase.getInstance().getReference("Penginapan");
    DatabaseReference databaseReferenceBuktiBayar = FirebaseDatabase.getInstance().getReference("BuktiBayar");
    FloatingActionButton fab;
    ArrayList<Penginapan> penginapanList;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = this.getIntent();
        fab = findViewById(R.id.fab);
        listview = findViewById(R.id.listview);

        if (storage.getJenis() != null){
            if (storage.getJenis().equals("owner")){
                fab.setVisibility(View.VISIBLE);
            }
        }

        penginapanList = new ArrayList<>();
                databaseReferencePenginapan.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Log.d("DATA : ", dataSnapshot.child("owner").getValue().toString());
                            Log.d("VALUE: ", dataSnapshot.getValue().toString());
                            if (dataSnapshot != null) {
                                if (storage.getJenis().equals("owner")) {
                                    Penginapan penginapan;
                                    penginapan = dataSnapshot.getValue(Penginapan.class);
                                    Log.d("MASUK OWNER MAIN", "");
                                    Log.d("DATA IF ", dataSnapshot.child("owner").getValue().toString());
                                    Log.d("DATA BANDINGAN ", firebaseAuth.getCurrentUser().getUid());
                                        if (dataSnapshot.child("owner").getValue().toString().equals(firebaseAuth.getCurrentUser().getUid())) {
                                            penginapanList.add(penginapan);
                                            listAdapter listAdapter = new listAdapter(MainActivity.this, penginapanList);
                                            listview.setAdapter(listAdapter);
                                        }
                                } else {
                                    databaseReferenceBuktiBayar.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Log.d("MASUK DATA CHANGE", snapshot.toString());
                                            Penginapan penginapan1 = new Penginapan();
                                            if (snapshot.getValue() != null) {
//                                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//                                                DataSnapshot dataSnapshot1 = snapshot.getChildren();
                                                    String parent = firebaseAuth.getCurrentUser().getUid()+dataSnapshot.child("judul");
                                                    if (snapshot.child(parent).child("customer").getValue() != null) {
                                                        if (snapshot.child(parent).child("customer").getValue().toString().equals(firebaseAuth.getCurrentUser().getUid())) {
                                                            Log.d("IF ATAS BENAR", dataSnapshot.child("judul").getValue().toString());
                                                            if (snapshot.child(parent).child("penginapan").getValue().toString().equals(dataSnapshot.child("judul").getValue().toString())) {
                                                                Log.d("MASUK IF", "");
                                                                String status = snapshot.child(parent).child("status").getValue().toString();
                                                                penginapan1.setOwner(dataSnapshot.child("owner").getValue().toString());
                                                                penginapan1.setJudul(dataSnapshot.child("judul").getValue().toString());
                                                                penginapan1.setKamar(dataSnapshot.child("kamar").getValue().toString());
                                                                penginapan1.setHarga(dataSnapshot.child("harga").getValue().toString());
                                                                penginapan1.setAlamat(dataSnapshot.child("alamat").getValue().toString());
                                                                penginapan1.setStatus(status);
                                                                penginapanList.add(penginapan1);
                                                                listAdapter listAdapter = new listAdapter(MainActivity.this, penginapanList);
                                                                listview.setAdapter(listAdapter);
                                                            } else {
                                                                penginapan1 = dataSnapshot.getValue(Penginapan.class);
                                                                penginapanList.add(penginapan1);
                                                                listAdapter listAdapter = new listAdapter(MainActivity.this, penginapanList);
                                                                listview.setAdapter(listAdapter);
                                                            }
                                                        }
                                                    }
                                                    else {
                                                        Log.d("MASUK ELSE", "");
                                                        penginapan1 = dataSnapshot.getValue(Penginapan.class);
                                                        penginapanList.add(penginapan1);
                                                        listAdapter listAdapter = new listAdapter(MainActivity.this, penginapanList);
                                                        listview.setAdapter(listAdapter);
                                                    }
//                                                }
                                            }else{
                                                penginapan1 = dataSnapshot.getValue(Penginapan.class);
                                                penginapanList.add(penginapan1);
                                                Log.d("MASUK ELSE BAWAH", penginapanList.get(0).getJudul());
                                                listAdapter listAdapter = new listAdapter(MainActivity.this, penginapanList);
                                                listview.setAdapter(listAdapter);
                                            }


//                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                            }
                        }
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (storage.getJenis().equals("owner")) {

                                } else {
                                    if (penginapanList.get(position).getStatus().equals("0")){
                                        storage.setJudul(penginapanList.get(position).getJudul());
                                        storage.setAlamat(penginapanList.get(position).getAlamat());
                                        storage.setKamar(penginapanList.get(position).getKamar());
                                        storage.setHarga(penginapanList.get(position).getHarga());
                                        String child = firebaseAuth.getCurrentUser().getEmail();
                                        String childN = child.substring(0, child.indexOf('@'));
                                        storage.setIdPenginapan(childN+penginapanList.get(position).getJudul());
                                        Intent detail = new Intent(MainActivity.this, DetailPenginapanActivity.class);
                                        startActivity(detail);
                                    }else{
                                        Toast.makeText(MainActivity.this, "Already Booked", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent penginapan = new Intent(MainActivity.this, AddPenginapanActivity.class);
                    startActivity(penginapan);
                    finish();
                }
            });

        }

    @Override
    protected void onResume() {
        super.onResume();
    }
}