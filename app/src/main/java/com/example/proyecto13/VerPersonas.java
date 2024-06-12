package com.example.proyecto13;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyecto13.conexion.SQLiteConexion;
import com.example.proyecto13.transacciones.Personas;
import com.example.proyecto13.transacciones.Transacciones;

import java.util.ArrayList;

public class VerPersonas extends AppCompatActivity {

    SQLiteConexion conexion;
    ArrayList<Personas> listaPersonas;
    ArrayList<String> arregloPersonas;
    ArrayAdapter adp;

    Button regresar, actualizar, eliminar;

    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_personas);
        getSupportActionBar().setTitle("Lista de Personas");
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);


        regresar = ( Button ) findViewById(R.id.regresar2);
        actualizar = ( Button ) findViewById(R.id.actualizar);
        eliminar = ( Button ) findViewById(R.id.eliminar);
        lista = ( ListView ) findViewById(R.id.lista);

        ObtenerListaContactos();

        adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arregloPersonas);
        lista.setAdapter(adp);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerPersonas.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int color = Color.parseColor("#abaaad");
                view.setBackgroundColor(color);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = lista.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    int id = listaPersonas.get(seleccionado).getId();
                    Eliminar(id);
                    ObtenerListaContactos();
                    adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arregloPersonas);
                    lista.setAdapter(adp);
                }
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = lista.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    int id = listaPersonas.get(seleccionado).getId();
                    Actualizar(id);
                }
            }
        });
    }

    public void ObtenerListaContactos()
    {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Personas persona = null;
        listaPersonas = new ArrayList<Personas>();

        // Cursor
        Cursor cursor = db.rawQuery("SELECT * FROM personas", null );

        while(cursor.moveToNext())
        {
            persona = new Personas();

            persona.setId(cursor.getInt(0));
            persona.setNombre(cursor.getString(1));
            persona.setApellido(cursor.getString(2));
            persona.setNumero(cursor.getString(3));
            persona.setCorreo(cursor.getString(4));
            persona.setDireccion(cursor.getString(5));

            listaPersonas.add(persona);
        }

        Toast.makeText(this, "Personas en Total: " + listaPersonas.size(), Toast.LENGTH_SHORT).show();

        cursor.close();
        Filling();
    }

    public void Filling()
    {
        arregloPersonas = new ArrayList<String>();
        for(int i = 0; i < listaPersonas.size(); i++)
        {
            arregloPersonas.add(listaPersonas.get(i).getId() + " | "+
                    listaPersonas.get(i).getNombre() + " " + listaPersonas.get(i).getApellido() + " | " +
                    listaPersonas.get(i).getNumero());
        }
    }

    public void Eliminar(int id){
        SQLiteDatabase db = conexion.getReadableDatabase();
        String[] argumentos = { String.valueOf(id) };
        String condicion = "id = ?";
        db.delete(Transacciones.tablaPersonas, condicion, argumentos);
        Toast.makeText(this, "Registro Eliminado Correctamente", Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void Actualizar(int id){
        Intent intent = new Intent(VerPersonas.this, AgregarPersona.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}