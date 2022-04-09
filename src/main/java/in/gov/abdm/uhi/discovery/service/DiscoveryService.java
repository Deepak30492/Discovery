package in.gov.abdm.uhi.discovery.service;

import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import in.gov.abdm.uhi.discovery.controller.DiscoveryController;
import in.gov.abdm.uhi.discovery.entity.Message;
import in.gov.abdm.uhi.discovery.entity.RequestRoot;
import in.gov.abdm.uhi.discovery.entity.Subscriber;
import in.gov.abdm.uhi.discovery.service.beans.Ack;
import in.gov.abdm.uhi.discovery.service.beans.MessageAck;
import in.gov.abdm.uhi.discovery.service.beans.OnTBody;
import in.gov.abdm.uhi.discovery.service.beans.Response;
import reactor.core.publisher.Mono;
import in.gov.abdm.uhi.discovery.service.beans.Error;

/**
 * @author Deepak Kumar
 *
 */
@Service
public class DiscoveryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryService.class);

	@Value("${spring.application.registryurl}")
	String registry_url;

	public Response process(@Valid String req) {
		// Call to lookup
		Message listsubs = lookup(req);
		LOGGER.info("ArraySize for subsribers|" + listsubs.message.size());

		try {
			for (Subscriber subs : listsubs.message) {

				WebClient webClient = WebClient.create(subs.getUrl());
				webClient.post().uri("/search").body(Mono.just(req), Response.class).retrieve()
						.onStatus(HttpStatus::isError, clientResponse -> {
							clientResponse.bodyToMono(String.class).flatMap(responseBody -> {
								LOGGER.info("Body from within flatMap within onStatus: {}", responseBody);
								return Mono.just(responseBody);
							});
							return Mono.error(new RuntimeException("Resolved!"));
						}).bodyToMono(String.class).subscribe();
			}
			return generateAck(req);
		} catch (Exception e) {
			e.printStackTrace();
			generateNack(req, e);
		}
		return generateAck(req);
	}

	public Response generateAck(String req) {
		Ack ack = new Ack();
		Response resp = new Response();
		MessageAck msgack = new MessageAck();
		Error err = new Error("", "", "", "");
		ack.setStatus("ACK");
		msgack.setAck(ack);
		resp.setMessage(msgack);
		resp.setError(err);

		return resp;
	}

	public Response generateNack(String req, Exception ex) {
		Ack ack = new Ack();
		Error err = new Error("", "500", ex.getClass().getCanonicalName(), ex.getMessage());
		Response resp = new Response();
		MessageAck msgack = new MessageAck();
		ack.setStatus("NACK");
		msgack.setAck(ack);
		resp.setMessage(msgack);
		resp.setError(err);

		return resp;
	}

	public static String getValueAsString(String name, JsonNode objectNode) {
		String propertyValue = null;
		JsonNode propertyNode = objectNode.get(name);
		if (propertyNode != null && !propertyNode.isNull()) {
			propertyValue = propertyNode.asText();
		}
		return propertyValue;
	}

	public Message lookup(String req) {
		Message resp = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootnode = objectMapper.readTree(req);
			JsonNode contextNode = rootnode.get("context");
			String country = getValueAsString("country", contextNode);
			String city = getValueAsString("city", contextNode);
			String domain = getValueAsString("domain", contextNode);
			LOGGER.info("registry_url|" + registry_url);

			String lookupRequestString = "{\"country\":\"" + country + "\",\"city\":\"" + city + "\",\"domain\":\""
					+ domain + "\",\"type\":\"BPP\",\"status\":\"SUBSCRIBED\"}";

			LOGGER.info("Lookup_request|" + lookupRequestString);
			Subscriber lookupRequest = objectMapper.readValue(lookupRequestString, Subscriber.class);

			RestTemplate template = new RestTemplate();
			resp = template.postForObject(registry_url + "/api/lookup", lookupRequest, Message.class);
			LOGGER.info("Lookup call successful::" + resp.message.toString());
		} catch (Exception e) {

			e.printStackTrace();
		}
		return resp;

	}
}
