package iestrassierra.pmdm.recyclerview;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class TareaViewModel extends ViewModel {
    private MutableLiveData<String> titulo = new MutableLiveData<>();
    private MutableLiveData<String> fechaCreacion = new MutableLiveData<>();
    private MutableLiveData<String> fechaObjetivo = new MutableLiveData<>();
    private MutableLiveData<Boolean> prioritaria = new MutableLiveData<>();

    private MutableLiveData<Integer> progreso = new MutableLiveData<>();
    private MutableLiveData<String> descripcion = new MutableLiveData<>();

    private MutableLiveData<Long> id = new MutableLiveData<>();


    public MutableLiveData<String> getTitulo() {
        return titulo;
    }

    public void setTitulo(String tituloValue) {
        titulo.setValue(tituloValue);
    }

    public MutableLiveData<Integer> getProgreso() {
        return progreso;
    }

    public void setProgreso(Integer progresovalue) {
        progreso.setValue(progresovalue);
    }

    public void setId(Long idValue) {
        id.setValue(idValue);
    }

    public MutableLiveData<String> getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacionValue) {
        fechaCreacion.setValue(fechaCreacionValue);
    }

    public MutableLiveData<String> getFechaObjetivo() {
        return fechaObjetivo;
    }

    public void setFechaObjetivo(String fechaObjetivoValue) {
        fechaObjetivo.setValue(fechaObjetivoValue);
    }

    public MutableLiveData<Boolean> getPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(Boolean prioritariaValue) {
        prioritaria.setValue(prioritariaValue);
    }

    public MutableLiveData<String> getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcionValue) {
        descripcion.setValue(descripcionValue);
    }

    public String getTituloValue() {
        return titulo.getValue();
    }

    public int getProgresoValue() {

        if (progreso.getValue() != null){

            return  progreso.getValue();

        } else{

            return 0;
        }

    }

    public long getIdValue() {

        if (id.getValue() != null){

            return  id.getValue();

        } else{

            return 0;
        }

    }
    public String getFechaCreacionValue() {


        return fechaCreacion.getValue();
    }

    public String getFechaObjetivoValue() {


        return fechaObjetivo.getValue();
    }

    public boolean getPrioritariaValue() {
        if (prioritaria.getValue() != null){

            return prioritaria.getValue();

        } else {

            return false;

        }

    }

    public String getDescripcionValue() {

        if(descripcion.getValue() != null){

            return (String)descripcion.getValue();

        } else {

            return "";

        }
    }
}

