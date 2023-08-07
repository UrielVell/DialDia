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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {

    EditText txtNombreUsuario;
    EditText txtCorreoElectronico;
    EditText txtContrasena;
    Button btnRegistrarse;
    ImageButton btnAtras;

    FirebaseAuth auth;
    DatabaseReference referenciaUsuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombreUsuario = (EditText) findViewById(R.id.txtNombreUsuario);
        txtCorreoElectronico = (EditText) findViewById(R.id.txtEmailRegistro);
        txtContrasena = (EditText) findViewById(R.id.txtPasswordRegistro);
        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);
        btnAtras = (ImageButton) findViewById(R.id.btnAtrasRegistro);

        auth = FirebaseAuth.getInstance();
        referenciaUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreUsuario = txtNombreUsuario.getText().toString().trim();
                String correo = txtCorreoElectronico.getText().toString().trim();
                String contrasena = txtContrasena.getText().toString().trim();

                if(TextUtils.isEmpty(nombreUsuario)||TextUtils.isEmpty(correo)||TextUtils.isEmpty(contrasena)){
                    Toast.makeText(Registro.this, "Complete todos los Datos", Toast.LENGTH_SHORT).show();
                } else if (contrasena.length()>6) {
                    Toast.makeText(Registro.this, "La contaseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog dialog = new ProgressDialog(Registro.this);
                    dialog.setMessage("Registrando Usuario...");
                    dialog.show();
                    auth.createUserWithEmailAndPassword(correo,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                int numRecambioInicial =0;
                                int milIniciales =0;
                                int milEntrada =0;
                                String idUsuario = auth.getUid();
                                UsuarioClass nuevoUsuario = new UsuarioClass(nombreUsuario,correo,numRecambioInicial,milIniciales,milEntrada);
                                referenciaUsuarios.child(idUsuario).setValue(nuevoUsuario);
                                dialog.dismiss();
                                Toast.makeText(Registro.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                irInicioSesion();
                            }else{
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                Toast.makeText(Registro.this, errorCode, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    public void irInicioSesion(){
        Intent intent = new Intent(getApplicationContext(), InicioSesion.class);
        startActivity(intent);
    }
}