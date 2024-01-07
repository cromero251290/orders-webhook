package com.romertec.webook.controller;

import com.romertec.webook.model.abebooks.AbebooksRequest;
import com.romertec.webook.model.csv.CSVRequest;
import com.romertec.webook.service.AbebooksDocumentService;
import com.romertec.webook.service.CSVDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "romertec/webhook")
public class CSVController {

    @Autowired
    CSVDocumentService csvDocumentService;
    @RequestMapping(value = "/csv/upload", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@Valid @RequestBody CSVRequest request) throws Exception {
        csvDocumentService.generateInvoice(request);
        return ResponseEntity.ok().body("Success");
    }
}
