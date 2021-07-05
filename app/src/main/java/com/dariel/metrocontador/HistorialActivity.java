package com.dariel.metrocontador;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TabHost;

public class HistorialActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);


        this.ConfigurarTabs();
        this.CargarLecturasCalculos();

    }

    private void ConfigurarTabs(){

        TabHost tabs = (TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("Lecturas");
        spec.setContent(R.id.tabLecturas);
        spec.setIndicator("Lecturas");

        tabs.addTab(spec);

        spec = tabs.newTabSpec("Cálculos");
        spec.setContent(R.id.tabCalculos);
        spec.setIndicator("Cálculos");

        tabs.addTab(spec);

        tabs.setCurrentTab(0);
    }

    private void CargarLecturasCalculos(){
//Codigo para llenar a datos

        AppSqliteHelper sqlite = new AppSqliteHelper(this, "DBMetrocontador", null, 1);
        SQLiteDatabase db = sqlite.getWritableDatabase();
//Cargando lecturas
        String[] campos = new String[]{ "id as _id","valor", "fecha"};
        Cursor c = db.query("lectura", campos, null, null, null, null, "fecha desc");

        ListView lv = (ListView) findViewById(R.id.listLecturas);
        CursorAdapterLectura adapter = new CursorAdapterLectura(this,c);
        lv.setAdapter(adapter);

        //Cargando calculos
        String[] campos1 =  new String[]{
                "id as _id", "id_lectura_anterior", "id_lectura_actual", "consumo", "pago", "fecha"
        };
        Cursor c1 = db.query("calculo",campos1,null,null,null,null,"fecha DESC");

        ListView lc = (ListView) findViewById(R.id.listCalculos);
        CursorAdapterCalculo adapterCalculo = new CursorAdapterCalculo(this, c1);
        lc.setAdapter(adapterCalculo);
    }

}
