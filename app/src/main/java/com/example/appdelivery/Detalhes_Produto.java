package com.example.appdelivery;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class Detalhes_Produto extends AppCompatActivity {

    private ImageView dt_fotoProduto;
    private TextView dt_nomeProduto, dt_descricaoProduto, dt_precoProduto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        IniciarComponentes();

        //recuperar os objetos
        String foto = getIntent().getExtras().getString("foto"); //.getStrin, porque é string, se fosse int, etc, seria getint etc
        String nome = getIntent().getExtras().getString("nome");
        String descricao = getIntent().getExtras().getString("descrição");
        String preco = getIntent().getExtras().getString("preço");

        Glide.with(getApplicationContext()).load(foto).into(dt_fotoProduto);  //glide renderizar foto
        dt_nomeProduto.setText(nome);
        dt_descricaoProduto.setText(descricao);
        dt_precoProduto.setText(preco); // fazendo isso, conseguimos recuperar os dados de CADA item de lista, baseado no click da posição

    }
    public void IniciarComponentes() {
        dt_fotoProduto = findViewById(R.id.id_fotoProduto);
        dt_nomeProduto = findViewById(R.id.nomeProduto);
        dt_descricaoProduto = findViewById(R.id.dt_descricaoProduto);
        dt_precoProduto = findViewById(R.id.dt_precoProduto);
    }


}