package iestrassierra.pmdm.recyclerview;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class PreferenciasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tamanoFuente = prefs.getString("tamanoLetra", "default");

        if (tamanoFuente.equals("a")) {
            setTheme(R.style.AppTheme_Small);
        } else if (tamanoFuente.equals("b")) {
            setTheme(R.style.AppTheme_Medium);
        } else if (tamanoFuente.equals("c")) {
            setTheme(R.style.AppTheme_Large);
        }

        boolean modoOscuroActivado = prefs.getBoolean("modo_oscuro", false); // Obtener el valor del modo oscuro

        if (modoOscuroActivado) {
            setTheme(R.style.ModoOscuro);
        } else {
            setTheme(R.style.ModoClaro);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Preferencias de usuario");

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference temaPreference = findPreference("tema");
            temaPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object valor) {

                    boolean temaSeleccionado = (boolean) valor;
                    if (temaSeleccionado) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    getActivity().recreate();
                    return true;
                }
            });

            ListPreference fontSizePreference = findPreference("fuente");

            fontSizePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object valor) {

                    String tamanoSeleccionado = (String) valor;

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = prefs.edit();

                    if (tamanoSeleccionado.equals("a")) {
                        editor.putString("tamanoLetra", "a");
                        editor.apply();
                        getActivity().recreate();
                        return true;

                    } else if (tamanoSeleccionado.equals("b")) {
                        editor.putString("tamanoLetra", "b");
                        editor.apply();
                        getActivity().recreate();
                        return true;

                    } else if (tamanoSeleccionado.equals("c")) {
                        editor.putString("tamanoLetra", "c");
                        editor.apply();
                        getActivity().recreate();
                        return true;

                    }

                    return true;
                }
            });

            ListPreference criterioPreference = findPreference("criterio");

            assert criterioPreference != null;
            criterioPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String criterioSeleccionado = (String) newValue;

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = prefs.edit();

                    String valorAlmacenado = "";

                    if (criterioSeleccionado.equals("Alfabético")) {
                        valorAlmacenado = "a";
                    } else if (criterioSeleccionado.equals("Fecha de Creación")) {
                        valorAlmacenado = "b";
                    } else if (criterioSeleccionado.equals("Días Restantes")) {
                        valorAlmacenado = "c";
                    } else if (criterioSeleccionado.equals("Progreso")) {
                        valorAlmacenado = "d";
                    }

                    editor.putString("criterioSeleccionado", valorAlmacenado);
                    editor.apply();

                    getActivity().recreate();
                    return true;
                }
            });






        }


    }

}