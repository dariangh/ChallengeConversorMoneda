import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class GeneradorDeArchivo {
    public void generarArchivo(String conversiones)throws IOException {


        FileWriter escritura = new FileWriter("Historial de conversiones.txt");
        escritura.write(conversiones);
        escritura.close();
    }
}
