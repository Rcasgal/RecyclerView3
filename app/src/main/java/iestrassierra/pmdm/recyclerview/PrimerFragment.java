package iestrassierra.pmdm.recyclerview;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

public class PrimerFragment extends Fragment {

    private EditText editTextTitulo, editTextFechaCreacion, editTextFechaObjetivo;

    private Spinner spProgreso;
    private CheckBox checkBoxPrioritaria;
    private Button btnSiguiente;
    private TareaViewModel tareaViewModel;

    private OnSiguienteClickListener listener;


    public PrimerFragment() {
    }

    public interface OnSiguienteClickListener {
        void onSiguienteClick(String titulo, String fechaCreacion, String fechaObjetivo, boolean prioritaria,int progreso);
    }

    public void setOnSiguienteClickListener(OnSiguienteClickListener listener) {
        this.listener = listener;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tareaViewModel = new ViewModelProvider(requireActivity()).get(TareaViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmento1 = inflater.inflate(R.layout.fragment_primer, container, false);

        editTextTitulo = fragmento1.findViewById(R.id.editTextTitulo);
        editTextFechaCreacion = fragmento1.findViewById(R.id.editTextFechaCreacion);
        editTextFechaObjetivo = fragmento1.findViewById(R.id.editTextFechaObjetivo);
        checkBoxPrioritaria = fragmento1.findViewById(R.id.checkBoxPrioritaria);
        spProgreso = fragmento1.findViewById(R.id.spinnerProgreso);
        btnSiguiente = fragmento1.findViewById(R.id.btnSiguiente);



            String titulo = tareaViewModel.getTituloValue();
            String fechaCreacion = tareaViewModel.getFechaCreacionValue();
            String fechaObjetivo = tareaViewModel.getFechaObjetivoValue();
            boolean esPrioritaria = tareaViewModel.getPrioritariaValue();
            int progreso = tareaViewModel.getProgresoValue();


            if (progreso >= 0 && progreso <= 4) {
                spProgreso.setSelection(progreso);
            }

            if (titulo != null) {
                editTextTitulo.setText(titulo);
            }

            if (fechaCreacion != null) {
                editTextFechaCreacion.setText(fechaCreacion);
            }

            if (fechaObjetivo != null) {
                editTextFechaObjetivo.setText(fechaObjetivo);
            }

            checkBoxPrioritaria.setChecked(esPrioritaria);

            if (progreso >= 0 && progreso <= 4) {
                spProgreso.setSelection(progreso);

        }


        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String titulo = editTextTitulo.getText().toString();
                String fechaCreacion = editTextFechaCreacion.getText().toString();
                String fechaObjetivo = editTextFechaObjetivo.getText().toString();
                boolean prioritaria = checkBoxPrioritaria.isChecked();
                int progreso = spProgreso.getSelectedItemPosition();


                if (listener != null) {
                    listener.onSiguienteClick(titulo, fechaCreacion, fechaObjetivo, prioritaria,progreso);
                }
            }
        });

        editTextFechaCreacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                    editTextFechaCreacion.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                }, year, month, day);

                datePickerDialog.show();

            }
        });

        editTextFechaObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, monthOfYear, dayOfMonth) -> {
                    editTextFechaObjetivo.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                }, year, month, day);

                datePickerDialog.show();

            }
        });

        return fragmento1;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("titulo", editTextTitulo.getText().toString());
        outState.putString("fechaCreacion", editTextFechaCreacion.getText().toString());
        outState.putString("fechaObjetivo", editTextFechaObjetivo.getText().toString());
        outState.putBoolean("esPrioritaria", checkBoxPrioritaria.isChecked());
        outState.putInt("progreso", spProgreso.getSelectedItemPosition());
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


            tareaViewModel.setTitulo(titulo);
            tareaViewModel.setFechaCreacion(fechaCreacion);
            tareaViewModel.setFechaObjetivo(fechaObjetivo);
            tareaViewModel.setPrioritaria(esPrioritaria);
            tareaViewModel.setProgreso(progreso);
        }
    }
}

