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
import com.romertec.webook.controller.WebhookController;
import com.romertec.webook.entities.MenuEntity;
import com.romertec.webook.entities.RestaurantsEntity;
import com.romertec.webook.model.uber.UberRequest;
import com.romertec.webook.repository.MenuRepository;
import com.romertec.webook.repository.RestaurantsRepository;
import com.romertec.webook.service.UberDocumentService;
import com.romertec.webook.util.WebhookUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UberDocumentServiceImpl implements UberDocumentService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RestaurantsRepository restaurantRepository;

    @Override
    public void generateInvoice(UberRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException {
        Optional<RestaurantsEntity> restaurantOptional = restaurantRepository.findRandomRestaurant();
        RestaurantsEntity restaurant = restaurantOptional.get();
        Optional<List<MenuEntity>> optionalMenuRepositoryList = menuRepository.findAllMenusByRestaurantId(restaurant.getId());
        MenuEntity menu = optionalMenuRepositoryList.get().get(0);


        InputStream invoiceInputStream = WebhookController.class.getClassLoader().getResourceAsStream("uber-invoice-template.html");
        File inputInvoiceTemplate = new File("uber-invoice-template");
        WebhookUtils.convertInputStreamToFile(invoiceInputStream, inputInvoiceTemplate);
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(WebhookUtils.FTP_HOST, WebhookUtils.FTP_PORT);
        ftpClient.login(WebhookUtils.FTP_USERNAME, WebhookUtils.FTP_PASSWORD);
        ftpClient.enterLocalPassiveMode();
        if (ftpClient.isConnected()) {
            String rootDir = "cromero";
            String targetDir = "uber";
            String emailDir = request.getEmail();
            Random rand = new Random();
            int orderNumber = 150000000 + rand.nextInt(90000000);
            String purchaseOrderDir = String.valueOf(orderNumber);
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

            Pattern usdPattern = Pattern.compile("\\s*USD\\s*");
            Matcher matcher = usdPattern.matcher(menu.getPrice());
            String itemPriceString = null;
            if (matcher.find()) {
                itemPriceString = matcher.replaceAll("");
            } else {
                itemPriceString = menu.getPrice();
            }

            String itemName = menu.getName();

            File tempFile = new File("order.txt");
            List<String> values = new ArrayList<>();

            Random shippingRandom = new Random();
            double randomDouble = 4.0 + (7.0 - 4.0) * shippingRandom.nextDouble();
            double shipping = randomDouble;
            double itemPrice = Double.valueOf(itemPriceString);
            double subtotal = itemPrice;
            double tax = (0.07) * (shipping + itemPrice);


            Random randomServiceFee = new Random();
            double serviceFee = randomServiceFee.nextDouble();
            serviceFee = 3.0 + serviceFee * (4.5 - 3.0);

            Random randomTipFee = new Random();
            int tip = randomTipFee.nextInt(5) + 1;


            double total = itemPrice + tax + shipping + tip + serviceFee;

            String name = request.getName();
            String storeName = restaurant.getName();
            String restaurantAddress = restaurant.getStreet() + ", " + restaurant.getCity() + ", " + restaurant.getState() + " " + restaurant.getZip();
            String address = request.getStreet() + ", " + request.getCity() + ", " + request.getStateCode() + " " + request.getZip();


            SimpleDateFormat formatterFreeshipping = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            String dateForFreeshipping = formatterFreeshipping.format(date);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter bodyDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a");
            DateTimeFormatter headerDateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

            String formattedBody = now.format(bodyDateFormatter);
            String formattedHeader = now.format(headerDateFormatter);

            // Imprime la fecha y hora formateadas

            DecimalFormat df = new DecimalFormat("#.00");

            String formattedShipping = df.format(shipping);
            String formattedTax = df.format(tax);
            String formattedTotal = df.format(total);
            String formattedSubtotal = df.format(subtotal);
            String formattedTip = df.format(tip);
            String formattedServiceFee = df.format(serviceFee);

            values.add("Retailer: Uber Eats");
            values.add("Order Date: " + dateForFreeshipping);
            values.add("Service Fee: " + formattedServiceFee);
            values.add("Delivery Fee: " + formattedShipping);
            values.add("Order Total: " + formattedTotal);
            values.add("Email: " + request.getEmail());

            Files.write(Path.of(tempFile.getPath()), values, StandardCharsets.UTF_8);
            InputStream inputStream = new FileInputStream(tempFile);
            boolean done = ftpClient.storeFile("invoice.txt", inputStream);

            Document invoiceDoc = Jsoup.parse(inputInvoiceTemplate, "UTF-8");

            invoiceDoc.outputSettings().prettyPrint(false);

            String invoiceFormattedHtml = invoiceDoc.toString()
                    .replace("%%BODY_DATE%%", formattedBody)
                    .replace("%%HEADER_DATE%%", formattedHeader)
                    .replace("%%NAME%%", name)
                    .replace("%%STORE_NAME%%", storeName)
                    .replace("%%TOTAL%%", formattedTotal)
                    .replace("%%ITEM_PRICE%%", itemPriceString)
                    .replace("%%ITEM_NAME%%", itemName)
                    .replace("%%SUBTOTAL%%", formattedSubtotal)
                    .replace("%%SERVICE%%", formattedServiceFee)
                    .replace("%%TAX%%", formattedTax)
                    .replace("%%TIP%%", formattedTip)
                    .replace("%%SHIPPING%%", formattedShipping)
                    .replace("%%SUBTOTAL%%", formattedSubtotal)
                    .replace("%%TAX%%", formattedTax)
                    .replace("%%STORE_ADDRESS%%", restaurantAddress)
                    .replace("%%ADDRESS%%", address);

            InputStream invoiceHtmlInputStream = new ByteArrayInputStream(invoiceFormattedHtml.getBytes(StandardCharsets.UTF_8));
            CloudConvertClient cloudConvertClient = new CloudConvertClient(new StringSettingsProvider(WebhookUtils.CLOUD_CONVERT_API_KEY, "webhook-signing-secret", false));
            final Result<TaskResponse> uploadImportTaskResponseResult = cloudConvertClient.importUsing().upload(new UploadImportRequest(), invoiceHtmlInputStream);
            if (uploadImportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
                final TaskResponse uploadImportTaskResponse = uploadImportTaskResponseResult.getBody();
                cloudConvertClient.tasks().wait(uploadImportTaskResponse.getId());
                final ConvertFilesTaskRequest convertFilesTaskRequest = new ConvertFilesTaskRequest().setInput(uploadImportTaskResponse.getId()).setInputFormat("html").setOutputFormat("pdf").setProperty("pages", "1");
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
