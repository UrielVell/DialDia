package com.example.dialdia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesion extends AppCompatActivity {

    EditText txtCorreoElectronico;
    EditText txtContrasena;
    TextView registro;
    Button btnIniciarSesion;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        txtCorreoElectronico = (EditText) findViewById(R.id.txtEmailInicio);
        txtContrasena = (EditText) findViewById(R.id.txtPasswordInicio);
        registro = (TextView) findViewById(R.id.registrateAqui);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesion);

        auth = FirebaseAuth.getInstance();

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irRegistro();
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = txtCorreoElectronico.getText().toString().trim();
                String contrasena = txtContrasena.getText().toString().trim();

                if (TextUtils.isEmpty(correo)||TextUtils.isEmpty(contrasena)){
                    Toast.makeText(InicioSesion.this, "Complete todos los Datos", Toast.LENGTH_SHORT).show();
                }else {
                    ProgressDialog dialog = new ProgressDialog(InicioSesion.this);
                    dialog.setMessage("Iniciando Sesión...");
                    dialog.show();
                    auth.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                auth.getCurrentUser();
                                dialog.dismiss();
                                Intent intent = new Intent (getApplicationContext(), ConfInicial.class);
                                startActivity(intent);

                            }else {
                                dialog.dismiss();
                                Toast.makeText(InicioSesion.this, "Error, verifique sus Datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    public void irRegistro(){
        Intent intent = new Intent(getApplicationContext(), Registro.class);
        startActivity(intent);
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(this, ConfInicial.class);
            startActivity(intent);
        }
    }


}