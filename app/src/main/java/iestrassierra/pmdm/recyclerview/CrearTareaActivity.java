package iestrassierra.pmdm.recyclerview;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;


public class CrearTareaActivity extends AppCompatActivity implements PrimerFragment.OnSiguienteClickListener,SegundoFragment.OnVolverClickListener,SegundoFragment.OnGuardarClickListener{

    private FragmentManager fragmentManager;
    private TareaViewModel tareaViewModel;

    private static boolean comprobanteCargarSegundoFragent = false;

    private static boolean  actividadRestaurada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        fragmentManager = getSupportFragmentManager();
        tareaViewModel = new ViewModelProvider(this).get(TareaViewModel.class);
        if (actividadRestaurada && comprobanteCargarSegundoFragent){

            actividadRestaurada = false;
            cargarSegundoFragment();

        } else {

            cargarPrimerFragment();

        }

    }

    private void cargarPrimerFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PrimerFragment primerFragment = new PrimerFragment();
        primerFragment.setOnSiguienteClickListener(this);
        fragmentTransaction.replace(R.id.frameLayoutContainer, primerFragment);
        fragmentTransaction.commit();
    }

    private void cargarSegundoFragment() {
        SegundoFragment segundoFragment = new SegundoFragment();
        segundoFragment.setOnVolverClickListener(this);
        segundoFragment.setOnGuardarClickListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContainer, segundoFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSiguienteClick(String titulo, String fechaCreacion, String fechaObjetivo, boolean prioritaria, int progreso) {

        if (!atributoRelleno(titulo) || !atributoRelleno(fechaCreacion) || !atributoRelleno(fechaObjetivo)) {

            Toast.makeText(getApplicationContext(), getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show();

        } else {
            tareaViewModel.setTitulo(titulo);
            tareaViewModel.setFechaCreacion(fechaCreacion);
            tareaViewModel.setFechaObjetivo(fechaObjetivo);
            tareaViewModel.setPrioritaria(prioritaria);
            tareaViewModel.setProgreso(progreso);
            comprobanteCargarSegundoFragent = true;

            cargarSegundoFragment();

        }
    }



    private boolean atributoRelleno(String atributo) {
        return !atributo.isEmpty();
    }

    @Override
    public void onVolverClick(String titulo, String fechaCreacion, String fechaObjetivo, boolean prioritaria, int progreso, String descripcion) {

        tareaViewModel.setTitulo(titulo);
        tareaViewModel.setFechaCreacion(fechaCreacion);
        tareaViewModel.setFechaObjetivo(fechaObjetivo);
        tareaViewModel.setPrioritaria(prioritaria);
        tareaViewModel.setProgreso(progreso);
        tareaViewModel.setDescripcion(descripcion);
        comprobanteCargarSegundoFragent = false;

        PrimerFragment primerFragment = new PrimerFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContainer, primerFragment);
        primerFragment.setOnSiguienteClickListener(this);
        fragmentTransaction.commit();

    }

    @Override
    public void onGuardarClick() {

        String titulo = tareaViewModel.getTituloValue();
        String fechaCreacion = tareaViewModel.getFechaCreacionValue();
        String fechaObjetivo = tareaViewModel.getFechaObjetivoValue();
        boolean prioritaria = tareaViewModel.getPrioritariaValue();
        int progreso = tareaViewModel.getProgresoValue();
        String descripcion = tareaViewModel.getDescripcionValue();

        if (!atributoRelleno(descripcion)) {

            Toast.makeText(getApplicationContext(), getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show();

        } else {

            Tarea nuevaTarea = new Tarea(titulo, descripcion, progreso, fechaCreacion, fechaObjetivo, prioritaria);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("TareaNueva", nuevaTarea);

            setResult(Activity.RESULT_OK, resultIntent);

            finish();

        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        actividadRestaurada = true;
    }
    }






