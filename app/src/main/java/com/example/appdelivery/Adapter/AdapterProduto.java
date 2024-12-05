package com.example.appdelivery.Adapter;

import android.content.ContentProvider;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdelivery.Model.Produto;
import com.example.appdelivery.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder> {
//holder vai criar visualização, e quem vai exibir é onBindViewHolder

    private Context context;
    private List<Produto> produtoList;

    public AdapterProduto(Context context, List<Produto> produtoList) {
        this.context = context;
        this.produtoList = produtoList;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        itemLista =layoutInflater.inflate(R.layout.produto_item, parent, false);
        return new ProdutoViewHolder(itemLista);  //nisso, nesse bloco, criamos a visualização
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        //irei comentar e nao excluir o holder , ja que ele pega a foto do android, contudo ira ser substituido por Glide que ai ja pega do servidor firebase
       // holder.foto.setImageResource(produtoList.get(position).getFoto());// holder.foto, isso aqui pega foto do android, o glade pega foto do servidor firebase

        Glide.with(context).load(produtoList.get(position).getFoto()).into(holder.foto);

        holder.nome.setText(produtoList.get(position).getNome());
        holder.preco.setText(produtoList.get(position).getPreco());
        holder.descricao.setText(produtoList.get(position).getDescricao());


    }

    @Override
    public int getItemCount() {

        return produtoList.size();  //antes era return zero, de la passamos produtolistsize, dai ele retornar o tamnho da lista. Logo vai saber o tamanho da lista
    }

    public class ProdutoViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView foto;
        private TextView nome;
        private TextView preco;
        private TextView descricao;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.fotoProduto);
            nome = itemView.findViewById(R.id.nomeProduto);
            preco = itemView.findViewById(R.id.precoProduto);
            descricao = itemView.findViewById(R.id.dt_descricaoProduto);
        }
    }
}
