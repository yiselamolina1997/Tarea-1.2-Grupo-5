package com.example.proyecto13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyecto13.conexion.SQLiteConexion;
import com.example.proyecto13.transacciones.Personas;
import com.example.proyecto13.transacciones.Transacciones;

public class AgregarPersona extends AppCompatActivity {

    Button guardar, regresar;
    EditText nombre, apellido, numero, correo, direccion;

    Personas personas;
    Boolean actualizacionActiva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_persona2);

        actualizacionActiva = false;

        guardar =  ( Button ) findViewById(R.id.guardar);
        regresar = ( Button ) findViewById(R.id.regresar);

        nombre = ( EditText ) findViewById(R.id.nombre);
        apellido = ( EditText ) findViewById(R.id.apellido);
        numero = ( EditText ) findViewById(R.id.numero);
        correo = ( EditText ) findViewById(R.id.correo);
        direccion = ( EditText ) findViewById(R.id.direccion);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        if (id != -1){
            actualizacionActiva = true;
            getSupportActionBar().setTitle("Actualizar Contacto");
            extraerPersona(String.valueOf(id));
        }else{
            getSupportActionBar().setTitle("Agregar Contacto");
        }

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarPersona.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nombre.getText().toString().equals("")){
                    Toast.makeText(AgregarPersona.this, "Agregue un nombre valido", Toast.LENGTH_LONG).show();
                    return;
                }

                if (apellido.getText().toString().equals("")){
                    Toast.makeText(AgregarPersona.this, "Agregue un apellido valido", Toast.LENGTH_LONG).show();
                    return;
                }

                if (numero.getText().toString().equals("")){
                    Toast.makeText(AgregarPersona.this, "Agregue un numero valido", Toast.LENGTH_LONG).show();
                    return;
                }

                if (correo.getText().toString().equals("")){
                    Toast.makeText(AgregarPersona.this, "Agregue un correo valida", Toast.LENGTH_LONG).show();
                    return;
                }

                if (direccion.getText().toString().equals("")){
                    Toast.makeText(AgregarPersona.this, "Agregue una direccion valida", Toast.LENGTH_LONG).show();
                    return;
                }


                if (id == -1){
                    agregarPersona(nombre.getText().toString(),
                            apellido.getText().toString(),
                            numero.getText().toString(),
                            correo.getText().toString(),
                            direccion.toString());
                }else{
                    actualizarPersona(id,
                            nombre.getText().toString(),
                            apellido.getText().toString(),
                            numero.getText().toString(),
                            correo.getText().toString(),
                            direccion.toString());
                }
            }
        });
    }

    public void agregarPersona(String nombre, String apellido, String numero, String correo, String direccion){
        try{

            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase bd = conexion.getWritableDatabase();
            Personas persona = new Personas(nombre, apellido, numero, correo, direccion);
            ContentValues valores = new ContentValues();
            valores.put("nombre", persona.getNombre());
            valores.put("apellido", persona.getApellido());
            valores.put("numero", persona.getNumero());
            valores.put("correo", persona.getCorreo());
            valores.put("direccion", persona.getDireccion());
            bd.insert(Transacciones.tablaPersonas, "id", valores);
            Toast.makeText(this, "Se ha agregado " + nombre + " a tus agenda!", Toast.LENGTH_SHORT).show();
            limpiar();

        }catch(Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarPersona(int id, String nombre, String apellido, String numero, String correo, String direccion){
        try{

            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase bd = conexion.getWritableDatabase();
            Personas persona = new Personas(nombre, apellido, numero, correo, direccion);
            ContentValues valores = new ContentValues();
            valores.put("nombre", persona.getNombre());
            valores.put("apellido", persona.getApellido());
            valores.put("numero", persona.getNumero());
            valores.put("correo", persona.getCorreo());
            valores.put("direccion", persona.getDireccion());
            String[] idActualizacion = new String[] {String.valueOf(id)};
            bd.update(Transacciones.tablaPersonas, valores,"id=?", idActualizacion);
            Toast.makeText(this, "Se ha actualizado " + nombre + " de tus contactos", Toast.LENGTH_SHORT).show();
            limpiar();

            Intent intent = new Intent(AgregarPersona.this, VerPersonas.class);
            startActivity(intent);

        }catch(Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void extraerPersona(String id){
        try{
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase bd = conexion.getWritableDatabase();
            String[] busqueda = {String.valueOf(id)};
            String[] campos = {"id", "nombre", "apellido", "numero", "correo", "direccion"};
            Cursor cursor =  bd.query(Transacciones.tablaPersonas, campos, "id=?", busqueda, null, null, null);
            cursor.moveToFirst();
            personas = new Personas(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
            nombre.setText(personas.getNombre());
            apellido.setText(personas.getApellido());
            numero.setText(personas.getNumero());
            correo.setText(personas.getCorreo());
            direccion.setText(personas.getDireccion());
        }catch(Exception ex){
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void limpiar() {
        nombre.setText(Transacciones.Empty);
        apellido.setText(Transacciones.Empty);
        numero.setText(Transacciones.Empty);
        correo.setText(Transacciones.Empty);
        direccion.setText(Transacciones.Empty);
    }
}