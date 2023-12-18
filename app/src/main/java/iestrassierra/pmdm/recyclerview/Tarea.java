package iestrassierra.pmdm.recyclerview;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tarea implements Parcelable {

    private long id;
    private static long contador = 0;
    private String titulo;
    private String descripcion;
    private int progreso;
    private String fechaCreacion;
    private String fechaObjetivo;
    private boolean prioritaria;

    public Tarea(){}

    public Tarea(String titulo, String descripcion, int progreso, String fechaCreacion, String fechaObjetivo, boolean prioritaria) {

        this.id = contador++;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.progreso = progreso;
        this.fechaCreacion = fechaCreacion;
        this.fechaObjetivo = fechaObjetivo;
        this.prioritaria = prioritaria;

    }


    public void configurarContador(){

        contador--;

    }

    public boolean esPrioritaria() {
        return prioritaria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getProgreso() {
        return progreso;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaObjetivo() {
        return fechaObjetivo;
    }

    public Date getFechaObjetivoDate() {
        Date fechaDate = null;
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        try {
            fechaDate = formatoFecha.parse(fechaObjetivo);
        } catch (ParseException e) {
            e.printStackTrace();
            // Manejo de la excepción si la cadena no puede ser parseada
        }

        return fechaDate;
    }
    public String getFechaObjetivoString(){


        return fechaObjetivo;
    }

    public void setFechaObjetivo(String fechaObjetivo) {
        this.fechaObjetivo = fechaObjetivo;
    }

    public boolean isPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeLong(this.id);
        parcel.writeString(this.titulo);
        parcel.writeString(this.descripcion);
        parcel.writeInt(this.progreso);
        parcel.writeString(this.fechaCreacion);
        parcel.writeString(this.fechaObjetivo);
        parcel.writeByte((byte) (prioritaria ? 1 : 0));
    }
    public static final Creator<Tarea> CREATOR = new Creator<Tarea>() {
        @Override
        public Tarea createFromParcel(Parcel in) {
            return new Tarea(in);
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size];
        }
    };
    protected Tarea(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        descripcion = in.readString();
        progreso = in.readInt();
        fechaCreacion = in.readString();
        fechaObjetivo = in.readString();
        prioritaria = in.readByte() != 0;
    }


}