package com.example.appdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil_Usuario extends AppCompatActivity {


    private CircleImageView foto_usuario;
    private TextView nome_usuario, email_usuario;
    private Button bt_editPerfil;

    private String usuarioID
;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        IniciarComponentes();
        bt_editPerfil.setOnClickListener(new View.OnClickListener() { //tela do editar perfil
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Perfil_Usuario.this, Editar_Perfil.class); //navegar da tela de Perfil_Usuario para Editar_Perfil
                startActivity(intent);
            }
        });

    }

    //para recuperar o nome, email etc, vamos abrir o ciclo de vida da activity, que é o onStart


    @Override
    protected void onStart() { //então, todas as vezes, que ele iniciar a tela do perfil do usuario, vai ser carregando tudo que tiver dentro do onStart()
        super.onStart();
        //aqui vamos abrir uma conexão com o banco de dados e recuperar esses dados que foram salvos no banco e na parte do usuario

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //agora criamos uma String , onde vamos recuperar por exemplo, o email de outra forma, quando ele nao esta gravado no banco de dados
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);  //esse Usuarios, foi criado no firestore, olhar depois isso, ja que nao fiz la.
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) { //no lugar de value*( que ja vem automaticamente junto), trocar para documentSnapshot, para manter o padrão

                if(documentSnapshot !=null) { //se tiver dados para retornar do nosso banco de dados, a gente vai retornar aqui. Se o documento for diferente de nulo, tem dados para retornar, se for igual, que dizer que nao tem nenhum dado para retornar.
                    //vamos recuperar o id da imagem, para isso, nao se forma estatica e sim, pegaremos um framework. Duas bibliotecas famosas sao, a picasso e glade, as duas fazem a mesma coisa
               //o glade vai pegar a imagem da internet e renderizar para a gente na aplicação
                    Glide.with(getApplicationContext()).load(documentSnapshot.getString("foto")).into(foto_usuario);//get string vai recuperar atraves de uma chave, que nesse caso a chave é a propria foto, o objeto que definimos ja. QUando fazemo isso, conseguimos recuperar a foto atrves de sua chave
                    nome_usuario.setText(documentSnapshot.getString("nome"));
                    email_usuario.setText(email); //fazendo tudo isso, vamos poder recuperar os dados do usuario

                }
                

            }
        });
    }

    public void IniciarComponentes() {
    foto_usuario = findViewById(R.id.foto_usuario);
    nome_usuario = findViewById(R.id.nome_usuario);
    email_usuario = findViewById(R.id.email_usuario);
    bt_editPerfil = findViewById(R.id.bt_editarPerfil);

    }

}