package com.example.flytrapiot;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleActivity extends AppCompatActivity {

    private int id_actual;

    TextView txt_codigo, txt_tipoTrampa, txt_nivelCaptura, txt_observacion;
    Button boton_editar, boton_eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_codigo = findViewById(R.id.txt_codigo);
        txt_tipoTrampa = findViewById(R.id.txt_tipoTrampa);
        txt_nivelCaptura = findViewById(R.id.txt_nivelCaptura);
        txt_observacion = findViewById(R.id.txt_observacion);

        boton_editar = findViewById(R.id.boton_editar);
        boton_eliminar = findViewById(R.id.boton_eliminar);

        Intent intent_actual = getIntent();
        id_actual = intent_actual.getIntExtra("id_actual", -1);

        cargarTrampa(id_actual);

        boton_eliminar.setOnClickListener(v -> eliminarTrampa(id_actual));

        boton_editar.setOnClickListener(v -> {
            Intent intent_editar = new Intent(DetalleActivity.this, EditarActivity.class);
            intent_editar.putExtra("id_actual", id_actual);
            startActivity(intent_editar);
        });
    }

    public void cargarTrampa(int id) {

        TrampasSQLOpenHelper admin = new TrampasSQLOpenHelper(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor fila = db.rawQuery(
                "SELECT id, codigo_sector, tipo_trampa, nivel_captura, observaciones FROM trampas WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        if (fila.moveToFirst()) {
            txt_codigo.setText("Código: " + fila.getString(1));
            txt_tipoTrampa.setText("Tipo de trampa: " + fila.getString(2));
            txt_nivelCaptura.setText("Nivel de captura: " + fila.getInt(3));
            txt_observacion.setText("Observaciones: " + fila.getString(4));
        } else {
            Toast.makeText(this, "No se encontró la trampa con ID " + id, Toast.LENGTH_SHORT).show();
        }

        fila.close();
        db.close();
    }

    public void eliminarTrampa(int id) {

        TrampasSQLOpenHelper admin = new TrampasSQLOpenHelper(this);
        SQLiteDatabase db = admin.getWritableDatabase();

        db.delete("trampas", "id = ?", new String[]{String.valueOf(id)});

        db.close();

        Toast.makeText(this, "La trampa fue eliminada", Toast.LENGTH_SHORT).show();

        finish();
    }
}