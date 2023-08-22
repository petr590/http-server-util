package x590.util.httpserver;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class HttpUtil {

	private HttpUtil() {}

	public static Map<String, String> parseQueryParameters(String query) {
		if (query == null)
			return Collections.emptyMap();

		var result = new HashMap<String, String>();

		for (String param : query.split("&")) {
			var pair = param.split("=");
			var key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
			var value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
			result.put(key, value);
		}

		return result;
	}

	public static void addAccessControlAllowHeaders(HttpExchange exchange) {
		var responseHeaders = exchange.getResponseHeaders();
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		responseHeaders.add("Access-Control-Allow-Methods", "GET, HEAD, POST, OPTIONS");
		responseHeaders.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		responseHeaders.add("Access-Control-Allow-Credentials", "true");
	}

	public static void sendEmptyResponse(HttpExchange exchange, int status) throws IOException {
		exchange.sendResponseHeaders(status, 0);
		exchange.getResponseBody().close();
	}

	public static void sendResponse(HttpExchange exchange, int status, String response) throws IOException {
		byte[] bytes = response.getBytes();

		exchange.sendResponseHeaders(status, bytes.length);

		var responseBody = exchange.getResponseBody();
		responseBody.write(bytes);
		responseBody.close();
	}
}
