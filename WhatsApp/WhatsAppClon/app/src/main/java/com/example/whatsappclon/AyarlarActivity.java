package com.example.whatsappclon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AyarlarActivity extends AppCompatActivity {

    private Button hesapAyarlarınıGüncelleme;
    private EditText kullaniciAdi , kullaniciDurumu;
    private CircleImageView kullaniciProfilResmi;


    //Firebase
    private FirebaseAuth mYetki;
    private DatabaseReference veriyolu;

    private String mevcutKullaniciId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);


        //Firebase
        mYetki=FirebaseAuth.getInstance();
        veriyolu= FirebaseDatabase.getInstance().getReference();
        mevcutKullaniciId=mYetki.getCurrentUser().getUid();



        //Kontrol Tanımlamarı
        hesapAyarlarınıGüncelleme=findViewById(R.id.ayarları_guncelleme_buttonu);
        kullaniciAdi=findViewById(R.id.kullanici_adi_ayarla);
        kullaniciDurumu=findViewById(R.id.profil_durumu_ayarlama);
        kullaniciProfilResmi=findViewById(R.id.profil_resmi_ayarla);


        hesapAyarlarınıGüncelleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AyarlariGüncelle();
            }
        });

    }

    private void AyarlariGüncelle() {

        String kullaniciAdiAyarla = kullaniciAdi.getText().toString();
        String kullaniciDurumuAyarla = kullaniciDurumu.getText().toString();

        if (TextUtils.isEmpty(kullaniciAdiAyarla))
        {
            Toast.makeText(this, "Ad Kısmı Boş Bırakılamaz", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(kullaniciDurumuAyarla))
        {
            Toast.makeText(this, " Durum Kısmı Boş Bırakılamaz", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,String> profilHaritasi=new HashMap<>();
            profilHaritasi.put("uid",mevcutKullaniciId);
            profilHaritasi.put("ad",kullaniciAdiAyarla);
            profilHaritasi.put("durum",kullaniciDurumuAyarla);


            veriyolu.child("Kullanicilar").child(mevcutKullaniciId).setValue(profilHaritasi)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(AyarlarActivity.this, "Profiliniz başarılı şekilde güncellendi...", Toast.LENGTH_SHORT).show();
                                Intent anaSayfa=new Intent(AyarlarActivity.this,MainActivity.class);
                                anaSayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(anaSayfa);
                                finish();
                            }
                            else
                            {
                                String mesaj = task.getException().toString();
                                Toast.makeText(AyarlarActivity.this, "Hata:"+mesaj, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    }
}