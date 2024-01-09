package com.romertec.webook.service.impl;

import com.cloudconvert.client.CloudConvertClient;
import com.cloudconvert.client.setttings.StringSettingsProvider;
import com.cloudconvert.dto.request.ConvertFilesTaskRequest;
import com.cloudconvert.dto.request.UploadImportRequest;
import com.cloudconvert.dto.request.UrlExportRequest;
import com.cloudconvert.dto.response.TaskResponse;
import com.cloudconvert.dto.result.Result;
import com.cloudconvert.exception.CloudConvertClientException;
import com.cloudconvert.exception.CloudConvertServerException;
import com.romertec.webook.controller.AbebooksController;
import com.romertec.webook.model.walmart.WalmartRequest;
import com.romertec.webook.service.WalmartDocumentService;
import com.romertec.webook.util.WebhookUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service

public class WalmartDocumentServiceImpl implements WalmartDocumentService {
    @Override
    public void generateInvoice(WalmartRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException {
        InputStream invoiceInputStream = AbebooksController.class.getClassLoader().getResourceAsStream("walmart-invoice-template.html");
        File inputInvoiceTemplate = new File("walmart-invoice-template");
        WebhookUtils.convertInputStreamToFile(invoiceInputStream, inputInvoiceTemplate);
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(WebhookUtils.FTP_HOST, WebhookUtils.FTP_PORT);
        ftpClient.login(WebhookUtils.FTP_USERNAME, WebhookUtils.FTP_PASSWORD);
        ftpClient.enterLocalPassiveMode();
        if (ftpClient.isConnected()) {
            String[] receiptArr = request.getPayload().getParsed().get_original_recipient_().split("\\.");
            String rootDir = receiptArr[0];
            String purchaseOrderDir = request.getPayload().getParsed().getOrder_number();
            boolean rootDirExist = ftpClient.changeWorkingDirectory(rootDir);
            if (!rootDirExist) ftpClient.makeDirectory(rootDir);
            ftpClient.changeWorkingDirectory(rootDir);

            boolean purchaseOrderDirExist = ftpClient.changeWorkingDirectory(purchaseOrderDir);
            if (!purchaseOrderDirExist) ftpClient.makeDirectory(purchaseOrderDir);
            ftpClient.changeWorkingDirectory(purchaseOrderDir);

            File tempFile = new File("order.txt");
            List<String> values = new ArrayList<>();
            String itemPriceOnlyNumber = request.getPayload().getParsed().getProduct_price().replace("$", "");
            Random randomTipFee = new Random();

            int tip = randomTipFee.nextInt(5) + 1;

            double total = Double.valueOf(itemPriceOnlyNumber) + 9.95 + tip;
            int tdcDigits = ThreadLocalRandom.current().nextInt(1000, 10000); // 1000 a 9999


            DecimalFormat df = new DecimalFormat("#.00");

            String formattedTotal = df.format(total);
            String formattedTip = df.format(tip);


            String description = request.getPayload().getParsed().getProduct_description();
            String result = description.replace("Qty: 1", "");


            values.add("orderNumber: " + request.getPayload().getParsed().getOrder_number());
            values.add("orderTotal: " + formattedTotal);
            values.add("orderShipping: " + 9.95);
            Files.write(Path.of(tempFile.getPath()), values, StandardCharsets.UTF_8);
            InputStream inputStream = new FileInputStream(tempFile);
            boolean done = ftpClient.storeFile("invoice.txt", inputStream);

            Document invoiceDoc = Jsoup.parse(inputInvoiceTemplate, "UTF-8");

            invoiceDoc.outputSettings().prettyPrint(false);

            String invoiceFormattedHtml = invoiceDoc.toString()
                    .replace("%%ORDER_DATE%%", request.getPayload().getParsed().getOrder_date())
                    .replace("%%ITEM_PRICE%%", request.getPayload().getParsed().getProduct_price())
                    .replace("%%ITEM_DESCRIPTION%%", result)
                    .replace("%%ORDER%%",request.getPayload().getParsed().getOrder_number())
                    .replace("%%TIP%%", formattedTip)
                    .replace("%%HOLD%%", formattedTotal)
                    .replace("%%TOTAL%%", formattedTotal)
                    .replace("%%LAST_DIGITS%%", String.valueOf(tdcDigits));

            InputStream invoiceHtmlInputStream = new ByteArrayInputStream(invoiceFormattedHtml.getBytes(StandardCharsets.UTF_8));
            CloudConvertClient cloudConvertClient = new CloudConvertClient(new StringSettingsProvider(WebhookUtils.CLOUD_CONVERT_API_KEY, "webhook-signing-secret", false));
            final Result<TaskResponse> uploadImportTaskResponseResult = cloudConvertClient.importUsing().upload(new UploadImportRequest(), invoiceHtmlInputStream);
            if (uploadImportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
                final TaskResponse uploadImportTaskResponse = uploadImportTaskResponseResult.getBody();
                cloudConvertClient.tasks().wait(uploadImportTaskResponse.getId());
                final ConvertFilesTaskRequest convertFilesTaskRequest = new ConvertFilesTaskRequest().setInput(uploadImportTaskResponse.getId()).setInputFormat("html").setOutputFormat("pdf").setProperty("page_width","29.7").setProperty("pages", "1");;
                final Result<TaskResponse> convertTaskResponseResult = cloudConvertClient.tasks().convert(convertFilesTaskRequest);
                if (convertTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
                    final TaskResponse convertTaskResponse = convertTaskResponseResult.getBody();
                    final Result<TaskResponse> waitConvertTaskResponseResult = cloudConvertClient.tasks().wait(convertTaskResponse.getId());
                    if (waitConvertTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
                        final UrlExportRequest exportRequest = new UrlExportRequest().setInput(convertTaskResponse.getId());
                        final Result<TaskResponse> exportTaskResponseResult = cloudConvertClient.exportUsing().url(exportRequest);
                        if (exportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
                            final TaskResponse exportTaskResponse = exportTaskResponseResult.getBody();
                            final Result<TaskResponse> waitExportTaskResponseResult = cloudConvertClient.tasks().wait(exportTaskResponse.getId());
                            if (waitExportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
                                TaskResponse showExportTaskResponse = waitExportTaskResponseResult.getBody();
                                final Result<InputStream> inputStreamResult = cloudConvertClient.files().download(showExportTaskResponse.getResult().getFiles().get(0).get("url"));
                                if (inputStreamResult.getStatus().getCode() == HttpStatus.SC_OK) {
                                    String fileName = WebhookUtils.getRandomInvoiceName() + ".pdf";
                                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                    boolean fileHasBeenUploaded = ftpClient.storeFile(fileName, inputStreamResult.getBody());
                                    if (fileHasBeenUploaded) {
                                        System.out.println("The invoice has been successfully uploaded");
                                        System.out.println("Public Url" + showExportTaskResponse.getResult().getFiles().get(0).get("url"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            inputStream.close();
            invoiceHtmlInputStream.close();
            invoiceInputStream.close();
            inputInvoiceTemplate.delete();
            tempFile.delete();
            ftpClient.logout();
            ftpClient.disconnect();
        }

    }
}
