package br.edu.opet.projetointregador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mAuth = FirebaseAuth.getInstance();
    }

    public void salvarReg(View v){
        Intent intent = new Intent(this,MainActivity.class);

        EditText eTName = findViewById(R.id.editTextEmail);
        EditText eTEmail = findViewById(R.id.editTextTextEmail);
        EditText eTTelefone = findViewById(R.id.editTextTextTelefone);
        EditText eTSenha = findViewById(R.id.editTextSenhaLogin);
        ProgressBar pbReg = findViewById(R.id.progressBarReg);

        String txtNome = eTName.getText().toString().trim();
        String txtEmail = eTEmail.getText().toString().trim();
        String txtFone = eTTelefone.getText().toString().trim();
        String txtSenha = eTSenha.getText().toString();

        if (txtNome.isEmpty()){
            eTName.setError("Nome Invalido");
            eTName.requestFocus();

        }
        if (txtEmail.isEmpty()){
            eTEmail.setError("Email Invalido");
            eTEmail.requestFocus();

        }
        if (txtFone.isEmpty()){
            eTTelefone.setError("Telefone Invalido");
            eTTelefone.requestFocus();

        }
        if (txtSenha.isEmpty() || txtSenha.length() < 6){
            eTSenha.setError("Senha Invalida");
            eTSenha.requestFocus();

        }

        pbReg.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(txtEmail,txtSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Usuarios u = new Usuarios(txtNome,txtEmail,txtFone,txtSenha);

                    FirebaseDatabase.getInstance().getReference("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistroActivity.this,"Usuario Cadastrado com Sucesso :D",Toast.LENGTH_LONG).show();
                                pbReg.setVisibility(View.GONE);
                            }
                            else{
                                Toast.makeText(RegistroActivity.this,"Falha No Cadastro",Toast.LENGTH_LONG).show();
                                pbReg.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegistroActivity.this,"Falha No Cadastro",Toast.LENGTH_LONG);
                }


            }
        });
    }

}