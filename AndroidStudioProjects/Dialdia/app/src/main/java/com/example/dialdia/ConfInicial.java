package com.example.dialdia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ConfInicial extends AppCompatActivity {

    EditText txtUltimpRecambio;
    EditText txtEntradaAsignada;
    EditText txtMililitrosTotales;

    ImageButton btnCerrarSesion;
    Button btnGuardarConf;

    FirebaseAuth auth;
    DatabaseReference referenciaUsuario;

    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_inicial);

        txtUltimpRecambio = (EditText) findViewById(R.id.txtUltimoRecambio);
        txtEntradaAsignada = (EditText) findViewById(R.id.txtMililitrosEntrada);
        txtMililitrosTotales = (EditText) findViewById(R.id.txtMililitrosTotales);
        btnCerrarSesion = (ImageButton) findViewById(R.id.btnLogOut);
        btnGuardarConf = (Button) findViewById(R.id.btnGuardarConf);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        idUsuario = currentUser.getUid();
        referenciaUsuario = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUsuario);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CerrarSesion();
            }
        });

        btnGuardarConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificarDatos();
            }
        });



    }

    public void CerrarSesion(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Cerrar Sesión");
        builder1.setMessage("¿Esta segur@ que quiere Cerrar Sesión?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Continuar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProgressDialog progress = new ProgressDialog(ConfInicial.this);
                        progress.setMessage("Iniciando Sesión...");
                        progress.show();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(ConfInicial.this, "Sesión Cerrada", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        progress.dismiss();
                        Intent intent = new Intent(getApplicationContext(), InicioSesion.class);
                        startActivity(intent);
                    }
                });
        builder1.setNegativeButton(
                "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void VerificarDatos(){
        String ultimoR = txtEntradaAsignada.getText().toString();
        String milE = txtEntradaAsignada.getText().toString();
        String milT = txtEntradaAsignada.getText().toString();
        if (TextUtils.isEmpty(ultimoR)||TextUtils.isEmpty(milE)||TextUtils.isEmpty(milT)){
            Toast.makeText(this, "Complete los campos faltantes", Toast.LENGTH_SHORT).show();
        }else{
            int milEntrada = Integer.parseInt(txtEntradaAsignada.getText().toString());
            int ultimoRecambio = Integer.parseInt(txtUltimpRecambio.getText().toString());
            int milTotales = Integer.parseInt(txtMililitrosTotales.getText().toString());
            if (milEntrada==0){
                Toast.makeText(this, "No deje en cero el segundo campo", Toast.LENGTH_SHORT).show();
            }else {
                ActualizarDatos(ultimoRecambio, milEntrada,milTotales);
            }
        }
    }

    public void ActualizarDatos(int ultimoRecambio, int milEntrada, int milTotales){
        ProgressDialog dialog = new ProgressDialog(ConfInicial.this);
        dialog.setMessage("Actualizando datos...");
        dialog.show();
        HashMap datosActualizados = new HashMap();
        datosActualizados.put("milTotales" ,milTotales);
        datosActualizados.put("numRecambio" ,ultimoRecambio);
        datosActualizados.put("milEntrada" ,milEntrada);

        referenciaUsuario.updateChildren(datosActualizados).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                dialog.dismiss();
                Toast.makeText(ConfInicial.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Inicio.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfInicial.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

}