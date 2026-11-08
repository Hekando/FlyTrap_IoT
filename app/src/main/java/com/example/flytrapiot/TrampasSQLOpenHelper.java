package com.example.flytrapiot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TrampasSQLOpenHelper extends SQLiteOpenHelper
{
    static final String NOMBRE_BASE_DATOS = "flytrap_db";
    static final int VERSION = 1;

    public TrampasSQLOpenHelper(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE = "CREATE TABLE trampas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "codigo_sector TEXT NOT NULL, " +
                "tipo_trampa TEXT NOT NULL, " +
                "nivel_captura INTEGER NOT NULL, " +
                "observaciones TEXT)";

        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int version_anterior,
                          int nueva_version) {

        db.execSQL("DROP TABLE IF EXISTS trampas");

        onCreate(db);
    }
}
