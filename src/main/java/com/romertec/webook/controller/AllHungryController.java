package com.romertec.webook.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romertec.webook.model.allhungry.AllHungryOrder;
import com.romertec.webook.model.allhungry.AllHungryRequest;
import com.romertec.webook.model.target.TargetRequest;
import com.romertec.webook.service.AllhungryDocumentService;
import com.romertec.webook.service.TargetDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "romertec/build")
public class AllHungryController {

    @Autowired
    AllhungryDocumentService allhungryDocumentService;

    @RequestMapping(value = "/allhungry/invoice/generate", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@Valid @RequestBody AllHungryRequest request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<AllHungryOrder> ordersTemplate = mapper.readValue(new File("src/main/resources/data/allhungry/data.json"),
                new TypeReference<List<AllHungryOrder>>() {});
        Random rand = new Random();
        int randomIndex = rand.nextInt(ordersTemplate.size());
        AllHungryOrder order = ordersTemplate.get(randomIndex);
        request.setOrder(order);
        allhungryDocumentService.generateInvoice(request);
        return ResponseEntity.ok().body("Success");
    }
}
