package com.romertec.webook.controller;

import com.romertec.webook.model.bestbuy.BestbuyRequest;
import com.romertec.webook.model.target.TargetRequest;
import com.romertec.webook.service.BestBuyDocumentService;
import com.romertec.webook.service.TargetDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "romertec/webhook")
public class BestbuyController {

    @Autowired
    BestBuyDocumentService bestBuyDocumentService;

    @RequestMapping(value = "/bestbuy/upload", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@Valid @RequestBody BestbuyRequest request) throws Exception {
        bestBuyDocumentService.generateInvoice(request);
        return ResponseEntity.ok().body("Success");
    }
}
