package com.romertec.webook.controller;

import com.romertec.webook.model.uber.UberRequest;
import com.romertec.webook.model.walmart.WalmartRequest;
import com.romertec.webook.service.UberDocumentService;
import com.romertec.webook.service.WalmartDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "romertec/webhook")
public class WalmartController {

    @Autowired
    WalmartDocumentService walmartDocumentService;
    @RequestMapping(value = "/walmart/upload", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@Valid @RequestBody WalmartRequest request) throws Exception {
        walmartDocumentService.generateInvoice(request);
        return ResponseEntity.ok().body("Success");
    }
}
