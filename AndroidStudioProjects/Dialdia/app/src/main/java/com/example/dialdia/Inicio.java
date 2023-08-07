package com.example.dialdia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Inicio extends AppCompatActivity {

    FirebaseAuth auth;

    DatabaseReference referenciaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        TextView nombreUsuario = (TextView) findViewById(R.id.txtNombre);
        TextView correoUsuario = (TextView) findViewById(R.id.txtCorreo);

        String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        cargarInfo(idUsuario, nombreUsuario,correoUsuario);
    }

    private void cargarInfo(String idUsuario, TextView nombreUsuario, TextView correoUsuario) {
        referenciaUsuario = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUsuario);
        referenciaUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nombre=null;
                String correo=null;
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    nombre = snapshot.child("nombreUsuario").getValue(String.class);
                    correo = snapshot.child("correo").getValue(String.class);
                }
                nombreUsuario.setText(nombre);
                correoUsuario.setText(correo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}