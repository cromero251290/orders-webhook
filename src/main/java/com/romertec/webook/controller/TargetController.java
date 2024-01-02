package com.romertec.webook.controller;


import com.romertec.webook.model.target.TargetRequest;
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
public class TargetController {
    @Autowired
    TargetDocumentService targetDocumentService;

    @RequestMapping(value = "/target/upload", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@Valid @RequestBody TargetRequest request) throws Exception {
        targetDocumentService.generateInvoice(request);
        return ResponseEntity.ok().body("Success");
    }

}