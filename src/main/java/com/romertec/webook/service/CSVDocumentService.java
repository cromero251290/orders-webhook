package com.romertec.webook.service;

import com.cloudconvert.exception.CloudConvertClientException;
import com.cloudconvert.exception.CloudConvertServerException;
import com.romertec.webook.model.chewy.ChewyRequest;
import com.romertec.webook.model.csv.CSVRequest;

import java.io.IOException;
import java.net.URISyntaxException;

public interface CSVDocumentService {
    public void generateInvoice(CSVRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException;

}
