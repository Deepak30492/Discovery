package in.gov.abdm.uhi.discovery.entity;

import java.time.Instant;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscriber {

	
	@Override
	public String toString() {
		return "Subscriber [subscriberId=" + subscriberId + ", country=" + country + ", city=" + city + ", domain="
				+ domain + ", signingPublicKey=" + signingPublicKey + ", encryptedPublicKey=" + encryptedPublicKey
				+ ", validFrom=" + validFrom + ", validUntil=" + validUntil + ", status=" + status + ", endpoint="
				+ endpoint + ", type=" + type + ", url=" + url + "]";
	}

	@JsonProperty("subscriber_id")
	private String subscriberId;

	private String country;

	private String city;

	private String domain;
	
	@JsonProperty("signing_public_key")
	private String signingPublicKey;
	
	@JsonProperty("encr_public_key")
	private String encryptedPublicKey;

	private Instant validFrom;

	private Instant validUntil;
	
	private String status;
	
	private String endpoint;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String type;
	
	private String url;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Subscriber() {

	}


	

	public String getSubscriberId() {
		return subscriberId;
	}

	public Subscriber(String subscriberId, String country, String city, String domain, String signingPublicKey,
			String encryptedPublicKey, Instant validFrom, Instant validUntil, String status, String endpoint,
			String type, String url) {
		super();
		this.subscriberId = subscriberId;
		this.country = country;
		this.city = city;
		this.domain = domain;
		this.signingPublicKey = signingPublicKey;
		this.encryptedPublicKey = encryptedPublicKey;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.status = status;
		this.endpoint = endpoint;
		this.type = type;
		this.url = url;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Subscriber(JSONObject o) {
		// TODO Auto-generated constructor stub
	}

	

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSigningPublicKey() {
		return signingPublicKey;
	}

	public void setSigningPublicKey(String signingPublicKey) {
		this.signingPublicKey = signingPublicKey;
	}

	public String getEncryptedPublicKey() {
		return encryptedPublicKey;
	}

	public void setEncryptedPublicKey(String encryptedPublicKey) {
		this.encryptedPublicKey = encryptedPublicKey;
	}

	public Instant getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Instant validFrom) {
		this.validFrom = validFrom;
	}

	public Instant getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Instant validUntil) {
		this.validUntil = validUntil;
	}


}
