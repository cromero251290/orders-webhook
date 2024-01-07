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
import com.romertec.webook.model.bestbuy.BestbuyRequest;
import com.romertec.webook.service.BestBuyDocumentService;
import com.romertec.webook.util.WebhookUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service

public class BestBuyDocumentServiceImpl implements BestBuyDocumentService {
    @Override
    public void generateInvoice(BestbuyRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException {
        List<String> urlResourceList = request.getPayload().getParsed().getResource_url();
        String firstUrl = urlResourceList.get(0);
        InputStream invoiceInputStream = AbebooksController.class.getClassLoader().getResourceAsStream("bestbuy-order-invoice.html");
        File inputInvoiceTemplate = new File("bestbuy-order-invoice");
        WebhookUtils.convertInputStreamToFile(invoiceInputStream, inputInvoiceTemplate);
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(WebhookUtils.FTP_HOST, WebhookUtils.FTP_PORT);
        ftpClient.login(WebhookUtils.FTP_USERNAME, WebhookUtils.FTP_PASSWORD);
        ftpClient.enterLocalPassiveMode();
        if (ftpClient.isConnected()) {
            String rootDir = "cromero";
            String targetDir = "bestbuy";
            String emailDir = request.getPayload().getParsed().get_to_();
            String purchaseOrderDir = request.getPayload().getParsed().getOrder_number();
            boolean rootDirExist = ftpClient.changeWorkingDirectory(rootDir);
            if (!rootDirExist) ftpClient.makeDirectory(rootDir);
            ftpClient.changeWorkingDirectory(rootDir);

            boolean targetDirExist = ftpClient.changeWorkingDirectory(targetDir);
            if (!targetDirExist) ftpClient.makeDirectory(targetDir);
            ftpClient.changeWorkingDirectory(targetDir);

            boolean emailDirExist = ftpClient.changeWorkingDirectory(emailDir);
            if (!emailDirExist) ftpClient.makeDirectory(emailDir);
            ftpClient.changeWorkingDirectory(emailDir);

            boolean purchaseOrderDirExist = ftpClient.changeWorkingDirectory(purchaseOrderDir);
            if (!purchaseOrderDirExist) ftpClient.makeDirectory(purchaseOrderDir);
            ftpClient.changeWorkingDirectory(purchaseOrderDir);

            File tempFile = new File("order.txt");
            List<String> values = new ArrayList<>();

            String itemPriceOriginalValue = request.getPayload().getParsed().getProduct_price();
            String itemPriceOnlyNumber = itemPriceOriginalValue.replace("$", "");
            double itemPrice = Double.valueOf(itemPriceOnlyNumber);
            double subtotal = itemPrice;
            Random shippingRandom = new Random();
            double randomDouble = shippingRandom.nextDouble();
            double scaledDouble = 10.00 + (randomDouble * (15.00 - 10.00));
            double shipping = Math.round(scaledDouble * 100.0) / 100.0;
            double total =  itemPrice + shipping;
            int tdcDigits = ThreadLocalRandom.current().nextInt(1000, 10000); // 1000 a 9999
            DecimalFormat df = new DecimalFormat("#.00");

            String formattedShipping = df.format(shipping);
            String formattedTotal = df.format(total);
            String formattedSubtotal = df.format(subtotal);
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            String formattedDate = currentDate.format(formatter);


            values.add("orderNumber: " + request.getPayload().getParsed().getOrder_number());
            values.add("orderTotal: " + formattedTotal);
            values.add("orderShipping: " + formattedShipping);
            values.add("email: " + request.getPayload().getParsed().get_to_());
            Files.write(Path.of(tempFile.getPath()), values, StandardCharsets.UTF_8);
            InputStream inputStream = new FileInputStream(tempFile);
            boolean done = ftpClient.storeFile("invoice.txt", inputStream);
            Document invoiceDoc = Jsoup.parse(inputInvoiceTemplate, "UTF-8");

            invoiceDoc.outputSettings().prettyPrint(false);

            String invoiceFormattedHtml = invoiceDoc.toString()
                    .replace("%%ORDER_DATE%%", formattedDate)
                    .replace("%%ORDER%%", request.getPayload().getParsed().getOrder_number())
                    .replace("%%TOTAL%%", formattedTotal)
                    .replace("%%TOTAL%%", formattedTotal)
                    .replace("%%PRODUCT_DESCRIPTION%%", request.getPayload().getParsed().getProduct_description())
                    .replace("%%MODEL%%", request.getPayload().getParsed().getModel_number())
                    .replace("%%SKU%%", request.getPayload().getParsed().getSku())
                    .replace("%%LAST_DIGITS%%", String.valueOf(tdcDigits))
                    .replace("%%NAME%%", request.getPayload().getParsed().getName())
                    .replace("%%STREET%%", request.getPayload().getParsed().getStreet())
                    .replace("%%UNIT%%", "")
                    .replace("%%CITY%%", request.getPayload().getParsed().getCity())
                    .replace("%%STATE%%", request.getPayload().getParsed().getState_code())
                    .replace("%%ZIP%%", request.getPayload().getParsed().getZip())
                    .replace("%%PRODUCT_PRICE%%", request.getPayload().getParsed().getProduct_price())
                    .replace("%%SHIPPING%%", formattedShipping)
                    .replace("%%RESOURCE_URL%%", firstUrl);

            InputStream invoiceHtmlInputStream = new ByteArrayInputStream(invoiceFormattedHtml.getBytes(StandardCharsets.UTF_8));
            CloudConvertClient cloudConvertClient = new CloudConvertClient(new StringSettingsProvider(WebhookUtils.CLOUD_CONVERT_API_KEY, "webhook-signing-secret", false));
            final Result<TaskResponse> uploadImportTaskResponseResult = cloudConvertClient.importUsing().upload(new UploadImportRequest(), invoiceHtmlInputStream);
            if (uploadImportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
                final TaskResponse uploadImportTaskResponse = uploadImportTaskResponseResult.getBody();
                cloudConvertClient.tasks().wait(uploadImportTaskResponse.getId());
                final ConvertFilesTaskRequest convertFilesTaskRequest = new ConvertFilesTaskRequest().setInput(uploadImportTaskResponse.getId()).setInputFormat("html").setOutputFormat("pdf");
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
