package com.dariel.metrocontador;

import java.util.Calendar;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private EditText lecturaAnterior;
    private EditText lecturaActual;
    private EditText consumo;
    private EditText pagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lecturaAnterior = (EditText) findViewById(R.id.lecturaAnterior);
        lecturaActual = (EditText) findViewById(R.id.lecturaActual);
        consumo = (EditText) findViewById(R.id.consumo);
        pagar = (EditText) findViewById(R.id.pagar);
        Log.d("Dariel: ", pagar.getText().toString());


        Button btnGetLecturaAnterior = (Button) findViewById(R.id.btnGetlecturaAnterior);
        Button btnGetLecturaActual = (Button) findViewById(R.id.btnGetlecturaActual);

        // opcion de cargar una lectura desde la BD
        btnGetLecturaAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cargarLecturaCustom();
                cargarLectura(false);

            }
        });

        btnGetLecturaActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarLectura(true);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----------------------------------------------------------------
                try {
                    if (isEmpty(lecturaAnterior) || isEmpty(lecturaActual)) {
                        throw new Exception("El consumo actual o el anterior no pueden estar vacíos");
                    } else {
                        int consumoAnterior;
                        int consumoActual;
                        consumoAnterior = Integer.valueOf(lecturaAnterior.getText().toString());
                        consumoActual = Integer.valueOf(lecturaActual.getText().toString());

                        double suma = 0;
                        int kw = consumoActual - consumoAnterior;
                        if (kw < 0) {
                            throw new Exception("La lectura actual debe ser mayor que la anterior");
                        } else if (kw <= 100) {
                            suma = 0.33 * kw;

                        } else if (kw > 100 && kw <= 150) {
                            suma = 32.78 + (kw - 100) * 1.07;

                        } else if (kw > 150 && kw <= 200) {
                            suma = 86.06 + (kw - 150) * 1.43;

                        } else if (kw > 200 && kw <= 250) {
                            suma = 157.78 + (kw - 200) * 2.46;

                        } else if (kw > 250 && kw <= 300) {
                            suma = 280.72 + (kw - 250) * 3;

                        } else if (kw > 300 && kw <= 350) {
                            suma = 430.72 + (kw - 300) * 4;

                        } else if (kw > 350 && kw <= 400) {
                            suma = 630.72+ (kw - 350) * 5;

                        } else if (kw > 400 && kw <= 450) {
                            suma = 880.72 + (kw - 400) * 6;

                        } else if (kw > 450 && kw <= 500) {
                            suma = 1180.72 + (kw - 450) * 7;

                        } else if (kw > 500 && kw <= 600) {
                            suma = 1530.72 + (kw - 500) * 9.20;

                        } else if (kw > 600 && kw <= 700) {
                            suma = 2450.72 + (kw - 600) * 9.45;

                        } else if (kw > 700 && kw <= 1000) {
                            suma = 3995.72 + (kw - 700) * 9.85;

                        } else if (kw > 1000 && kw <= 1800) {
                            suma = 6350.72 + (kw - 1000) * 10.80;

                        } else if (kw > 1800 && kw <= 2600) {
                            suma = 14990.72 + (kw - 1800) * 11.80;

                        } else if (kw > 2600 && kw <= 3400) {
                            suma = 24430.72 + (kw - 2600) * 12.90;

                        } else if (kw > 3400 && kw <= 4200) {
                            suma = 34750.72 + (kw - 3400) * 13.95;

                        } else if (kw > 4200 && kw <= 5000) {
                            suma = 45910.72 + (kw - 4200) * 15;

                        } else if (kw > 5000) {
                            suma = 57910.72 + (kw - 5000) * 20;
                        }
                        pagar.setText(String.valueOf(suma));
                        consumo.setText(String.valueOf(kw));
                    }
                } catch (Exception ex) {
                    Snackbar.make(view, ex.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                //---------------------------------------------------------------

            }
        });


    }

    //recibe una variable boolean para saber si es lectura actual o anterior
    private void cargarLectura(final boolean esLecturaActual) {

        //Codigo para llenar a datos
        AppSqliteHelper sqlite = new AppSqliteHelper(this, "DBMetrocontador", null, 1);
        SQLiteDatabase db = sqlite.getWritableDatabase();

        //Cargando lecturas
        String[] campos = new String[]{"id as _id", "valor", "fecha"};
        Cursor c = db.query("lectura", campos, null, null, null, null, "fecha desc");

        //Creamos un nuevo AlertDialog.Builder pasandole como parametro el contexto
        AlertDialog.Builder ADBuilder = new AlertDialog.Builder(this);

        ADBuilder.setTitle("Lecturas(kw)");//Asignamos un titulo al mensaje

        //Creamos un nuevo ArrayAdapter de 'Strings' y pasamos como parametros (Contexto, int id "Referencia a layout");
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice);

        if (c.moveToFirst()) {

            do {
                //Añadimos los elementos a mostrar
                int valor = c.getInt(1);
                arrayAdapter.add(valor);

            } while (c.moveToNext());
            c.close();
        }
        //Creamos un boton para cancelar el dialog
        ADBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//Cerramos el dialogo
            }
        });

        //Capturamos el evento 'OnClick' de los elementos en el dialogo
        ADBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int _item) {
                if (esLecturaActual) {
                    lecturaActual.setText(arrayAdapter.getItem(_item).toString());
                } else {
                    lecturaAnterior.setText(arrayAdapter.getItem(_item).toString());
                }

                //Creamos un toast para mostrar el elemento selecionado
                Toast.makeText(getApplicationContext(), arrayAdapter.getItem(_item).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        ADBuilder.show();//Mostramos el dialogo
    }

    private void cargarLecturaCustom(){

        //Codigo para llenar a datos
        AppSqliteHelper sqlite = new AppSqliteHelper(this, "DBMetrocontador", null, 1);
        SQLiteDatabase db = sqlite.getWritableDatabase();

        //Cargando lecturas
        String[] campos = new String[]{"id as _id", "valor", "fecha"};
        Cursor c = db.query("lectura", campos, null, null, null, null, "fecha desc");


        final Dialog  dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_dialog,null);

        ListView lv = (ListView) findViewById(R.id.listLecturasDialog);
        CursorAdapterLectura adapter = new CursorAdapterLectura(this,c);
        lv.setAdapter(adapter);

        dialog.setContentView(view);
        dialog.show();

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Calendar c = new GregorianCalendar();

            new AlertDialog.Builder(this)
                    .setTitle("Fecha")
                    .setMessage(c.getTime().toLocaleString() + "\n" + pagar.getText().toString())
                    .show();

            return true;
        }

        if (id == R.id.action_acerca_de) {
            Intent intent = new Intent(this, AcercaDeActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_save) {
            try {
            //validar que exista un calculo
            if (isEmpty(consumo) || isEmpty(pagar)) {
                throw new Exception("Debe realizar un cálculo antes de guardarlo");
            }
            Calendar c = new GregorianCalendar();

            //Abrimos la base de datos ‘DBMetrocontador’ en modo escritura
            AppSqliteHelper sqlite = new AppSqliteHelper(this, "DBMetrocontador", null, 1);
            SQLiteDatabase db = sqlite.getWritableDatabase();

            //Creamos el contenido de la tabla calculo como objeto ContentValues
            ContentValues calculo = new ContentValues();
            calculo.put("consumo", consumo.getText().toString());
            calculo.put("pago", pagar.getText().toString());
            calculo.put("fecha", c.getTime().toString());

            //buscamos si existen las lecturas en la BD
            Cursor cAnt = db.rawQuery("select id from lectura where valor =" + lecturaAnterior.getText().toString() + " limit 1", null);
            if (cAnt.moveToFirst()) {

                //si existen guardamos el id y luego lo insertamos en calculo
                int idAnt = cAnt.getInt(0);
                calculo.put("id_lectura_anterior", idAnt);

            } else {
                // si no la creamos y almismo tiempo le asignamos el id que devuelve a calculo
                ContentValues LecturaAnt = new ContentValues();
                LecturaAnt.put("valor", lecturaAnterior.getText().toString());
                LecturaAnt.put("fecha", c.getTime().toString());

                calculo.put("id_lectura_anterior", db.insert("lectura", null, LecturaAnt));
            }

            Cursor cAct = db.rawQuery("select id from lectura where valor =" + lecturaActual.getText().toString() + " limit 1", null);
            if (cAct.moveToFirst()) {

                //si existen guardamos el id y luego lo insertamos en calculo
                int idAct = cAnt.getInt(0);
                calculo.put("id_lectura_actual", idAct);

            } else {
                // si no la creamos y almismo tiempo le asignamos el id que devuelve a calculo
                ContentValues LecturaAct = new ContentValues();
                LecturaAct.put("valor", lecturaActual.getText().toString());
                LecturaAct.put("fecha", c.getTime().toString());

                calculo.put("id_lectura_actual", db.insert("lectura", null, LecturaAct));
            }
            //finalmente insertamos en calculo
            long status = db.insert("calculo", null, calculo);
            if (status != -1) {

                //Creamos un toast para mostrar el elemento selecionado
                Toast.makeText(this,"Datos guardados correctamente",Toast.LENGTH_SHORT)
                .show();

            }
            db.close();

            } catch (Exception ex) {
                Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT)
                        .show();
            }
            return true;
        }

        if (id == R.id.action_historic) {
            Intent intent = new Intent(this, HistorialActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
