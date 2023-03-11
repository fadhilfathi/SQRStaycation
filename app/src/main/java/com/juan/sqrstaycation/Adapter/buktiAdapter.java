package com.juan.sqrstaycation.Adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juan.sqrstaycation.Model.Pembayaran;
import com.juan.sqrstaycation.Model.Penginapan;
import com.juan.sqrstaycation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class buktiAdapter extends ArrayAdapter<Pembayaran> {

    DatabaseReference databaseReferenceCustomer = FirebaseDatabase.getInstance().getReference("Customer");
    String nama;
    public buktiAdapter(Context context, ArrayList<Pembayaran> pembayaranArrayList){
        super(context, R.layout.list_bukti, pembayaranArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Pembayaran pembayaran = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_bukti, parent, false);
        }

        TextView judul = convertView.findViewById(R.id.judulItem);
        TextView alamat = convertView.findViewById(R.id.alamatItem);
        TextView kamar = convertView.findViewById(R.id.kamarItem);
        TextView harga = convertView.findViewById(R.id.hargaItem);
        TextView status = convertView.findViewById(R.id.status);
        TextView pembeli = convertView.findViewById(R.id.pembeliBukti);
        ImageView image = convertView.findViewById(R.id.gambarbukti);

        judul.setText(pembayaran.getPenginapan());

        databaseReferenceCustomer.child(pembayaran.getCustomer()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("id").getValue().toString().equals(pembayaran.getCustomer())){
                    Log.d("SNAPSHOT : ", snapshot.getValue().toString());
                    String tempNama = snapshot.child("nama").getValue().toString();
                    pembeli.setText("Pembeli : " + tempNama);
                }

//                nama = snapshot.child(pembayaran.getCustomer()).child("nama").getValue().toString();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Log.d("DATA", dataSnapshot.getValue().toString());
//                    nama = dataSnapshot.child("nama").getValue().toString();
//                    pembeli.setText("Pembeli : " + nama);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        String tempNama = databaseReferenceCustomer.child(pembayaran.getCustomer()).child("nama")
//        pembeli.setText("Pembeli : " + nama);
        alamat.setVisibility(GONE);
        kamar.setVisibility(GONE);
        harga.setVisibility(GONE);
        if (pembayaran.getStatus().equals("no")){
            status.setTextColor(Color.RED);
        }else{
            status.setTextColor(Color.GREEN);
        }
        status.setText(pembayaran.getStatus());
        String link = pembayaran.getGambar();
        Picasso.get().load(link).fit().into(image);
//        image.setImageResource();

        return convertView;
    }
}
