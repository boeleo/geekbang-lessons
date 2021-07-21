package org.geektimes.microprofile.rest;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

/**
 * 
 * Test {@link DefaultRestClientBuilder} with POST
 *
 */
public class DefaultRestClientBuilderPostTest {
	public static void main(String[] args) throws MalformedURLException {
		ShutDownService shutdownService = RestClientBuilder.newBuilder()
				.baseUrl(new URL("http://localhost:9001"))
				.build(ShutDownService.class);

		System.out.println(shutdownService.shutdown());
	}
}

/**
 * ShutDown Service
 *
 */
interface ShutDownService {

	@POST
	@Path("/actuator/shutdown")
	@Timeout(500)
	String shutdown();
}
