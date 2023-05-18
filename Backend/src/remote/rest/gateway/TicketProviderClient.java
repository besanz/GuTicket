package remote.rest.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import data.entidades.*;
import remote.rest.dto.*;
import remote.rest.transformer.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class TicketProviderClient {

    private static final String API_BASE_URL = "https://deusto-api.arambarri.eus";
    private static final String TOKEN = "258c263f485581b788b509f2499f49418c640fa412a19ae2a96a7d93a38354f1b06e577ec301a213027acbcde59a9a8ce709862b8e8e6f59c90dbbe6f2a4c43582fa58f384bd7c45016bcd1e61c25358c0e3a9d592dc5e39d60b5825b931ec77ccb228ce133e1360902eb3ec8948aa13ba66bbd8f92df5e1cc5acd00848f1cce";

    private static TicketProviderClient instance = null;

    private TicketProviderClient() {
        // Constructor privado para prevenir su instanciacion.
    }

    // Método estático de acceso global
    public static synchronized TicketProviderClient getInstance() {
        if (instance == null) {
            instance = new TicketProviderClient();
        }
        return instance;
    }

    ArtistaTransformer artistaTransformer = ArtistaTransformer.getInstance();
    EspacioTransformer espacioTransformer = EspacioTransformer.getInstance();
    EventoTransformer eventoTransformer = EventoTransformer.getInstance();
    PrecioTransformer precioTransformer = PrecioTransformer.getInstance();

    public List<Evento> getEventos() throws IOException {
        // Obtener todos los eventos con precios y clientes
        String jsonResponse = makeApiRequest("/api/eventos?populate=precios.cliente");
        JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
        if (jsonObject == null) {
            throw new NullPointerException("Error en el parseo del JSON de eventos");
        }
        JsonArray dataArray = jsonObject.getAsJsonArray("data");
        if (dataArray == null) {
            return new ArrayList<>();
        }
        
        // Filtrar eventos que tienen un precio con cliente ID 1 y construir la lista de eventos filtrados
        List<Evento> eventosFiltrados = new ArrayList<>();
        for (JsonElement element : dataArray) {
            JsonObject eventoObject = element.getAsJsonObject();
            JsonObject attributesObject = eventoObject.getAsJsonObject("attributes");
            if (attributesObject != null) {
                int eventoID = eventoObject.get("id").getAsInt();
                String titulo = attributesObject.get("titulo").getAsString();
                String descripcion = attributesObject.get("descripcion").getAsString();
                String fechaString = attributesObject.get("fecha").getAsString();
                Date fecha = parseFecha(fechaString);
                String createdAt = attributesObject.get("createdAt").getAsString();
                String updatedAt = attributesObject.get("updatedAt").getAsString();
                String publishedAt = attributesObject.get("publishedAt").getAsString();
                int aforo = attributesObject.get("aforo").getAsInt();

                // Obtener precios del evento
                List<Precio> preciosEvento = new ArrayList<>();
                JsonObject preciosObject = attributesObject.getAsJsonObject("precios");
                if (preciosObject != null) {
                    JsonElement preciosDataElement = preciosObject.get("data");
                    if (preciosDataElement != null && preciosDataElement.isJsonArray()) {
                        JsonArray preciosDataArray = preciosDataElement.getAsJsonArray();
                        for (JsonElement precioElement : preciosDataArray) {
                            JsonObject precioObject = precioElement.getAsJsonObject();
                            JsonObject precioAttributesObject = precioObject.getAsJsonObject("attributes");
                            if (precioAttributesObject != null) {
                                int precioID = precioObject.get("id").getAsInt();
                                String nombre = precioAttributesObject.get("nombre").getAsString();
                                double precioValue = precioAttributesObject.get("precio").getAsDouble();
                                int disponibles = precioAttributesObject.get("disponibles").getAsInt();
                                int ofertadas = precioAttributesObject.get("ofertadas").getAsInt();

                                // Obtener el objeto "cliente"
                                Cliente cliente = null;
                                JsonObject clienteObject = precioAttributesObject.getAsJsonObject("cliente");
                                if (clienteObject != null) {
                                    JsonObject clienteDataObject = clienteObject.getAsJsonObject("data");
                                    if (clienteDataObject != null) {
                                        int clienteID = clienteDataObject.get("id").getAsInt();
                                        String nombreCliente = clienteDataObject.getAsJsonObject("attributes")
                                                .get("nombre").getAsString();
                                        String clienteCreatedAt = clienteDataObject.getAsJsonObject("attributes")
                                                .get("createdAt").getAsString();
                                        String clienteUpdatedAt = clienteDataObject.getAsJsonObject("attributes")
                                                .get("updatedAt").getAsString();
                                        String clientePublishedAt = clienteDataObject.getAsJsonObject("attributes")
                                                .get("publishedAt").getAsString();
                                        cliente = new Cliente(clienteID, nombreCliente, clienteCreatedAt,
                                                clienteUpdatedAt, clientePublishedAt);
                                    }
                                }

                                // Filtrar precios con cliente ID 1
                                if (cliente != null && cliente.getId() == 1) {
                                    Precio precioEntity = new Precio(precioID, nombre, precioValue, disponibles, ofertadas, cliente);
                                    preciosEvento.add(precioEntity);
                                }
                            }
                        }
                    }
                }
                // Agregar el evento a la lista de eventos filtrados si tiene precios con cliente ID 1
                if (!preciosEvento.isEmpty()) {
                    Evento evento = new Evento(eventoID, titulo, descripcion, fecha, createdAt, updatedAt, publishedAt, aforo, preciosEvento);
                    eventosFiltrados.add(evento);
                }

            }
        }

        return eventosFiltrados;
    }

    public List<Artista> getArtistasByEventoID(int eventoID) throws IOException {
        String jsonResponse = makeApiRequest("/api/eventos/" + eventoID + "?populate=artistas");
        JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
        if (jsonObject == null) {
            throw new NullPointerException("Error en el parseo del JSON");
        }
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        if (dataObject == null) {
            return new ArrayList<>();
        }
        JsonObject artistasObject = dataObject.getAsJsonObject("attributes").getAsJsonObject("artistas");
        if (artistasObject == null) {
            return new ArrayList<>();
        }
        JsonArray dataArray = artistasObject.getAsJsonArray("data");
        if (dataArray == null) {
            return new ArrayList<>();
        }

        Type listType = new TypeToken<ArrayList<ArtistaDTO>>() {
        }.getType();
        List<ArtistaDTO> artistaResponses = new Gson().fromJson(dataArray, listType);

        List<Artista> artistas = artistaTransformer.transform(artistaResponses);

        return artistas;
    }

    public Artista getArtistaByID(int artistaID) throws IOException {
        String jsonResponse = makeApiRequest("/api/artistas/" + artistaID);
        JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
        if (jsonObject == null) {
            throw new NullPointerException("Error en el parseo del JSON");
        }
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        if (dataObject == null) {
            return null;
        }

        ArtistaDTO artistaResponse = new Gson().fromJson(dataObject, ArtistaDTO.class);
        Artista artista = artistaTransformer.transform(artistaResponse);

        return artista;
    }

    public Espacio getEspacioByEventoID(int eventoID) throws IOException {
        String jsonResponse = makeApiRequest("/api/eventos/" + eventoID + "?populate=espacio");
        JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
        if (jsonObject == null) {
            throw new NullPointerException("Error en el parseo del JSON");
        }
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        if (dataObject == null) {
            return null;
        }
        JsonObject attributesObject = dataObject.getAsJsonObject("attributes");
        if (attributesObject == null) {
            return null;
        }
        JsonObject espacioObject = attributesObject.getAsJsonObject("espacio").getAsJsonObject("data");
        if (espacioObject == null) {
            return null;
        }

        Type type = new TypeToken<EspacioDTO>() {
        }.getType();
        EspacioDTO espacioResponse = new Gson().fromJson(espacioObject, type);

        EspacioTransformer espacioTransformer = EspacioTransformer.getInstance();

        Espacio espacio = espacioTransformer.transform(espacioResponse);

        return espacio;
    }

    private String makeApiRequest(String endpoint) throws IOException {
        URL url = new URL(API_BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection == null) {
            throw new NullPointerException("No se pudo abrir la conexión");
        }
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error en la llamada a la API: codigo de respuesta " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        connection.disconnect();
        return sb.toString();
    }

    private Date parseFecha(String fechaString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // O manejar el error de alguna otra manera
        }
    }
}