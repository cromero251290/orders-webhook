package com.romertec.webook.service;

import com.cloudconvert.exception.CloudConvertClientException;
import com.cloudconvert.exception.CloudConvertServerException;
import com.romertec.webook.model.AbebooksRequest;

import java.io.IOException;
import java.net.URISyntaxException;


public interface AbebooksDocumentService {


    public void generateInvoice(AbebooksRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException;
}
