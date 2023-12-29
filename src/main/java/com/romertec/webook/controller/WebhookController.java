package com.romertec.webook.controller;


import com.romertec.webook.model.AbebooksRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "romertec/webhook")
public class WebhookController {

    private Logger logger = LoggerFactory.getLogger(WebhookController.class);



    @RequestMapping(value = "/abebooks/upload", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@Valid @RequestBody AbebooksRequest request) {
        return ResponseEntity.ok().body("Success");
    }

}