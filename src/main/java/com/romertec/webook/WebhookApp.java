package com.romertec.webook;


import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class WebhookApp {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebhookApp.class, args);
    }
}


