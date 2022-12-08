package com.example.whatsappclon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    //    Tanımlama İŞLEMLERİ
    private Button girisButonu, telefonlaGirisButonu;
    private EditText KullaniciMail, KullaniciSifre;
    private TextView YeniHesapAlma, SifreUnutmaBaglanti;


    //Firebase Yetki

    private FirebaseAuth mYetki;

    //PrOGRESS
    ProgressDialog girisDialog;




    private void KullaniciyiAnaAktivitiyeGönder() {
        Intent AnaAktiviteIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(AnaAktiviteIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //KONTROL TANIMLAMARI
        girisButonu = findViewById(R.id.giris_butonu);
        telefonlaGirisButonu = findViewById(R.id.telefonla_giris_butonu);

        KullaniciMail = findViewById(R.id.giris_email);
        KullaniciSifre = findViewById(R.id.giris_sifre);

        YeniHesapAlma = findViewById(R.id.yeni_hesap_alma);
        SifreUnutmaBaglanti = findViewById(R.id.sifre_unutma_baglantisi);

        //PROgRESS
        girisDialog = new ProgressDialog(this);


        //Firebase yetki
        mYetki = FirebaseAuth.getInstance();





        YeniHesapAlma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login activityi   kayıt activitye gönderir

                Intent kayitAktivityIntent = new Intent(LoginActivity.this,KayitActivity.class);
                startActivity(kayitAktivityIntent);
            }
        });

        girisButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KullaniciyaGirisİzniVer();
            }

        });
    }

    private void KullaniciyaGirisİzniVer() {
        String email = KullaniciMail.getText().toString();
        String sifre = KullaniciSifre.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email boş olamaz!", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(sifre)) {
            Toast.makeText(this, "Şifre boş olamaz!", Toast.LENGTH_SHORT).show();
        } else {
            //Progress
            girisDialog.setTitle("Giriş yapılıyor");
            girisDialog.setMessage("Lütfen bekleyin...");
            girisDialog.setCanceledOnTouchOutside(true);
            girisDialog.show();


            //Giriş için
            mYetki.signInWithEmailAndPassword(email, sifre)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                Intent anaSayfa = new Intent(LoginActivity.this, MainActivity.class);
                                anaSayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(anaSayfa);
                                Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                                girisDialog.dismiss();
                            }
                            else
                            {
                                String mesaj = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Hata: " + mesaj + " bilgileri kontrol edin!", Toast.LENGTH_SHORT).show();
                                girisDialog.dismiss();

                            }

                        }
                    });


        }
    }
}
