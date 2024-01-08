package iestrassierra.pmdm.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ListadoTareasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTareas;
    private TextView textViewNoTareas;
    private TareaAdapter tareaAdapter;
    private List<Tarea> listaTareas;
    private ActivityResultLauncher<Intent> lanzador;

    private ActivityResultLauncher<Intent> lanzadorPreferencias;
    private boolean filtrarFav = false;

    private SharedPreferences sharedPreferences;

    private IComunicador comunicador = new IComunicador() {
        @Override
        public void editarTarea(Tarea tareaEditar) {

            Intent intentEditarTarea = new Intent(getApplicationContext(), EditarTareaActivity.class);
            intentEditarTarea.putExtra("tareaEditar", tareaEditar);
            lanzador.launch(intentEditarTarea);

        }

        @Override
        public void eliminartarea(Tarea eliminarTarea) {

            if (eliminar(eliminarTarea)) {
                Toast.makeText(getApplicationContext(), getString(R.string.eliminacion_exitosa), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.eliminacion_fallida), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void mostrarDescripcion(Tarea tareaDescripcion) {

            String descripcion = tareaDescripcion.getDescripcion();

            mostrarDes(descripcion);

        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("listaTareas", new ArrayList<>(listaTareas));

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);



        if (savedInstanceState != null) {

            ArrayList<Tarea> restoredList = savedInstanceState.getParcelableArrayList("listaTareas");
            if (restoredList != null) {
                listaTareas = restoredList;
            }

            actualizarInterfazConListaTareas();
        }
    }

    private void actualizarInterfazConListaTareas() {
        tareaAdapter = new TareaAdapter(listaTareas, comunicador);
        recyclerViewTareas.setAdapter(tareaAdapter);

        if (tareaAdapter.getItemCount() == 0) {
            recyclerViewTareas.setVisibility(View.GONE);
            textViewNoTareas.setVisibility(View.VISIBLE);
        } else {
            recyclerViewTareas.setVisibility(View.VISIBLE);
            textViewNoTareas.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tamanoFuente = prefs.getString("tamanoLetra", "default");

        if (tamanoFuente.equals("a")) {
            setTheme(R.style.AppTheme_Small);
        } else if (tamanoFuente.equals("b")) {
            setTheme(R.style.AppTheme_Medium);
        } else if (tamanoFuente.equals("c")) {
            setTheme(R.style.AppTheme_Large);
        }


        recyclerViewTareas = findViewById(R.id.recyclerViewTareas);
        textViewNoTareas = findViewById(R.id.textViewNoTareas);

        Calendar fechaTarea1 = new GregorianCalendar(2024, Calendar.MAY, 3);

        Calendar fechaTarea2 = new GregorianCalendar(2024, Calendar.AUGUST, 25);

        Date fechaTarea1Date = fechaTarea1.getTime();
        Date fechaTarea2Date = fechaTarea2.getTime();
        listaTareas = new ArrayList<>();
        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        listaTareas.add(new Tarea("Tarea 1", "Descripción de la Tarea 1", 2, formato.format(new Date()), formato.format(fechaTarea1Date), true));
        listaTareas.add(new Tarea("Tarea 2", "Descripción de la Tarea 2", 0, formato.format(new Date()), formato.format(fechaTarea2Date), false));

        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(this));
        tareaAdapter = new TareaAdapter(listaTareas, comunicador);
        recyclerViewTareas.setAdapter(tareaAdapter);

        if (tareaAdapter.getItemCount() == 0) {
            recyclerViewTareas.setVisibility(View.GONE);
            textViewNoTareas.setVisibility(View.VISIBLE);
        } else {
            recyclerViewTareas.setVisibility(View.VISIBLE);
            textViewNoTareas.setVisibility(View.GONE);
        }

        lanzador = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent intentDevuelto = result.getData();
                            Tarea tareaNueva = (Tarea) intentDevuelto.getExtras().get("TareaNueva");
                            if (añadirTarea(tareaNueva) == 0) {

                                Toast.makeText(getApplicationContext(), getString(R.string.edicion_exitosa), Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(getApplicationContext(), getString(R.string.creacion_exitosa), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.creacion_fallida), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void mostrarDes(String descripcion) {

        ScrollView scrollView = new ScrollView(getApplicationContext());
        TextView textView = new TextView(getApplicationContext());
        textView.setText(descripcion);

        int marginInDp = 20;
        float scale = getResources().getDisplayMetrics().density;
        int marginInPixels = (int) (marginInDp * scale + 0.5f);
        textView.setPadding(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
        textView.setTextSize(18);

        scrollView.addView(textView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.descripcion))
                .setView(scrollView)
                .setPositiveButton(getString(R.string.cerrar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private int añadirTarea(Tarea nuevaTarea) {

        int posicion = 0;

        for (Tarea tarea : listaTareas) {

            if (nuevaTarea.getId() == tarea.getId()) {

                listaTareas.set(posicion, nuevaTarea);

                tareaAdapter = new TareaAdapter(listaTareas, comunicador);
                recyclerViewTareas.setAdapter(tareaAdapter);

                if (tareaAdapter.getItemCount() == 0) {
                    recyclerViewTareas.setVisibility(View.GONE);
                    textViewNoTareas.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewTareas.setVisibility(View.VISIBLE);
                    textViewNoTareas.setVisibility(View.GONE);
                }

                return 0;

            }

            posicion = posicion + 1;

        }

        listaTareas.add(nuevaTarea);
        tareaAdapter = new TareaAdapter(listaTareas, comunicador);
        recyclerViewTareas.setAdapter(tareaAdapter);

        if (tareaAdapter.getItemCount() == 0) {
            recyclerViewTareas.setVisibility(View.GONE);
            textViewNoTareas.setVisibility(View.VISIBLE);
        } else {
            recyclerViewTareas.setVisibility(View.VISIBLE);
            textViewNoTareas.setVisibility(View.GONE);
        }

        return 1;

    }

    private boolean eliminar(Tarea tarea) {

        int posicion = 0;

        for (Tarea t : listaTareas) {

            if (t.getId() == tarea.getId()) {

                listaTareas.remove(posicion);

                tareaAdapter = new TareaAdapter(listaTareas, comunicador);
                recyclerViewTareas.setAdapter(tareaAdapter);

                if (tareaAdapter.getItemCount() == 0) {
                    recyclerViewTareas.setVisibility(View.GONE);
                    textViewNoTareas.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewTareas.setVisibility(View.VISIBLE);
                    textViewNoTareas.setVisibility(View.GONE);
                }

                return true;

            }

            posicion = posicion + 1;

        }

        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemAcercaDe:

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle(getString(R.string.app_name));
                dialogo1.setMessage(getString(R.string.info_autor));
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton(getString(R.string.cerrar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        ;
                    }
                });
                ;
                dialogo1.show();

                return true;

            case R.id.menuItemSalir:
                finish();
                return true;

            case R.id.menuItemFavoritas:

                filtrarFavorita();
                return true;

            case R.id.menuItemAnadirTareas:

                Intent intent = new Intent(this, CrearTareaActivity.class);
                lanzador.launch(intent);
                return true;

            case R.id.menuItemPreferencias:

                Intent intentPref = new Intent(this,PreferenciasActivity.class);
                startActivity(intentPref);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void filtrarFavorita() {

        filtrarFav = !filtrarFav;

        if (filtrarFav) {

            tareaAdapter = new TareaAdapter(favoritas(listaTareas), comunicador);

        } else {

            tareaAdapter = new TareaAdapter(listaTareas, comunicador);

        }

        recyclerViewTareas.setAdapter(tareaAdapter);
    }

    private List<Tarea> favoritas(List<Tarea> tareas) {

        List<Tarea> favoritas = new ArrayList<>();

        for (Tarea tarea : tareas) {

            if (tarea.esPrioritaria()) {

                favoritas.add(tarea);

            }
        }

        return favoritas;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tamanoFuente = prefs.getString("tamanoLetra", "default");

        if (tamanoFuente.equals("a")) {
            setTheme(R.style.AppTheme_Small);
        } else if (tamanoFuente.equals("b")) {
            setTheme(R.style.AppTheme_Medium);
        } else if (tamanoFuente.equals("c")) {
            setTheme(R.style.AppTheme_Large);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listado_tareas, menu);



        return true;
    }
}
