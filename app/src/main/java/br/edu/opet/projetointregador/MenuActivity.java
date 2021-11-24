package br.edu.opet.projetointregador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MenuActivity extends AppCompatActivity {

    EditText editNome ;
    EditText editPreco;
    EditText editQuant;
    ListView listView ;
    ProgressBar progressBar ;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private ArrayList<Produto> lisProduto = new ArrayList<Produto>();
    private ArrayAdapter<Produto> arrayAdapterProduto;
    private Produto produtoSelec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        editNome = findViewById(R.id.textViewIdNome);
        editQuant = findViewById(R.id.textViewIdQuant);
        editPreco = findViewById(R.id.textViewIdPreco);
        listView = (ListView) findViewById(R.id.idListView);
        progressBar = findViewById(R.id.progressBarReg);


        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


       loadData();

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               produtoSelec = (Produto) adapterView.getItemAtPosition(i);
               editNome.setText(produtoSelec.getNome());
               editPreco.setText(produtoSelec.getPreço());
               editQuant.setText(produtoSelec.getQuantidade());
           }
       });

    }

    private void loadData() {
        databaseReference.child("Produtos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lisProduto.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    Produto p = obj.getValue(Produto.class);
                    lisProduto.add(p);
                }
                Collections.sort(lisProduto);
                ProdutoAdapter produtoAdapter = new ProdutoAdapter(MenuActivity.this, lisProduto);
                listView.setAdapter(produtoAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crud,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        progressBar = findViewById(R.id.progressBarReg);
        int id = item.getItemId();
        Produto p = new Produto();
        progressBar.setVisibility(View.VISIBLE);
        switch (id){
            case R.id.menu_inser:
                p.setId(UUID.randomUUID().toString());
                p.setNome(editNome.getText().toString());
                p.setPreço(editPreco.getText().toString());
                p.setQuantidade(editQuant.getText().toString());
                databaseReference.child("Produtos").child(p.getId()).setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MenuActivity.this, "Produto Inserido com Sucesso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MenuActivity.this,MenuActivity.class);
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(MenuActivity.this, "Falha ao inserir o cliente", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    }
                });
                break;
            case R.id.menu_del:
                p.setId(produtoSelec.getId());
                databaseReference.child("Produtos").child(p.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MenuActivity.this, "Produto Deletado com Sucesso", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(MenuActivity.this, "Falha ao deletar o cliente", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                break;
            case R.id.menu_alter:
                p.setId(produtoSelec.getId());
                p.setNome(editNome.getText().toString().trim());
                p.setPreço(editPreco.getText().toString().trim());
                p.setQuantidade(editQuant.getText().toString().trim());
                databaseReference.child("Produtos").child(p.getId()).setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MenuActivity.this, "Produtos Alterado com Sucesso", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(MenuActivity.this, "Falha ao alterar o cliente", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                break;
            default:return false;
        }
        editNome.setText("");
        return true;
    }
}