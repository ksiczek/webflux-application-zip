package com.acme.webfluxapplicationzip;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@WireMockTest
class DownloadArchiveClientTest {

    @Test
    void whenFileIsApplicationZip_itShouldBeMappedToClientResponse(WireMockRuntimeInfo wmri) {
        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .baseUrl("http://localhost:%d".formatted(wmri.getHttpPort()))
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
                        .build();
        DownloadArchiveClient archiveClient = httpServiceProxyFactory.createClient(DownloadArchiveClient.class);

        byte[] fakeZip = new byte[10_000_000];
        Random random = new Random();
        random.nextBytes(fakeZip);

        stubFor(get("/documents/archive").willReturn(aResponse()
                .withStatus(200)
                .withBody(fakeZip)
                .withHeader(HttpHeaders.CONTENT_TYPE, "application/zip"))
        );

        ClientResponse clientResponse = archiveClient.getZipArchive(new HttpHeaders());

        assertNotNull(clientResponse);
    }

}