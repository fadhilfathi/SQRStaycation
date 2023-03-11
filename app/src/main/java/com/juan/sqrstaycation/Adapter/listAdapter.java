package com.juan.sqrstaycation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.juan.sqrstaycation.Controller.DetailPenginapanActivity;
import com.juan.sqrstaycation.Controller.LoginActivity;
import com.juan.sqrstaycation.Controller.MainActivity;
import com.juan.sqrstaycation.Model.Penginapan;
import com.juan.sqrstaycation.R;

import java.util.ArrayList;

public class listAdapter extends ArrayAdapter<Penginapan> {

    public listAdapter(Context context, ArrayList<Penginapan> penginapanArrayList){
        super(context, R.layout.list_item, penginapanArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Penginapan penginapan = getItem(position);

         if (convertView == null){
             convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
         }

         TextView judul = convertView.findViewById(R.id.judulItem);
         TextView alamat = convertView.findViewById(R.id.alamatItem);
         TextView kamar = convertView.findViewById(R.id.kamarItem);
         TextView harga = convertView.findViewById(R.id.hargaItem);
         TextView status = convertView.findViewById(R.id.status);
         LinearLayout linearLayout = convertView.findViewById(R.id.layoutitem);

         judul.setText(penginapan.getJudul());
         alamat.setText("Alamat : " + penginapan.getAlamat());
         kamar.setText("Jumlah Kamar : " + penginapan.getKamar());
         harga.setText("Rp. " + penginapan.getHarga());
         if (!penginapan.getStatus().equals("0")){
             parent.setClickable(false);
             linearLayout.setBackgroundColor(Color.GRAY);
//             convertView.setClickable(false);
             status.setText(penginapan.getStatus());
             if (penginapan.getStatus().equals("no")) {
                 parent.setClickable(false);
                 status.setTextColor(Color.RED);
             }else{
                 parent.setClickable(false);
                 linearLayout.setBackgroundColor(Color.GRAY);
                 status.setTextColor(Color.GREEN);
             }
         }else{
             parent.setClickable(true);
//             convertView.setClickable(true);
             status.setVisibility(View.GONE);
         }

        return convertView;
    }
}
