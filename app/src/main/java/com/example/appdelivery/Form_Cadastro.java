package com.example.appdelivery;

import android.bluetooth.BluetoothLeAudioCodecStatus;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Form_Cadastro extends AppCompatActivity {


    private CircleImageView fotoUsuario;
    private Button bt_selecionarfoto, bt_cadastrar;
    private EditText edit_nome, edit_email, edit_senha;
    private TextView txt_mensagemErro;

    private  String usuarioID;
    private Uri mSelecionarUri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);


        IniciarComponentes();  //iniciar os componentes
        edit_nome.addTextChangedListener(cadastroTextWatcher);// campo de texto vai ouvir, por isso Listener. Vamos passar o observador de texto que é CadastroTextWatcher
        edit_email.addTextChangedListener(cadastroTextWatcher);
        edit_senha.addTextChangedListener(cadastroTextWatcher);


        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CadastrarUsuario(view);  //metodo, depois daqui, criaremos o metodo abaixo public void CadastrarUsuario
            }
        });

        bt_selecionarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelecionarFotoGaleria();
            }
        });

    }

    public void CadastrarUsuario(View view) { // dentro desse metodo vamos ter todo o processo de usuario

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        // Instance = vai recuperar a instancia do servidor do firebase
       //createUserWithEmailAndPassword() = esse metodo vai criar um usuario com email e senha e dentro dos (), ele espera dois parametros que é o email e a senha

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {  //task é uma propriedade responsavel por autentica e cadastrar o usuario. Nesse contexto, ele será responsavel por cadastrar o usuario. Nisso ele vai autenticar

            //vamos fazer uma validação

                if (task.isSuccessful()) {  // se o cadastro for realizado com sucesso, quero mostrar uma mensagem ao usuario, que o cadastro foi realizado com sucesso

                  SalvarDadosUsuario();


                    //podemos usar o task para mostrar a mensagem, mas é limitado, ou usamos uma mensagem personalizada como o Snack,onde conseguimos mudar a cor de fundo, cor do texto etc, conseguimos colocar ações, diferentes do task
                    Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE)  //classe Snackbar
                    //dentro do metodo make, esperamos tres parametros, onde esperamos uma view, a mensagem, e duração. Nesse caso nao temos a view, mas podemos pegar a view do proprio OnClickListener
                    //dai passamos  a view em public void CadastrarUsuario("View view") la em cima, e em public void onClick ("v") e na minha Snack.make('view', "xxxx'), passamos uma view e a mensagem
                    //.LENGTH_INDEFINITE = a mensagem so vai sumir quando realizarmos uma ação

                    //agora definimos uma ação para essa mensagem
                            .setAction("OK", new View.OnClickListener() { //aqui colocamos um botao nessa mensagem, que é setAction ("OK"), e nesse botao, vamos poder clicar nele, a mensagem so vai sumir quando clicado no botao "OK"
                                @Override
                                public void onClick(View view) {

                                    finish(); // aqui finalizamos a activity atual

                                }
                            });
                    //depois de finish, escrevemos aqui o snackbar, onde vai executar a mensagem.
                    snackbar.show();
                }else {   //senao o cadastro nao for realizado com sucesso, exibe uma mensagem de erro sobre.
                    // Nisso, vamos precisar tratar as excessões

                    String erro;


                    try { //try significa tentar , aqui vamos faz todos os tratamento de execessão, senha invalida, email invalido. Ele vai tentar exibir tudo que esta dentro do try

                        throw task.getException(); // o throw significa lançar, então : eu quero lançar uma excessão a parte do nosso objeto task, que tem o cadastro do usuario
                    }catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Coloque uma senha com no mínimo 6 caracteres!";

                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail inválido";
                    }catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esta conta já foi cadastrada!";
                    }catch (FirebaseNetworkException e) { //vai verificar se a internet esta ligada, senao estiver ele vai informar ao usuario
                        erro = "Sem conexão com a internet";
                  //  }catch (Exception e){    // uma excessão, caso nao dispare nenhuma dessas, ou seja se nao for nenhuma dessas mensagens vai aparecer 'Erro ao cadastrar o usuario'
                    }catch (Exception e) { //caso ele nao consiga exibir o try, ele vai cair no catch e vai mostrar para a gente uma excessão padrão: Exception default.
                        erro = "Erro ao cadastrar o usuário";
                    }
                    txt_mensagemErro.setText(erro); // a variavel erro vai ser adaptável ao nosso Exception
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

    public void SalvarDadosUsuario() {
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

                                DocumentReference documentReference =db.collection("Usuarios").document(usuarioID); // para cada usuario que for criado, a gente vai criar um documento baseado no usuario Id, de cada usuario cadastrado. Nisso, garantimos que nenhum usuario será duplicado e substituido.
                                documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        //vou usar um log temporario, so para saber se foi sucesso mesmo
                                        Log.i("db", "Sucesso ao salvar os dados");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    //um log aqui tambem, se caso os dados nao forem salvos, tambem quero saber
                                      Log.i("db_erro", "Erro ao salvar os dados" + e.toString());
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

    public void IniciarComponentes() {
        fotoUsuario = findViewById(R.id.fotoUsuario);
        bt_selecionarfoto = findViewById(R.id.bt_selecionarFoto);
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        txt_mensagemErro = findViewById(R.id.txt_mensagemErro);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);

    }

    TextWatcher cadastroTextWatcher = new TextWatcher() { //TextWatcher é o observador de texto
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String nome = edit_nome.getText().toString();
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();



                //so vai se retornar verdadeiro se todos os campos forem preenchidos
            // se todos os campos estiverem preenchidos, vai habilitar o botao e botao vai ficar na cor vermelha
            if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty() ) {   //inverter a condição // se o nosso nome nao estiver vazio eee o email nao estiver vazio eee a nossa senha nao estiver vazio, o que a gente que fazer é habilitar o botao
                bt_cadastrar.setEnabled(true);
                 bt_cadastrar.setBackgroundColor(getResources().getColor(R.color.dark_red));
                //bt_cadastrar.setBackgroundColor(ContextCompat.getColor(Form_Cadastro.this, R.color.dark_red)); // Atualizado
                //senao estiver preenchido, todos os campos, a gente vai deixar o botao desabilitado
            }else {
                bt_cadastrar.setEnabled(false);
                bt_cadastrar.setBackgroundColor(getResources().getColor(R.color.gray));
              //  bt_cadastrar.setBackgroundColor(ContextCompat.getColor(Form_Cadastro.this, R.color.gray)); // Atualizado
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

    };
}