package br.edu.opet.projetointregador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
    }

    public void textRecuperarCliclado(View v){
        EditText editEmail = findViewById(R.id.editTextEmail);
        ProgressBar progressBar = findViewById(R.id.progressBarReg);
        String textEmail = editEmail.getText().toString().trim();

        if (textEmail ==null ||!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
            editEmail.setError("Email Invalido");
            editEmail.requestFocus();

        }else{
            progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(textEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Email enviado com sucesso",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(LoginActivity.this,"Falha ao recuperar senha",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

        }

    }

    public void loginClicado(View v){
        EditText editEmail = findViewById(R.id.editTextEmail);
        EditText editSenha = findViewById(R.id.editTextSenhaLogin);
        ProgressBar progressBar = findViewById(R.id.progressBarReg);

        String textEmail = editEmail.getText().toString().trim();
        String textSenha = editSenha.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
            editEmail.setError("Email Invalido");
            editEmail.requestFocus();

        }

        if (textSenha.isEmpty() || textSenha.length() < 6){
            editSenha.setError("Senha Invalida");
            editSenha.requestFocus();

        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(textEmail,textSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Logado",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"Email ou Senha invalidos.",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}