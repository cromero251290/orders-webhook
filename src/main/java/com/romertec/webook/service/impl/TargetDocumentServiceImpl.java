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
import com.romertec.webook.model.target.TargetRequest;
import com.romertec.webook.service.TargetDocumentService;
import com.romertec.webook.util.WebhookUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TargetDocumentServiceImpl implements TargetDocumentService {
    @Override
    public void generateInvoice(TargetRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException {
        List<String> urlResourceList = request.getPayload().getParsed().getResource_url();
        Random random = new Random();
        int eightDigitNumber = 10000000 + random.nextInt(90000000);
        String tin = String.valueOf(eightDigitNumber);
        for (String url : urlResourceList) {
            URL urlTest = new URL(url);
            HttpURLConnection conexion = (HttpURLConnection) urlTest.openConnection();
            conexion.setInstanceFollowRedirects(true);
            conexion.connect();
            InputStream is = conexion.getInputStream();
            String urlFinal = conexion.getURL().toString();
            conexion.disconnect();
            Pattern pattern = Pattern.compile("A-(\\d{8})");
            Matcher matcher = pattern.matcher(urlFinal);
            if (matcher.find()) {
                tin = matcher.group(1);
                break;
            }
        }


        InputStream invoiceInputStream = AbebooksController.class.getClassLoader().getResourceAsStream("target-order-invoice.html");
        File inputInvoiceTemplate = new File("target-order-invoice");
        WebhookUtils.convertInputStreamToFile(invoiceInputStream, inputInvoiceTemplate);
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(WebhookUtils.FTP_HOST, WebhookUtils.FTP_PORT);
        ftpClient.login(WebhookUtils.FTP_USERNAME, WebhookUtils.FTP_PASSWORD);
        ftpClient.enterLocalPassiveMode();
        String itemImageUrl = request.getPayload().getParsed().getResource_url().get(0);
        URL url = new URL(itemImageUrl);
        String base64 = WebhookUtils.getByteArrayFromImageURL(url);
        if (ftpClient.isConnected()) {
            String[] receiptArr = request.getPayload().getParsed().get_original_recipient_().split("\\.");
            String rootDir = receiptArr[0];
            String emailDir = request.getPayload().getParsed().getEmail();
            String purchaseOrderDir = request.getPayload().getParsed().getOrder_number();
            boolean rootDirExist = ftpClient.changeWorkingDirectory(rootDir);
            if (!rootDirExist) ftpClient.makeDirectory(rootDir);
            ftpClient.changeWorkingDirectory(rootDir);

            boolean purchaseOrderDirExist = ftpClient.changeWorkingDirectory(purchaseOrderDir);
            if (!purchaseOrderDirExist) ftpClient.makeDirectory(purchaseOrderDir);
            ftpClient.changeWorkingDirectory(purchaseOrderDir);

            File tempFile = new File("order.txt");
            List<String> values = new ArrayList<>();
            String itemPriceFormatted = request.getPayload().getParsed().getItem_price().replaceAll("[^\\d.]", "");

            double subtotal = Double.valueOf(itemPriceFormatted);

            Random shippingRandom = new Random();
            double randomDouble = shippingRandom.nextDouble();
            double scaledDouble = 10.00 + (randomDouble * (15.00 - 10.00));
            double shipping = Math.round(scaledDouble * 100.0) / 100.0;
            double itemPrice = Double.valueOf(itemPriceFormatted);
            double tax = (0.07) * (shipping + itemPrice);
            double total = tax + itemPrice + shipping;
            int tdcDigits = ThreadLocalRandom.current().nextInt(1000, 10000); // 1000 a 9999


            DecimalFormat df = new DecimalFormat("#.00");

            String formattedShipping = df.format(shipping);
            String formattedTax = df.format(tax);
            String formattedTotal = df.format(total);


            values.add("orderNumber: " + request.getPayload().getParsed().getOrder_number());
            values.add("orderTotal: " + formattedTotal);
            values.add("orderShipping: " + shipping);
            values.add("email: " + request.getPayload().getParsed().getEmail());
            Files.write(Path.of(tempFile.getPath()), values, StandardCharsets.UTF_8);
            InputStream inputStream = new FileInputStream(tempFile);
            boolean done = ftpClient.storeFile("invoice.txt", inputStream);

            Document invoiceDoc = Jsoup.parse(inputInvoiceTemplate, "UTF-8");

            invoiceDoc.outputSettings().prettyPrint(false);


            String invoiceFormattedHtml = invoiceDoc.toString()
                    .replace("%%NAME%%", request.getPayload().getParsed().getDelivers_to_name())
                    .replace("%%STREET%%", request.getPayload().getParsed().getStreet())
                    .replace("%%CITY%%", request.getPayload().getParsed().getCity())
                    .replace("%%STATE%%", request.getPayload().getParsed().getState())
                    .replace("%%ZIP%%", request.getPayload().getParsed().getZip())
                    .replace("%%ORDER%%", request.getPayload().getParsed().getOrder_number())
                    .replace("%%ORDER_DATE%%", request.getPayload().getParsed().getOrder_date())
                    .replace("%%TIN%%", tin)
                    .replace("%%ITEM_DESCRIPTION%%", request.getPayload().getParsed().getItem_description())
                    .replace("%%ITEM_PRICE%%", itemPriceFormatted)
                    .replace("%%SUBTOTAL%%", String.valueOf(subtotal))
                    .replace("%%SHIPPING%%", formattedShipping)
                    .replace("%%SHIPPING_TAX%%", "0.00")
                    .replace("%%TAX%%", formattedTax)
                    .replace("%%TOTAL%%", formattedTotal)
                    .replace("%%TDC_DIGITS%%", String.valueOf(tdcDigits));


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
