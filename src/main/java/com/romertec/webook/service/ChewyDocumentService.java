package com.romertec.webook.service;

import com.cloudconvert.exception.CloudConvertClientException;
import com.cloudconvert.exception.CloudConvertServerException;
import com.romertec.webook.model.allhungry.AllHungryRequest;
import com.romertec.webook.model.chewy.ChewyRequest;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ChewyDocumentService {

    public void generateInvoice(ChewyRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException;

}
