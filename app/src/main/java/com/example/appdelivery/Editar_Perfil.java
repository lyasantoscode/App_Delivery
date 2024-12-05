package com.example.appdelivery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Editar_Perfil extends AppCompatActivity {

    private CircleImageView fotoUsuario;
    private EditText edit_nome;
    private Button bt_atualizarDados, bt_selecionarFoto;
    private Uri mSelecionarUri;
    private String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editar_perfil);

        IniciarComponentes();

        bt_selecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelecionarFotoGaleria();
            }
        });

        bt_atualizarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = edit_nome.getText().toString();
                if (nome.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view,"Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else {
                    AtualizarDadosPerfil(view);  // e por fim, passamos o view aqui. Lembrando que começou la em Snackbar, bloco de atualizar dados.
                }
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == AppCompatActivity.RESULT_OK) {// SE O NOSSO getresultcode for sucesso, sendo igual a Activity.ResultOK,  a gente que fazer alguma coisa, alguma intenção
                        Intent data = result.getData();
                        mSelecionarUri = data.getData();

                        try {
                            fotoUsuario.setImageURI(mSelecionarUri);
                        }catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public void  SelecionarFotoGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK); //PEGAR FOTO NA GALERIA, ESSA É A INTENÇÃO
        intent.setType("image/*");//quando faço isso, isso quer dizer ele so vai mostrar imagem na minha galeria
        activityResultLauncher.launch(intent);
    }

    public void AtualizarDadosPerfil(View view){  //passamos uma view aqui, depois de passar o Snackbar snackbar = Snacker.make(view), no bloco de atualizar os dados. Depois daqui, tambem passamos no metodo a view AtualizarDadosPerfil(view) no codigo acima.

        String nomeArquivo = UUID.randomUUID().toString(); //aqui vamos criar um nome de arquivo, ele vai gerar formar aleatoria.
        final StorageReference reference = FirebaseStorage.getInstance().getReference("/imagens/" + nomeArquivo); //aqui estou criando uma referencia para meu firebasestorage. E nessa referencia, vamos criar uma pasta lá no servidor. Uma pasta chamada /imagens/ e vamos concatenar com o nome do arquivo, que vai ser gerado de forma aleatorio
        //além disso, se todas as vezes for colocar uma imagem na pasta, ela ira ter um id unico, nunca vai se repetir
        //=======================
        reference.putFile(mSelecionarUri) // esse putfile espera uma uri
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                // Log.i("url img", uri.toString()); // quando mandar isso la para o firebase, vamos pdoer visualizar isso

                                String foto = uri.toString();

                                //Iniciar o banco de dados - Firebase

                                String nome = edit_nome.getText().toString();

                                //Agora inicamos o banco, depois de ter feito ajuste acima

                                FirebaseFirestore db = FirebaseFirestore.getInstance(); // a variavel de campo db ja tem a instancia do banco de dado

                                //criar um mep de string e objeto, String vai ser a chave, e o proprio objeto que vamos passar ao banco
                                //A chave é para a gente recuperar depois o perfil do usuario. E objeto, é objeto em si, que vamos passar ao banco
                                Map<String, Object> usuarios = new HashMap<>(); //vamos char tudo isso (map<string e objeto>) como usuarios
                                usuarios.put("nome", nome);  //esse metodo put espera dois parametros, uma chave do nosso objeto e o objeto value que é o proprio objeto em si
                                //exe de chave: quero mandar para o banco de dados o meu nome. Então, o usuario vai digitar o nome la no editnoome, nisso vamos ter o nome do usuario. Então criamos uma chave com nome, que chamamos de nome "nome", e depois passamos o objeto nome
                                // essa chave é pra recuperar em outra tela. Então utilizando a chave nome, depois vou conseguir recuperar o nome do usuario, daí vou poder saber o nome de quem eu cadastrei
                                usuarios.put("foto", foto); // então ja estamos pasando o nome e foto no map de string e objeto. Falando de um modo geral.


                                // a linha que contem o codigo é de DocumentReference document reference.
                                //esse document vem la do firebase.
                                //quando passamos collection, estamos criando uma coleção de usuários.
                                //essa coleção vai receber o documento, so que esse documento nao vai ser gerado aleatorio, mas sim baseado no Id de cada usuario
                                //===========
                                //o que essa variavel usuario Id tem, ela tem a instancia do firebase auth, o usuario atual, e o id de cada usuario
                                usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();  //metodo getCurrentUser vai obter o usuario atual e usuario autenticado  eo getuid, que é id do usuario

                                db.collection("Usuarios").document(usuarioID) // para cada usuario que for criado, a gente vai criar um documento baseado no usuario Id, de cada usuario cadastrado. Nisso, garantimos que nenhum usuario será duplicado e substituido.
                                        .update("nome", nome, "foto", foto)  //nesse bloco vamos atualizar os dados
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Snackbar snackbar = Snackbar.make(view, "Sucesso ao atualizar os dados", Snackbar.LENGTH_INDEFINITE)
                                                        .setAction("OK", new View.OnClickListener() { //botao OK da ação
                                                            @Override
                                                            public void onClick(View view) {
                                                                 finish();
                                                            }
                                                        });
                                                snackbar.show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {// usamos para caso deia algum erro tambem
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() { ////caso deia algum erro
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    public void IniciarComponentes(){
        fotoUsuario = findViewById(R.id.fotoUsuario);
        edit_nome = findViewById(R.id.edit_nome);
        bt_atualizarDados = findViewById(R.id.bt_atualizarDados);
        bt_selecionarFoto = findViewById(R.id.bt_selecionarFoto);
    }
}