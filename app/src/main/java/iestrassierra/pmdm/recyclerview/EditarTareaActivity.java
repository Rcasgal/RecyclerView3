package iestrassierra.pmdm.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

public class EditarTareaActivity extends AppCompatActivity implements PrimerFragment.OnSiguienteClickListener,SegundoFragment.OnVolverClickListener,SegundoFragment.OnGuardarClickListener{

    private FragmentManager fragmentManager;
    private TareaViewModel tareaViewModel;

    private static boolean comprobanteCargarSegundoFragent = false;

    private static boolean  actividadRestaurada = false;

    public EditarTareaActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tamanoFuente = prefs.getString("tamanoLetra", "default");

        if (tamanoFuente.equals("a")) {
            setTheme(R.style.AppTheme_Small);
        } else if (tamanoFuente.equals("b")) {
            setTheme(R.style.AppTheme_Medium);
        } else if (tamanoFuente.equals("c")) {
            setTheme(R.style.AppTheme_Large);
        }

        Tarea tareaEditar = getIntent().getParcelableExtra("tareaEditar");

        tareaViewModel = new ViewModelProvider(this).get(TareaViewModel.class);

        tareaViewModel.setPrioritaria(tareaEditar.esPrioritaria());
        tareaViewModel.setProgreso(tareaEditar.getProgreso());
        tareaViewModel.setTitulo(tareaEditar.getTitulo());
        tareaViewModel.setFechaCreacion(tareaEditar.getFechaCreacion());
        tareaViewModel.setFechaObjetivo(tareaEditar.getFechaObjetivo());
        tareaViewModel.setId(tareaEditar.getId());
        tareaViewModel.setDescripcion(tareaEditar.getDescripcion());


        fragmentManager = getSupportFragmentManager();

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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SegundoFragment segundoFragment = new SegundoFragment();
        segundoFragment.setOnGuardarClickListener(this);
        segundoFragment.setOnVolverClickListener(this);
        fragmentTransaction.replace(R.id.frameLayoutContainer, segundoFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSiguienteClick(String titulo, String fechaCreacion, String fechaObjetivo, boolean prioritaria, int progreso) {


        if (!todosAtributosRellenos(titulo,fechaCreacion,fechaObjetivo)){

            Toast.makeText(getApplicationContext(), "Se deben rellenar todos los campo", Toast.LENGTH_SHORT).show();

        } else {

        tareaViewModel.setTitulo(titulo);
        tareaViewModel.setFechaCreacion(fechaCreacion);
        tareaViewModel.setFechaObjetivo(fechaObjetivo);
        tareaViewModel.setPrioritaria(prioritaria);
        tareaViewModel.setProgreso(progreso);
        comprobanteCargarSegundoFragent = true;

        SegundoFragment segundoFragment = new SegundoFragment();
        segundoFragment.setOnVolverClickListener(this);
        segundoFragment.setOnGuardarClickListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContainer, segundoFragment);
        fragmentTransaction.commit();
    }
    }

    private boolean todosAtributosRellenos(String titulo, String fechaCreacion, String fechaObjetivo) {
        return !titulo.isEmpty() || !fechaCreacion.isEmpty() || !fechaObjetivo.isEmpty();
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
        long id = tareaViewModel.getIdValue();

        Tarea nuevaTarea = new Tarea(titulo,descripcion,progreso,fechaCreacion,fechaObjetivo,prioritaria);
        nuevaTarea.setId(id);
        nuevaTarea.configurarContador();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("TareaNueva", nuevaTarea);

        setResult(Activity.RESULT_OK, resultIntent);

        finish();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        actividadRestaurada = true;
    }


}
