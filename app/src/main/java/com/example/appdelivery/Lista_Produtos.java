package com.example.appdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdelivery.Adapter.AdapterProduto;
import com.example.appdelivery.Model.Produto;
import com.example.appdelivery.RecyclerViewItemClickListener.RecyclerViewItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Lista_Produtos extends AppCompatActivity {

    //vou criar um objeto para o recylerview la de activity_lista_produtos.xml

    private RecyclerView recyclerView_produtos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtoList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        recyclerView_produtos = findViewById(R.id.recyclerView_produtos);
        produtoList = new ArrayList<>();
        adapterProduto = new AdapterProduto(getApplicationContext(), produtoList);
        recyclerView_produtos.setLayoutManager(new LinearLayoutManager(getApplicationContext())); //formato da nossa lista, vertical usamos o .setLayoutManager. Tambem podemos passar a lista de produto ou getapplicationContext, tanto faz.
        recyclerView_produtos.setHasFixedSize(true); // aqui vamos melhorar o desempenho da lista, utilizando o .sethasfixedSIze..
        recyclerView_produtos.setAdapter(adapterProduto); //aqui vou setar meu adapter

        //Evento de click no RecyclerView

        recyclerView_produtos.addOnItemTouchListener(
                new RecyclerViewItemClickListener(
                        getApplicationContext(),
                        recyclerView_produtos,
                        new RecyclerViewItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                              //  Produto produto = produtoList.get(position); //irei remover, so comentar, isso era so para testar se os itens estavam escutando mesmo, tipo:quando clicamos no botao aparecia a mensagem daquele item Bolo, coxinha etc.
                              //  Toast.makeText(getApplicationContext(), produto.getNome(), Toast.LENGTH_SHORT).show();// comentar, faz parte de teste de escuta
                                //lembrando que no servidor é tudo string..
                                Intent intent = new Intent(Lista_Produtos.this, Detalhes_Produto.class); // estou na minha lista de produto quero passar para minha lista de detalhes ou tela
                                //ver os detalhes da descrição, então vamos passar dados para outra tela, pegar detalhes de cada item baseado na posição.
                                //Alem de ir para a proxima tela de detalhes, a gente que passsar os dados desse item para a proxima tela, Como fazer de uma tela para outra, usamos o intenção
                                intent.putExtra("foto", produtoList.get(position).getFoto()); //usando o putExtra, vamos poder passar o objeto para a proxima tela, baseado em sua chave, vamos passar a chave e o objeto...(? aqui passsamos a string).
                                //fazendo isso, vou passar para minha intenção a foto, e vou recuperar atraves da chave foto
                                intent.putExtra("nome", produtoList.get(position).getNome());
                                intent.putExtra("descrição", produtoList.get(position).getDescricao());
                                intent.putExtra("preço", produtoList.get(position).getPreco());
                                startActivity(intent); //então nesse bloco, estamos indo para a proxima tela e passando todos detalhes para a proxima tela

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }

                )
        );


        //abrir conexão com firestore

        db = FirebaseFirestore.getInstance();

        db.collection("Produtos").orderBy("nome")  //para colocar em ordm alfabetica, adicionamos orderBy(), nisso passmos o "nome". Ou seja vamos pegar a propriedade nome e ordenar por nome
                .get()  //get recupera tudo que esta dentro
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { //se for um sucesso quero fazer alguma coisa
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        //fazemos uma verificação
                        if (task.isSuccessful()) { // se o task for um sucesso, ou se a consulta for sucesso
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) { //vamos herdar de task, onde vamos pegar o resultado dessa consulta, se for um sucesso.
                                Produto produto = queryDocumentSnapshot.toObject(Produto.class);
                                produtoList.add(produto);
                                //se a gente inserir mais produtos, seremos notificamos quando tiver novos... por isso o uso de .notifyDataSetChanged
                                adapterProduto.notifyDataSetChanged(); //esse metodo vai notificar a lista, quando aparecer novos itens. Então automaticamente, ela vai atualizar para a gente

                            }
                        }

                    }
                });


        //-----------------------------------------------------------------------------------------------------------------
        //primeiro é validar, depois trazer os dados do servidor, por isso criamos uma lista fake
        //   Produto produto = new Produto(R.drawable.ic_launcher_background, "Produto 1", "R$ 50,00");
        //  produtoList.add(produto);

        //   Produto produto2 = new Produto(R.drawable.ic_launcher_background, "Produto 1", "R$ 50,00");
        //   produtoList.add(produto2);

        // Produto produto3 = new Produto(R.drawable.ic_launcher_background, "Produto 1", "R$ 50,00");
        // produtoList.add(produto3);

        //   Produto produto4 = new Produto(R.drawable.ic_launcher_background, "Produto 1", "R$ 50,00");
        //   produtoList.add(produto4);

        //   Produto produto5 = new Produto(R.drawable.ic_launcher_background, "Produto 1", "R$ 50,00");
        //   produtoList.add(produto5);

        //   Produto produto6 = new Produto(R.drawable.ic_launcher_background, "Produto 1", "R$ 50,00");
        //   produtoList.add(produto6);

        //   Produto produto7 = new Produto(R.drawable.ic_launcher_background, "Produto 1", "R$ 50,00");
        //   produtoList.add(produto7);}
        //o bloco acima que contem a lista fake de Produto produto1 etc... seria para remover, mas irei deixar coomentado caso precise futuramente.
    }

    @Override  ///va em generate override methods e escolhe a subescrita
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);  vem junto, entao removemos, apenas comentei, mas podes remover e substituir por getMenuInflater()

        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    //para definir as ações nos icones, perfil, sair e pedidos, vou usar outras subescritas

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemID = item.getItemId(); //vai pegar o id de cada menu
       //nisso faremos uma validação usando o if
        if (itemID == R.id.perfil) {

            Intent intent = new Intent(Lista_Produtos.this, Perfil_Usuario.class);
            startActivity(intent); //quando clicar no perfil, vamos para a tela do perfil_usuario

        }else if (itemID == R.id.pedidos) {


        } else if (itemID == R.id.deslogar) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Lista_Produtos.this, "Usuário Deslogado", Toast.LENGTH_SHORT).show();
            //depois de deslogar o usuario, quero voltar para a minha tela de login
            Intent intent = new Intent(Lista_Produtos.this, Form_login.class); //dentro do intent temos a listaproduto, onde quero voltar para a minha form login.
            startActivity(intent); //aqui vou inicializar a minha intenção start...
            finish(); //finalizar o contexto atual utilizando o metodo finish
        }
        return super.onOptionsItemSelected(item);
    }
}