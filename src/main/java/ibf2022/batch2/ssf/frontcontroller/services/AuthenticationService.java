package ibf2022.batch2.ssf.frontcontroller.services;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import ibf2022.batch2.ssf.frontcontroller.respositories.AuthenticationRepository;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class AuthenticationService {

	public static final String AUTHENTICATE = "https://auth.chuklee.com/api/authenticate";

	@Autowired
	private AuthenticationRepository authRepo;

	// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
	public String authenticate(String username, String password) throws Exception {
		JsonObject o = Json.createObjectBuilder()
						.add("username", username)
						.add("password", password)
						.build();

		System.out.println(o.toString());

		RequestEntity<String> req = RequestEntity.post(AUTHENTICATE)
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.APPLICATION_JSON)
									.body(o.toString());
		
		System.out.println(">>>> req ent built");

		RestTemplate template = new RestTemplate();
		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		template.setErrorHandler(new DefaultResponseErrorHandler());

		String message = null;
		ResponseEntity<String> response;
		try {
			response = template.exchange(req, String.class);
		} catch (Exception ex) {
			message = ex.getMessage();
			throw ex;
		}

		String payload = response.getBody();
		JsonReader reader = Json.createReader(new StringReader(payload));
		JsonObject json = reader.readObject();

		String respMessage = json.getString("message");
		
		return respMessage;
	}

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
		authRepo.disableUser(username);
	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return authRepo.isLocked(username);
	}
}

