package pl.gulci.airlysched;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.gulci.airlysched.model.Measurement;
import pl.gulci.airlysched.model.Station;

import java.util.Arrays;
import java.util.Collections;

@Component
public class ScheduledTasks {

    @Scheduled(cron = "*/10 * * * * *")
    public void refreshAirCondition() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("apikey", "5QTAeNrpDjAhj0zsH1mCst7w00VVpCZn");
        HttpEntity<String> headersEntity = new HttpEntity<>("parameters", headers);

        String url = "https://airapi.airly.eu/v2/installations/nearest?lat=52.4050175&lng=16.8645072&maxDistanceKM=5&maxResults=-1";
        ResponseEntity<Station[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, headersEntity, Station[].class);
        Station[] response = responseEntity.getBody();

        if (response != null) {
            Arrays.stream(response).forEach(item -> {
                System.out.println("==================================================");
                System.out.println(item.getId()
                        + " ("
                        + item.getAddress().getDisplayAddress1()
                        + ", "
                        + item.getAddress().getDisplayAddress2()
                        + ")");
                System.out.println("==================================================");

                String measurementUrl = "https://airapi.airly.eu/v2/measurements/installation?installationId=" + item.getId();
                ResponseEntity<Measurement> measurementResponseEntity = restTemplate.exchange(measurementUrl,
                        HttpMethod.GET,
                        headersEntity,
                        Measurement.class);
                Measurement measurementResponse = measurementResponseEntity.getBody();

                if (measurementResponse != null) {
                    Arrays.stream(measurementResponse.getCurrent().getValues()).forEach(value ->
                            System.out.println(value.getName() + ": " + value.getValue()));
                }
            });
        }
    }
}
