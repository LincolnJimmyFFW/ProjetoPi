package br.edu.opet.projetointregador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;


public class ProdutoAdapter extends ArrayAdapter<Produto> {
    private final Context context;
    private final ArrayList<Produto> elementos;

    public ProdutoAdapter(Context context,ArrayList<Produto> elementos){
        super(context, R.layout.linha, elementos);
        this.context = context;
        this.elementos= elementos;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha, parent, false);

        TextView nomeProduto = (TextView) rowView.findViewById(R.id.textViewLisNome);
        TextView quantidade = (TextView) rowView.findViewById(R.id.textViewLisQuant);
        TextView preco = (TextView) rowView.findViewById(R.id.textViewLisPreco);

        nomeProduto.setText(elementos.get(position).getNome());
        quantidade.setText(elementos.get(position).getQuantidade());
        preco.setText(elementos.get(position).getPreço());

        return rowView;
    }
}
