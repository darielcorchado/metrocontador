package com.dariel.metrocontador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dpto-isw on 23/01/17.
 */

public class AppSqliteHelper extends SQLiteOpenHelper {

    //sql para crear las tablas lectura y calculo
    private String lecturaCreate = "Create table lectura (" +
            "id integer primary key autoincrement not null," +
            " valor  integer not null, " +
            "fecha text not null );";

    private String calculoCreate = "Create table calculo(" +
            "id integer primary key autoincrement not null," +
            "id_lectura_anterior integer," +
            "id_lectura_actual integer not null," +
            "consumo integer not null, " +
            "pago real not null," +
            "fecha text not null," +
            "foreign key (id_lectura_anterior) references lectura (id)," +
            "foreign key (id_lectura_actual) references lectura (id));";

    public AppSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(lecturaCreate);
        db.execSQL(calculoCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //elimino las tablas anteriores
        db.execSQL("drop table if exist lectura; drop table if exist calculo");
        //creo las nuevas tablas
        db.execSQL(lecturaCreate);
        db.execSQL(calculoCreate);
    }
}
