package com.romertec.webook.service;

import com.cloudconvert.exception.CloudConvertClientException;
import com.cloudconvert.exception.CloudConvertServerException;
import com.romertec.webook.model.target.TargetRequest;
import com.romertec.webook.model.walmart.WalmartRequest;

import java.io.IOException;
import java.net.URISyntaxException;

public interface WalmartDocumentService {

    public void generateInvoice(WalmartRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException;

}
