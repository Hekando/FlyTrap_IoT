package com.example.flytrapiot;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText txtCodigo, txtTipoTrampa, txtNivelTrampa, txtObservaciones;
    Button btnAgregar;
    ListView listaTrampas;

    List<Integer> id_trampas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtCodigo = findViewById(R.id.txtCodigo);
        txtTipoTrampa = findViewById(R.id.txtTipoTrampa);
        txtNivelTrampa = findViewById(R.id.txtNivelTrampa);
        txtObservaciones = findViewById(R.id.txtObservaciones);

        btnAgregar = findViewById(R.id.btnAgregar);
        listaTrampas = findViewById(R.id.listaTrampas);

        cargarListaTrampas();

        btnAgregar.setOnClickListener(v -> {
            crearTrampa();
        });

        listaTrampas.setOnItemClickListener((parent, view, posicion, id) -> {

            int id_actual = id_trampas.get(posicion);

            Intent intent_detalle = new Intent(MainActivity.this, DetalleActivity.class);
            intent_detalle.putExtra("id_actual", id_actual);
            startActivity(intent_detalle);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarListaTrampas();
    }

    public void crearTrampa() {

        String codigo_actual = String.valueOf(txtCodigo.getText()).trim();
        String tipo_actual = String.valueOf(txtTipoTrampa.getText()).trim();
        String nivel_actual = String.valueOf(txtNivelTrampa.getText()).trim();
        String observaciones_actual = String.valueOf(txtObservaciones.getText()).trim();

        if (!codigo_actual.isEmpty() && !tipo_actual.isEmpty() && !nivel_actual.isEmpty()) {

            try {
                int nivel = Integer.parseInt(nivel_actual);

                TrampasSQLOpenHelper admin = new TrampasSQLOpenHelper(this);
                SQLiteDatabase db = admin.getWritableDatabase();

                ContentValues registro = new ContentValues();
                registro.put("codigo_sector", codigo_actual);
                registro.put("tipo_trampa", tipo_actual);
                registro.put("nivel_captura", nivel);
                registro.put("observaciones", observaciones_actual);

                db.insert("trampas", null, registro);
                db.close();

                txtCodigo.setText("");
                txtTipoTrampa.setText("");
                txtNivelTrampa.setText("");
                txtObservaciones.setText("");

                Toast.makeText(this, "Trampa agregada correctamente", Toast.LENGTH_SHORT).show();

                cargarListaTrampas();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Ingrese un nivel de captura válido", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_LONG).show();
        }
    }

    public void cargarListaTrampas() {

        TrampasSQLOpenHelper admin = new TrampasSQLOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        ArrayList<String> listaTrampasTexto = new ArrayList<>();
        id_trampas = new ArrayList<>();

        Cursor fila = db.rawQuery(
                "SELECT id, codigo_sector, tipo_trampa, nivel_captura FROM trampas",
                null
        );

        if (fila.moveToFirst()) {

            do {
                String trampa_texto = "Sector: " + fila.getString(1)
                        + " //// Tipo: " + fila.getString(2)
                        + " //// Capturas: " + fila.getInt(3);

                listaTrampasTexto.add(trampa_texto);
                id_trampas.add(fila.getInt(0));

            } while (fila.moveToNext());

        } else {
            listaTrampasTexto.add("No se encontraron trampas en la base de datos");
        }

        fila.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaTrampasTexto
        );

        listaTrampas.setAdapter(adapter);
    }
}