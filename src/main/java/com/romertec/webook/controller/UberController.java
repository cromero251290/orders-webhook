package com.romertec.webook.controller;

import com.romertec.webook.model.target.TargetRequest;
import com.romertec.webook.model.uber.UberRequest;
import com.romertec.webook.service.TargetDocumentService;
import com.romertec.webook.service.UberDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "romertec/build")
public class UberController {

    @Autowired
    UberDocumentService uberDocumentService;

    @RequestMapping(value = "/uber/invoice/generate", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@Valid @RequestBody UberRequest request) throws Exception {
        uberDocumentService.generateInvoice(request);
        return ResponseEntity.ok().body("Success");
    }
}
