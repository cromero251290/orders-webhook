package com.romertec.webook.service;

import com.cloudconvert.exception.CloudConvertClientException;
import com.cloudconvert.exception.CloudConvertServerException;
import com.romertec.webook.model.bestbuy.BestbuyRequest;
import com.romertec.webook.model.chewy.ChewyRequest;

import java.io.IOException;
import java.net.URISyntaxException;

public interface BestBuyDocumentService {
    public void generateInvoice(BestbuyRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException;

}
