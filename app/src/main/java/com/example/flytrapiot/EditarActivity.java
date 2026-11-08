package com.example.flytrapiot;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditarActivity extends AppCompatActivity {

    EditText input_codigo, input_tipo, input_nivel, input_observaciones;
    Button boton_editar;

    int id_actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        input_codigo = findViewById(R.id.input_codigo_editar);
        input_tipo = findViewById(R.id.input_tipo_editar);
        input_nivel = findViewById(R.id.input_nivel_editar);
        input_observaciones = findViewById(R.id.input_observaciones_editar);

        boton_editar = findViewById(R.id.boton_enviar_editar);

        Intent intent_actual = getIntent();
        id_actual = intent_actual.getIntExtra("id_actual", -1);

        cargarTrampa(id_actual);

        boton_editar.setOnClickListener(v -> {
            editarTrampa();
        });
    }

    public void editarTrampa() {

        String codigo_valor = String.valueOf(input_codigo.getText()).trim();
        String tipo_valor = String.valueOf(input_tipo.getText()).trim();
        String nivel_valor = String.valueOf(input_nivel.getText()).trim();
        String observaciones_valor = String.valueOf(input_observaciones.getText()).trim();

        if (!codigo_valor.isEmpty() && !tipo_valor.isEmpty() && !nivel_valor.isEmpty()) {

            int nivel = Integer.parseInt(nivel_valor);

            TrampasSQLOpenHelper admin = new TrampasSQLOpenHelper(this);
            SQLiteDatabase db = admin.getWritableDatabase();

            ContentValues dato_update = new ContentValues();
            dato_update.put("codigo_sector", codigo_valor);
            dato_update.put("tipo_trampa", tipo_valor);
            dato_update.put("nivel_captura", nivel);
            dato_update.put("observaciones", observaciones_valor);

            db.update("trampas", dato_update, "id = ?",
                    new String[]{String.valueOf(id_actual)});

            db.close();

            Toast.makeText(this, "Trampa editada", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarTrampa(int id) {

        TrampasSQLOpenHelper admin = new TrampasSQLOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor fila = db.rawQuery(
                "SELECT id, codigo_sector, tipo_trampa, nivel_captura, observaciones FROM trampas WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        if (fila.moveToFirst()) {

            input_codigo.setText(fila.getString(1));
            input_tipo.setText(fila.getString(2));
            input_nivel.setText(String.valueOf(fila.getInt(3)));
            input_observaciones.setText(fila.getString(4));

        } else {
            Toast.makeText(this, "No se encontró la trampa con ID " + id, Toast.LENGTH_SHORT).show();
        }

        fila.close();
        db.close();
    }
}