package iestrassierra.pmdm.recyclerview;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

public class SegundoFragment extends Fragment {

    private TextView textViewTitulo, textViewFechaCreacion, textViewFechaObjetivo;

    private Spinner spProgreso;

    private EditText editTextDescripcion;

    private Button btnVolver,btnGuardar;
    private CheckBox checkBoxPrioritaria;
    private TareaViewModel tareaViewModel;





    public interface OnVolverClickListener {
        void onVolverClick(String titulo, String fechaCreacion, String fechaObjetivo, boolean prioritaria,int progreso,String descripcion);
    }

    public interface OnGuardarClickListener {
        void onGuardarClick();
    }

    private SegundoFragment.OnVolverClickListener listener;
    private SegundoFragment.OnGuardarClickListener listenerGuardar;

    public void setOnVolverClickListener(SegundoFragment.OnVolverClickListener listener) {
        this.listener = listener;
    }

    public void setOnGuardarClickListener(SegundoFragment.OnGuardarClickListener listener) {
        this.listenerGuardar = listener;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_segundo, container, false);

        textViewTitulo = view.findViewById(R.id.textViewTitulo);
        textViewFechaCreacion = view.findViewById(R.id.textViewFechaCreacion);
        textViewFechaObjetivo = view.findViewById(R.id.textViewFechaObjetivo);
        checkBoxPrioritaria = view.findViewById(R.id.checkBoxPrioritaria);
        btnVolver = view.findViewById(R.id.btnVolver);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        spProgreso = view.findViewById(R.id.spinnerProgreso);
        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tareaViewModel.setDescripcion(String.valueOf(editTextDescripcion.getText()));

                if (listener != null) {
                    listenerGuardar.onGuardarClick();
                }

            }
        });


        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titulo = textViewTitulo.getText().toString();
                String fechaCreacion = textViewFechaCreacion.getText().toString();
                String fechaObjetivo = textViewFechaObjetivo.getText().toString();
                boolean prioritaria = checkBoxPrioritaria.isChecked();
                int progreso = spProgreso.getSelectedItemPosition();
                String descripcion = String.valueOf(editTextDescripcion.getText());


                if (listener != null) {
                    listener.onVolverClick(titulo, fechaCreacion, fechaObjetivo, prioritaria,progreso,descripcion);
                }
            }
        });

        iniciarComponentes();

        return view;
    }

    private void iniciarComponentes(){


        tareaViewModel = new ViewModelProvider(requireActivity()).get(TareaViewModel.class);

        String descripcion = tareaViewModel.getDescripcionValue();


        tareaViewModel.getTitulo().observe(getViewLifecycleOwner(), titulo -> {

            textViewTitulo.setText(titulo);

        });

        tareaViewModel.getFechaCreacion().observe(getViewLifecycleOwner(), fechaCreacion -> {

            textViewFechaCreacion.setText(fechaCreacion);

        });

        tareaViewModel.getFechaObjetivo().observe(getViewLifecycleOwner(), fechaObjetivo -> {

            textViewFechaObjetivo.setText(fechaObjetivo);

        });

        tareaViewModel.getPrioritaria().observe(getViewLifecycleOwner(), prioritaria -> {
            checkBoxPrioritaria.setChecked(prioritaria);
        });

        tareaViewModel.getProgreso().observe(getViewLifecycleOwner(), progreso -> {

            spProgreso.setSelection(progreso);

        });

        if (!descripcion.isEmpty()){

            editTextDescripcion.setText(descripcion);

        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("titulo", textViewTitulo.getText().toString());
        outState.putString("fechaCreacion", textViewFechaCreacion.getText().toString());
        outState.putString("fechaObjetivo", textViewFechaObjetivo.getText().toString());
        outState.putBoolean("esPrioritaria", checkBoxPrioritaria.isChecked());
        outState.putInt("progreso", spProgreso.getSelectedItemPosition());
        outState.putString("descripcion",editTextDescripcion.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null){
            String titulo = savedInstanceState.getString("titulo");
            String fechaCreacion = savedInstanceState.getString("fechaCreacion");
            String fechaObjetivo = savedInstanceState.getString("fechaObjetivo");
            boolean esPrioritaria = savedInstanceState.getBoolean("esPrioritaria");
            int progreso = savedInstanceState.getInt("progreso");
            String descripcion = savedInstanceState.getString("descripcion");


            tareaViewModel.setTitulo(titulo);
            tareaViewModel.setFechaCreacion(fechaCreacion);
            tareaViewModel.setFechaObjetivo(fechaObjetivo);
            tareaViewModel.setPrioritaria(esPrioritaria);
            tareaViewModel.setProgreso(progreso);
            tareaViewModel.setDescripcion(descripcion);
        }
    }



}
