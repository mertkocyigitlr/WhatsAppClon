package com.example.whatsappclon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private SekmeErisimAdapter mySekmeErisimAdapter;



    public MainActivity() {
    }
    //Firebase
    private FirebaseUser mevcutkullanici;
    private FirebaseAuth mYetki;
    private DatabaseReference kullanicilarReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar=findViewById(R.id.ana_sayfa_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("WhatsApp");

        myViewPager=findViewById(R.id.ana_sekmeler_pager);
        mySekmeErisimAdapter= new SekmeErisimAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(mySekmeErisimAdapter);

        myTabLayout=findViewById(R.id.ana_sekmeler);
        myTabLayout.setupWithViewPager(myViewPager);

        //Firebase
        mYetki=FirebaseAuth.getInstance();
        mevcutkullanici=mYetki.getCurrentUser();

        kullanicilarReference= FirebaseDatabase.getInstance().getReference();


    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mevcutKullanici=mYetki.getCurrentUser();


        if (mevcutKullanici ==null)
        {
            KullaniciyiLoginActivityeGonder();
        }
        else
        {
            KullanicininVarliginiDoğrula();
        }

    }

    private void KullanicininVarliginiDoğrula() {

        String mevcutKullaniciId=mYetki.getCurrentUser().getUid();

        kullanicilarReference.child("Kullanicilar").child(mevcutKullaniciId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.child("ad").exists()))
                {
                    Toast.makeText(MainActivity.this, "Hoşgeldiniz", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent ayarlar=new Intent(MainActivity.this,AyarlarActivity.class);
                    ayarlar.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(ayarlar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void KullaniciyiLoginActivityeGonder()
    {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.secenekler_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.ana_ayarlar_seceneği){
            Intent ayar = new Intent(MainActivity.this,AyarlarActivity.class);
            startActivity(ayar);

        }

        //Hesaptan Çıkış İşlemi
        if (item.getItemId() == R.id.mainLogout){
            mYetki.signOut();
            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }


        return true;

    }
}