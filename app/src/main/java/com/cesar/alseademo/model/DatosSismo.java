package com.cesar.alseademo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DatosSismo implements Parcelable {
    private String titulo;
    private String nomreLugar;
    private String colorAlerta;
    private double lat;
    private double lon;
    private String fecha;
    private String fechaActualización;

    public DatosSismo(){
    }

    protected DatosSismo(Parcel in) {
        titulo = in.readString();
        nomreLugar = in.readString();
        colorAlerta = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        fecha = in.readString();
        fechaActualización = in.readString();
    }

    public static final Creator<DatosSismo> CREATOR = new Creator<DatosSismo>() {
        @Override
        public DatosSismo createFromParcel(Parcel in) {
            return new DatosSismo(in);
        }

        @Override
        public DatosSismo[] newArray(int size) {
            return new DatosSismo[size];
        }
    };

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNomreLugar() {
        return nomreLugar;
    }

    public void setNomreLugar(String nomreLugar) {
        this.nomreLugar = nomreLugar;
    }

    public String getColorAlerta() {
        return colorAlerta;
    }

    public void setColorAlerta(String colorAlerta) {
        this.colorAlerta = colorAlerta;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFechaActualización() {
        return fechaActualización;
    }

    public void setFechaActualización(String fechaActualización) {
        this.fechaActualización = fechaActualización;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(nomreLugar);
        dest.writeString(colorAlerta);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(fecha);
        dest.writeString(fecha);
    }
}
