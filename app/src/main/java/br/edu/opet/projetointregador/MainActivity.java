package br.edu.opet.projetointregador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);





    }


    public void ClickLogin(View v){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }

    public void ClickRegistro(View v){
        Intent intent = new Intent(this,RegistroActivity.class);
        startActivity(intent);
    }


}