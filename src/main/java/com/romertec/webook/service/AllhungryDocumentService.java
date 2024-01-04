package com.romertec.webook.service;

import com.cloudconvert.exception.CloudConvertClientException;
import com.cloudconvert.exception.CloudConvertServerException;
import com.romertec.webook.model.allhungry.AllHungryRequest;
import com.romertec.webook.model.target.TargetRequest;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AllhungryDocumentService {
    public void generateInvoice(AllHungryRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException;

}
