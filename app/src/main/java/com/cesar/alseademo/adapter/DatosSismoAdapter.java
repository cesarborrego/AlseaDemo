package com.cesar.alseademo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cesar.alseademo.R;
import com.cesar.alseademo.interfaces.AdapterSelected;
import com.cesar.alseademo.model.DatosSismo;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatosSismoAdapter extends RecyclerView.Adapter<DatosSismoAdapter.DatosSismoHolder>{
    private ArrayList<DatosSismo> datosSismoArrayList;
    public AdapterSelected adapterSelected;
    Context context;


    public DatosSismoAdapter(ArrayList<DatosSismo> datosSismoArrayList, Context context) {
        this.datosSismoArrayList = datosSismoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DatosSismoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dato_sismo_item, parent, false);
        return new DatosSismoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DatosSismoHolder holder, final int position) {
        DatosSismo datosSismo = datosSismoArrayList.get(position);
        holder.titulo.setText(datosSismo.getTitulo());
        holder.lugar.setText(datosSismo.getNomreLugar());
        holder.lat.setText(String.valueOf(datosSismo.getLat()));
        holder.lon.setText(String.valueOf(datosSismo.getLon()));
        holder.fecha.setText(String.format("Fecha del sismo %s",convertTime(datosSismo.getFecha())));
        holder.fechaActualizacion.setText(String.format("Última actualización %s", convertTime(datosSismo.getFechaActualización())));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterSelected.selectedItem(position);
            }
        });
        if (datosSismo.getColorAlerta().equals("green")) {
            holder.alerta.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else if(datosSismo.getColorAlerta().equals("yellow")) {
            holder.alerta.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        } else if(datosSismo.getColorAlerta().equals("orange")) {
            holder.alerta.setBackgroundColor(context.getResources().getColor(R.color.orange));
        } else if(datosSismo.getColorAlerta().equals("red")) {
            holder.alerta.setBackgroundColor(context.getResources().getColor(R.color.red));
        }
    }

    public String convertTime(String time){
        Date date = new Date(Long.parseLong(time));
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    public int getItemCount() {
        return datosSismoArrayList.size();
    }

    public static class DatosSismoHolder extends RecyclerView.ViewHolder{
        private TextView titulo;
        private TextView lugar;
        private TextView lat;
        private TextView lon;
        private TextView fecha;
        private TextView fechaActualizacion;
        private CardView cardView;
        private View alerta;

        public DatosSismoHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tituloId);
            lugar = itemView.findViewById(R.id.lugarId);
            lat = itemView.findViewById(R.id.latitudId);
            lon = itemView.findViewById(R.id.longitudId);
            fecha = itemView.findViewById(R.id.fechaSismoId);
            fechaActualizacion = itemView.findViewById(R.id.fechaActualizacionId);
            cardView = itemView.findViewById(R.id.cardViewId);
            alerta = itemView.findViewById(R.id.colorAlerta);
        }
    }
}
