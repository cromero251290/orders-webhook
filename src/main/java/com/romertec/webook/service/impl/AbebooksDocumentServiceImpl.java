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
import com.romertec.webook.model.AbebooksRequest;
import com.romertec.webook.service.AbebooksDocumentService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class AbebooksDocumentServiceImpl implements AbebooksDocumentService {


    public void generateInvoice(AbebooksRequest request) throws IOException, CloudConvertServerException, CloudConvertClientException, URISyntaxException {
        String email = WebhookUtils.extractEmailAddress(request.get_to_());
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File inputInvoiceTemplate = new File(classloader.getResource("invoice-template.html").getFile());
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(WebhookUtils.FTP_HOST, WebhookUtils.FTP_PORT);
        ftpClient.login(WebhookUtils.FTP_USERNAME, WebhookUtils.FTP_PASSWORD);
        ftpClient.enterLocalPassiveMode();
        if (ftpClient.isConnected()) {
            if (request.get_original_recipient_().equalsIgnoreCase("cromeroordersabebooks.35796@in.airparser.com")) {
                String rootDir = "cromero";
                String abebooksDir = "abebooks";
                String emailDir = email;
                String purchaseOrderDir = request.getAbebooks_purchase_order_no();
                boolean rootDirExist = ftpClient.changeWorkingDirectory(rootDir);
                if (!rootDirExist) ftpClient.makeDirectory(rootDir);
                ftpClient.changeWorkingDirectory(rootDir);

                boolean abebooksDirExist = ftpClient.changeWorkingDirectory(abebooksDir);
                if (!abebooksDirExist) ftpClient.makeDirectory(abebooksDir);
                ftpClient.changeWorkingDirectory(abebooksDir);

                boolean emailDirExist = ftpClient.changeWorkingDirectory(emailDir);
                if (!emailDirExist) ftpClient.makeDirectory(emailDir);
                ftpClient.changeWorkingDirectory(emailDir);

                boolean purchaseOrderDirExist = ftpClient.changeWorkingDirectory(purchaseOrderDir);
                if (!purchaseOrderDirExist) ftpClient.makeDirectory(purchaseOrderDir);
                ftpClient.changeWorkingDirectory(purchaseOrderDir);

                File tempFile = new File("order.txt");
                List<String> values = new ArrayList<>();
                values.add("orderNumber: " + request.getAbebooks_purchase_order_no());
                values.add("orderTotal: " + request.getOrder_total());
                values.add("orderShipping: " + request.getShipping_price());
                values.add("email: " + email);
                Files.write(Path.of(tempFile.getPath()), values, StandardCharsets.UTF_8);
                InputStream inputStream = new FileInputStream(tempFile);
                boolean done = ftpClient.storeFile("invoice.txt", inputStream);
                LocalDateTime now = LocalDateTime.now();

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
                String formattedDate = now.format(dateFormatter);

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh a");
                String formattedTime = now.format(timeFormatter);

                Document invoiceDoc = Jsoup.parse(inputInvoiceTemplate, "UTF-8");

                invoiceDoc.outputSettings().prettyPrint(false);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                LocalDate orderDate = LocalDate.parse(request.getOrder_date(), formatter);

                Month month = orderDate.getMonth();
                String invoiceName = request.getName();

                String invoiceCreateMonth = month.getDisplayName(TextStyle.FULL, Locale.US);
                String invoiceCreateDay = String.valueOf(orderDate.getDayOfMonth());
                String invoiceCreateYear = String.valueOf(orderDate.getYear());

                String orderNumber = String.valueOf(request.getAbebooks_purchase_order_no());
                LocalDateTime currentDate = LocalDateTime.now();
                Month currentMonth = currentDate.getMonth();
                String invoiceProcessedMonth = currentMonth.getDisplayName(TextStyle.FULL, Locale.US);
                String invoiceProcessedDay = String.valueOf(currentDate.getDayOfMonth());
                String invoiceProcessedYear = String.valueOf(currentDate.getYear());
                String invoiceTotal = String.valueOf(request.getOrder_total());
                String invoiceBuyerStreet = request.getStreet();
                String invoiceBuyerCity = request.getCity();
                String invoiceBuyerState = request.getState();
                String invoiceBuyerZip = request.getZip();
                String invoiceBuyerCountry = request.getCountry();
                String invoiceEstimatedDeliveryDate = request.getEstimated_delivery_date();
                String invoiceStoreName = "Aamstar Bookshop / Hooked On Books";
                String invoiceStoreStreet = "12 East Bijou";
                String invoiceStoreCity = "Colorado Springs";
                String invoiceStoreState = "CO";
                String invoiceStoreZip = "80903";
                String invoiceStoreCountry = "U.S.A.";
                String invoiceTitle = request.getTitle();
                String invoiceAuthor = request.getAuthor();
                String invoiceISBN = request.getIsbn();
                String invoiceDescription = request.getBook_description();
                String invoiceItemPrice = request.getBook_price();
                String invoiceSubTotal = request.getOrder_total();
                String invoiceShipping = request.getShipping_price();

                String invoiceFormattedHtml = invoiceDoc.toString()
                        .replace("%%HDATE%%", formattedDate)
                        .replace("%%HHOUR%%", formattedTime)
                        .replace("%%NAME%%", invoiceName)
                        .replace("%%MONTH%%", invoiceCreateMonth)
                        .replace("%%DAY%%", invoiceCreateDay)
                        .replace("%%ORDER%%", orderNumber)
                        .replace("%%PMONTH%%", invoiceProcessedMonth)
                        .replace("%%PDAY%%", invoiceProcessedDay)
                        .replace("%%PYEAR%%", invoiceProcessedYear)
                        .replace("%%STREET%%", invoiceBuyerStreet)
                        .replace("%%CITY%%", invoiceBuyerCity)
                        .replace("%%STATE%%", invoiceBuyerState)
                        .replace("%%ZIP%%", invoiceBuyerZip)
                        .replace("%%COUNTRY%%", invoiceBuyerCountry)
                        .replace("%%ESTIMATED_DELIVERY%%", invoiceEstimatedDeliveryDate)
                        .replace("%%SNAME%%", invoiceStoreName)
                        .replace("%%SSTREET%%", invoiceStoreStreet)
                        .replace("%%SCITY%%", invoiceStoreCity)
                        .replace("%%SSTATE%%", invoiceStoreState)
                        .replace("%%SCOUNTRY%%", invoiceStoreCountry)
                        .replace("%%SZIP%%", invoiceStoreZip)
                        .replace("%%TITLE%%", invoiceTitle)
                        .replace("%%AUTHOR%%", invoiceAuthor)
                        .replace("%%ISBN%%", invoiceISBN)
                        .replace("%%DESCRIPTION%%", invoiceDescription)
                        .replace("%%ITEMPRICE%%", invoiceItemPrice)
                        .replace("%%ITEMSUBTOTAL%%", invoiceSubTotal)
                        .replace("%%SHIPPING%%", invoiceShipping)
                        .replace("%%TOTAL%%", invoiceTotal)
                        .replace("%%YEAR%%", invoiceCreateYear);
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
                ftpClient.logout();
                ftpClient.disconnect();
            }

        }
    }
}
