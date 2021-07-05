package com.dariel.metrocontador;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by dpto-isw on 30/01/17.
 */

public class CursorAdapterCalculo extends CursorAdapter {

    public CursorAdapterCalculo(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_list,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        TextView titulo = (TextView) view.findViewById(R.id.titulo);
        TextView subtitulo = (TextView) view.findViewById(R.id.subtitulo);

        int consumo = c.getInt(c.getColumnIndexOrThrow("consumo"));
        float pago = c.getFloat(c.getColumnIndexOrThrow("pago"));
        String fecha = c.getString(c.getColumnIndexOrThrow("fecha"));

        titulo.setText(String.valueOf(consumo)+" kw --> "+ "$"+pago);
        subtitulo.setText("calculado el "+fecha);
    }
}
