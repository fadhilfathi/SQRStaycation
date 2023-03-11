package com.juan.sqrstaycation.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteProgram;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.juan.sqrstaycation.Model.Pembayaran;
import com.juan.sqrstaycation.R;

import java.util.PropertyResourceBundle;

public class DetailPenginapanActivity extends MainActivity {

    TextView judul, alamat, kamar, harga;
    Button bayar;
    public static final int PICK_IMAGE = 1;
    Uri mImageUri = null;
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("BuktiBayar");
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penginapan);

        mProgress = new ProgressDialog(this);

        judul = findViewById(R.id.juduldetail);
        alamat = findViewById(R.id.alamatdetail);
        kamar = findViewById(R.id.kamardetail);
        harga = findViewById(R.id.hargadetail);
        bayar = findViewById(R.id.bayar);

        judul.setText(storage.getJudul());
        alamat.setText(storage.getAlamat());
        kamar.setText(storage.getKamar());
        harga.setText(storage.getHarga());

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProgress.setMessage("Process...");
        mProgress.show();
        mImageUri = data.getData();

        mStorage.child(storage.getJudul() + firebaseAuth.getCurrentUser().getUid()).putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                mStorage.child(storage.getJudul() + firebaseAuth.getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        Pembayaran pembayaran = new Pembayaran(firebaseAuth.getCurrentUser().getUid(), storage.getJudul(), downloadUrl, "no");
                        mDatabase.child(firebaseAuth.getCurrentUser().getUid()+storage.getJudul()).setValue(pembayaran).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mProgress.dismiss();
                                Toast.makeText(DetailPenginapanActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(DetailPenginapanActivity.this, MainActivity.class);
                                startActivity(home);
                                finish();
                            }
                        });
                    }
                });


            }
        });
    }

}