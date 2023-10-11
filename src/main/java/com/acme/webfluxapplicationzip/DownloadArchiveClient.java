package com.acme.webfluxapplicationzip;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.service.annotation.GetExchange;

public interface DownloadArchiveClient {

    @GetExchange("/documents/archive")
    ClientResponse getZipArchive(@RequestHeader HttpHeaders headers);

}
