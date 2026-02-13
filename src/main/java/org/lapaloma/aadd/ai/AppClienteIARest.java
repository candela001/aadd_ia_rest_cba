package org.lapaloma.aadd.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 *
 * ExtractorCine: Clase que realiza ....
 * 
 * @author Isidoro Nevares Martín - IES Virgen de la Paloma
 * @date 5 ene 2026
 * 
 * 
 */

public class AppClienteIARest {
	public static void main(String[] args) throws Exception {
		AppClienteIARest app = new AppClienteIARest();

		String contenidoPromptCliente = "Extrae la información completa, precisa y ordenada de las lineas del metro de Madrid";

		app.cargarDatosIARest(contenidoPromptCliente);

	}

	private void cargarDatosIARest(String contenidoPromptCliente) {
		
		String urlProveedorIA = "https://api.groq.com/openai/v1/chat/completions";
		String apiKey = "aquí_colocas_el_contenido_de_tu_api_key";
		String modeloIA = "openai/gpt-oss-120b";

		try {

			// Se define el prompt del contexto sistema 
			// para así fijar un comportamiento específico a la IA)
			String contextoInicialIA = "Actúa como un extractor de datos JSON. " + "Analiza el contexto proporcionado. "
					+ "Devuelve exclusivamente un objeto JSON con esta estructura: "
					+ "{ 'lineas': [{'nombre_linea': '', 'color':'', 'numero': '', 'kilometros': xx.yy}] }. "
					+ "No escribas nada de texto antes o después del JSON. "
					+ "Extrae solo información de las lineas del metro de Madrid, no de otras ciudades ni transportes.";

			// Se construye el cuerpo de la petición (Payload)
			String jsonInformacionEntrada = """
					{
					    "model": "%s",
					    "messages": [
					        { "role": "system", "content": "%s" },
					        { "role": "user", "content": "%s" }
					    ],
					    "response_format": { "type": "json_object" }
					}
					""".formatted(modeloIA, contextoInicialIA, contenidoPromptCliente);

			// 3. Creamos el cliente y la petición HTTP
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlProveedorIA))
					.header("Authorization", "Bearer " + apiKey).header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonInformacionEntrada)).build();

			// Se envía petición
			System.out.println("Consultando a Groq...");
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			// Se obtiente el resultado
			if (response.statusCode() == 200) {
				System.out.println("Datos extraídos con éxito:");

				// Aquí se llama a un método para procesar el JSON recibido, por ejemplo:
				procesarRespuestaJSON(response.body());

			} else {
				System.out.println("❌ Error: " + response.statusCode());
				System.out.println(response.body());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void procesarRespuestaJSON(String textoJsonResputa) {
		
		System.out.println(textoJsonResputa);

		// Aquí podrías usar una librería como Jackson para convertir el JSON a objetos

	}
}
