package com.example.appdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Form_login extends AppCompatActivity {

    private EditText edit_email, edit_senha;
    private Button bt_entrar;
    private TextView txt_criar_conta, txt_mensagemErro;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();
        IniciarComponentes(); // aqui vou iniciar o componente


        txt_criar_conta.setOnClickListener(new View.OnClickListener() {   // vamos criar um evento de click nesse texto
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Form_login.this, Form_Cadastro.class);
                startActivity(intent);
            }
        });

        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                //validação
                if (email.isEmpty() || senha.isEmpty())  {      // se o email tiver vazio e o senha, ira mostrar a mensagem de erro
                    txt_mensagemErro.setText("Preencha todos os campos");

                }else {
                    txt_mensagemErro.setText("");   //senão, a gente quer autenticar o usuário
                    AutenticarUsuario();
                }

            }
        });
    }

    public void AutenticarUsuario() {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //se for sucesso, quero habilitar a barra de progresso e dure 3 segundos em tela, depois parta para tela de produtos
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IniciarTelaProdutos();  // essa é a proxima tela que será iniciado
                          //  progressBar.setVisibility(View.GONE);
                        }
                    }, 3000);  //depois que passe 3 segundo na tela, irá ir para a proxima tela

                }else {

                    String erro;

                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao logar usuário";
                    }
                    txt_mensagemErro.setText(erro);
                }
            }
        });
    }

    public void IniciarTelaProdutos(){
        Intent intent = new Intent(Form_login.this, Lista_Produtos.class);
        startActivity(intent);
        finish();
    }




    @Override
    protected void onStart() { //metodo, é um ciclo de vida, toda as vezes que essa activity for iniciada vai está dentro do on start, sendo executado . Onde fazemos a verificação, se o usuario tiver logado, ele vai direto para a nossa tela principal
        super.onStart();


        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser(); //quando saio do app, mas ainda na tela do produto, tenho que voltar novamente para essa tela, e nao para a tela de login, esse objetivo

        if (usuarioAtual !=null){// se o usuario for diferente de nullo é porque tem usuario logado. Se o usuario for igual a null quer dizer que nao tem usuario logado.
            IniciarTelaProdutos();
        }


    }

    //metodo para iniciar os componentes
    public void IniciarComponentes() { //ficou azul porque colocamos acima IniciarComponentes(); tudo que tiver aqui dentro, ira iniciar
        txt_criar_conta = findViewById(R.id.txt_criar_conta);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        txt_mensagemErro = findViewById(R.id.txt_mensagemErro);
        progressBar = findViewById(R.id.progressBar);

    }

}