package com.juan.sqrstaycation.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juan.sqrstaycation.Adapter.buktiAdapter;
import com.juan.sqrstaycation.Model.Pembayaran;
import com.juan.sqrstaycation.Model.Penginapan;
import com.juan.sqrstaycation.R;

import java.util.ArrayList;

public class BuktiTransferActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReferenceCustomer = FirebaseDatabase.getInstance().getReference("Customer");
    DatabaseReference databaseReferenceOwner = FirebaseDatabase.getInstance().getReference("Owner");
    DatabaseReference databaseReferencePenginapan = FirebaseDatabase.getInstance().getReference("Penginapan");
    DatabaseReference databaseReferenceBuktiBayar = FirebaseDatabase.getInstance().getReference("BuktiBayar");
    FloatingActionButton fab;
    ArrayList<Pembayaran> pembayaranList;
    ListView listview;
    Button terima;
    String key, jumlah, totalS;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti_transfer);
        listview = findViewById(R.id.listBukti);
//        terima = findViewById(R.id.buttonTerima);
        pembayaranList = new ArrayList<>();
        databaseReferenceBuktiBayar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("DATA : ", dataSnapshot.getValue().toString());
                    Pembayaran pembayaran;
                    pembayaran = dataSnapshot.getValue(Pembayaran.class);
                    pembayaranList.add(pembayaran);
                    }
                buktiAdapter buktiadapter = new buktiAdapter(BuktiTransferActivity.this, pembayaranList);
                listview.setAdapter(buktiadapter);
                listview.setClickable(true);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        AlertDialog.Builder builder = new AlertDialog.Builder(BuktiTransferActivity.this);
                        builder.setMessage("Terima Pembayaran ?")
                                .setCancelable(false)
                                .setPositiveButton("Terima", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        databaseReferenceBuktiBayar.child(pembayaranList.get(position).getCustomer()+pembayaranList.get(position).getPenginapan()).child("status").setValue("yes");
                                        databaseReferencePenginapan.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                    if (dataSnapshot.child("judul").getValue().toString().equals(pembayaranList.get(position).getPenginapan())){
                                                        jumlah = dataSnapshot.child("kamar").getValue().toString();
                                                        key = dataSnapshot.getKey();
                                                    }
                                                }
                                                total = Integer.parseInt(jumlah);
                                                total = total - 1;
                                                totalS = String.valueOf(total);
                                                Log.d("TOTAL", totalS);
                                                databaseReferencePenginapan.child(key).child("kamar").setValue(totalS);
                                                Toast.makeText(BuktiTransferActivity.this, "Pembayaran Diterima", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                pembayaranList.clear();
                                                databaseReferenceBuktiBayar.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            Log.d("DATA : ", dataSnapshot.getValue().toString());
                                                            Pembayaran pembayaran;
                                                            pembayaran = dataSnapshot.getValue(Pembayaran.class);
                                                            pembayaranList.add(pembayaran);
                                                        }
                                                        buktiAdapter buktiadapter = new buktiAdapter(BuktiTransferActivity.this, pembayaranList);
                                                        listview.setAdapter(buktiadapter);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("Tolak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}