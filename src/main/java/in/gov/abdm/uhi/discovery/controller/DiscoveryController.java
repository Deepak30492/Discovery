package in.gov.abdm.uhi.discovery.controller;

import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.uhi.discovery.service.DiscoveryService;
import in.gov.abdm.uhi.discovery.service.beans.Response;
import reactor.core.publisher.Mono;

/**
 * @author Deepak Kumar
 *
 */
@RestController
@Validated
@RequestMapping("/requestor")
public class DiscoveryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryService.class);

	@Autowired
	DiscoveryService discoveryService;
	
	@PostMapping(value = "/api/v1/search", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Response> search(@Valid @RequestBody String req) {
		LOGGER.info("Search request| " + req);
		return ResponseEntity.status(HttpStatus.OK).body(discoveryService.process(req));
	}

	@PostMapping(value = "/api/v1/on_search", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Response> on_search(@Valid @RequestBody String req) {
		LOGGER.info("on_Search request| " + req);
		return ResponseEntity.status(HttpStatus.OK).body(discoveryService.generateAck(req));
	}

	@PostMapping("/on_search")
	public Mono<String> responderOnSearch(@RequestBody String message) throws URISyntaxException {

		Mono<String> onSearchForward = null;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node;
		String targetURI = "";
		try {
			node = mapper.readTree(message);
			targetURI = node.get("consumer_uri").asText();

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebClient client = WebClient.create(targetURI);
		try {
			onSearchForward = client.post().uri("/on_search")
					// .header("Authorization", "Bearer MY_SECRET_TOKEN") TODO: Add appropriate
					// header
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(message)).retrieve().bodyToMono(String.class);

		} catch (Exception ex) {

			throw ex;
		}
		return onSearchForward;
	}

}
