package br.edu.opet.projetointregador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
    ListView listView ;
    ProgressBar progressBar ;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Cliente> lisCliente = new ArrayList<Cliente>();
    private ArrayAdapter<Cliente> arrayAdapterCliente;
    private Cliente clienteSelec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        editNome = findViewById(R.id.editTextNomeUsuario);
        listView = findViewById(R.id.idListView);
        progressBar = findViewById(R.id.progressBarReg);


        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


       loadData();

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               clienteSelec = (Cliente) adapterView.getItemAtPosition(i);
               editNome.setText(clienteSelec.getNome());
           }
       });

    }

    private void loadData() {
        databaseReference.child("Clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lisCliente.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    lisCliente.add(c);
                }
                Collections.sort(lisCliente);
                arrayAdapterCliente = new ArrayAdapter<Cliente>(MenuActivity.this,android.R.layout.simple_list_item_1,lisCliente);
                listView.setAdapter(arrayAdapterCliente);
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
        Cliente c = new Cliente();
        progressBar.setVisibility(View.VISIBLE);
        switch (id){
            case R.id.menu_inser:
                c.setId(UUID.randomUUID().toString());
                c.setNome(editNome.getText().toString());
                databaseReference.child("Clientes").child(c.getId()).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MenuActivity.this, "Cliente Inserido com Sucesso", Toast.LENGTH_SHORT).show();
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
                c.setId(clienteSelec.getId());
                databaseReference.child("Clientes").child(c.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MenuActivity.this, "Cliente Deletado com Sucesso", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(MenuActivity.this, "Falha ao deletar o cliente", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                break;
            case R.id.menu_alter:
                c.setId(clienteSelec.getId());
                c.setNome(editNome.getText().toString().trim());
                databaseReference.child("Clientes").child(c.getId()).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MenuActivity.this, "Cliente Alterado com Sucesso", Toast.LENGTH_SHORT).show();
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