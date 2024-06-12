import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsultaMoneda {

    private static final String API_KEY = "016d6cccde1efca572be8966";
    private static final String URL_BASE = "https://v6.exchangerate-api.com/v6/";
    private static final String DOLAR = "USD";
    private static final String PESO_MXN = "MXN";
    private static final String REAL_BRL = "BRL";
    private static final String PESO_ARS = "ARS";
    private final List<String> historialConversiones;

    private final HttpClient client;

    public ConsultaMoneda() {
        this.client = HttpClient.newHttpClient();
        this.historialConversiones = new ArrayList<>();

    }

    public String getCurrencyChange(String monedaB, String monedaF, double cantidad) {

        URI uri = URI.create(URL_BASE + API_KEY + "/pair/" + monedaB + "/" + monedaF + "/" + cantidad);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Moneda formatoMoneda = new Gson().fromJson(response.body(), Moneda.class);

            String formato= String.format("===========\nMoneda Base: %s\nMoneda Final: %s\nCambio: %.2f\nCantidad: %.2f\nTotal: %.2f\nHora de cambio: %s\n===========\n",
                    formatoMoneda.baseCode(), formatoMoneda.targetCode(), formatoMoneda.conversionRate(), cantidad, formatoMoneda.conversionResult(), formatoMoneda.timeLast());
//                   this.generarHistorial(formato);

            historialConversiones.add(formato);

            return formato;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la tasa de cambio.", e);
        }
    }
    public void generarHistorial() throws IOException {

        GeneradorDeArchivo newArchivo = new GeneradorDeArchivo();
        newArchivo.generarArchivo(String.valueOf(historialConversiones));


    }
    public void menuChange() {
        Scanner scanner = new Scanner(System.in);
        try {
            int option;
            double amount=0;
            do {
                System.out.println("\n======Bienvenido========");
                System.out.println("Seleccione el Cambio a saber");
                System.out.println("1 Dolar Estado Unidense(USD) ==> Peso Mexicano(MXN)");
                System.out.println("2 Peso Mexicano(MXN)  ==> Dolar Estado Unidense(USD)");
                System.out.println("3 Dolar Estado Unidense(USD) ==> Real Brasileño(BRL)");
                System.out.println("4 Real Brasileño(BRL) ==> Dolar Estado Unidense(USD)");
                System.out.println("5 Dolar  Estado Unidense(USD)  ==> Peso Argentino");
                System.out.println("6 Peso Argentino ==> Dolar  Estado Unidense(USD)");
                System.out.println("7 Generar archivo con datos consultados");
                System.out.println("8 Salir");
                option = scanner.nextInt();
                if(option==7 || option==8){
                    continue;
                }else{
                    System.out.println("Ingrese el valor que desea convertir");
                    amount = scanner.nextDouble();
                }




                System.out.println("\n");
                switch (option) {
                    case 1:
                        System.out.println(this.getCurrencyChange(DOLAR, PESO_MXN, amount));
                        break;
                    case 2:
                        System.out.println(this.getCurrencyChange(PESO_MXN, DOLAR, amount));
                        break;
                    case 3:
                        System.out.println(this.getCurrencyChange(DOLAR, REAL_BRL, amount));
                        break;
                    case 4:
                        System.out.println(this.getCurrencyChange(REAL_BRL, DOLAR, amount));
                        break;
                    case 5:
                        System.out.println(this.getCurrencyChange(DOLAR, PESO_ARS, amount));
                        break;
                    case 6:
                        System.out.println(this.getCurrencyChange(PESO_ARS, DOLAR, amount));
                        break;
                    case 7:
                        System.out.println("Archivo Generado");
                        generarHistorial();
                        break;
                    case 8:
                        System.out.println("Saliendo ...... :)");
                        break;
                    default:
                        System.out.println("Seleccione una opción válida");
                        break;
                }
            } while (option != 8);
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}


