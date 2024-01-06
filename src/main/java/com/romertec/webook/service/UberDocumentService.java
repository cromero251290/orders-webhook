package com.romertec.webook.service;

import com.cloudconvert.exception.CloudConvertClientException;
import com.cloudconvert.exception.CloudConvertServerException;
import com.romertec.webook.model.target.TargetRequest;
import com.romertec.webook.model.uber.UberRequest;

import java.io.IOException;
import java.net.URISyntaxException;

public interface UberDocumentService {

    public void generateInvoice(UberRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException;

}
