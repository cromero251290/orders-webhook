package com.romertec.webook.controller;


import com.romertec.webook.model.abebooks.AbebooksRequest;
import com.romertec.webook.service.AbebooksDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "romertec/webhook")
public class AbebooksController {
    @Autowired
    AbebooksDocumentService abebooksDocumentService;

    @RequestMapping(value = "/abebooks/upload", method = RequestMethod.POST)
    public ResponseEntity<Object> upload(@Valid @RequestBody AbebooksRequest request) throws Exception {

        boolean activeInvoice = true;
        boolean activeConfirmation = true;
        boolean activeProcessedEmail = true;
        boolean activeOrderEmail = true;

        abebooksDocumentService.generateInvoice(request);

//        if (request != null) {
//            String email = request.getEmail();
//            String purchaseOrderId = request.getAbebooks_purchase_order_no();
//            String orderTotal = request.getOrder_total();
//            Path pathToDirectory = Paths.get("target/report/" + email);
//            Path pathToDirectory2 = Paths.get("target/report/" + email+"/"+purchaseOrderId);
//
//            if (!Files.exists(pathToDirectory)) {
//                //creates directory in drive
////                Files.createDirectories(pathToDirectory);
//            }
//            if (!Files.exists(pathToDirectory2)) {
//                //creates directory in drive
////                Files.createDirectories(pathToDirectory2);
//            }
//
//            Path pathToFile = Paths.get("target/report/" + email + "/" + purchaseOrderId +"/"+purchaseOrderId+".txt");
//            if (!Files.exists(pathToFile)) {
////                Files.createFile(pathToFile);
//            }
//            List<String> values = new ArrayList<>();
//            values.add("orderNumber: " + purchaseOrderId);
//            values.add("orderTotal: " + orderTotal);
//            values.add("orderShipping: " + purchaseOrderItem.getOrderTotals().getShipping().getText());
//            values.add("email: " + email);
//            values.add("name: " + purchaseOrderItem.getBuyer().getMailingAddress().getName());
//            Files.write(pathToFile, values, StandardCharsets.UTF_8);
//
//            LocalDateTime now = LocalDateTime.now();
//
//            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
//            String formattedDate = now.format(dateFormatter);
//
//            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh a");
//            String formattedTime = now.format(timeFormatter);
//
//
//            String bookDescriptionContent = purchaseOrderItem.getPurchaseOrderItemList().getPurchaseOrderItem().getBook().getDescription();
//            String[] bookDescriptionLines = bookDescriptionContent.split("\\. "); // Aquí estoy asumiendo que cada línea termina con un punto y un espacio.
//
//            Document doc = Jsoup.parse(inputInvoiceTemplate, "UTF-8");
//            Document confirmationDoc = Jsoup.parse(inputConfirmationTemplate, "UTF-8");
//            Document outlookProcessedEmailDoc = Jsoup.parse(inputOutlookProcessedEmailemplate, "UTF-8");
//            Document outlookOrderEmailDoc = Jsoup.parse(inputOutlookOrderEmailemplate, "UTF-8");
//
//            doc.outputSettings().prettyPrint(false);
//            confirmationDoc.outputSettings().prettyPrint(false);
//            outlookProcessedEmailDoc.outputSettings().prettyPrint(false);
//            outlookOrderEmailDoc.outputSettings().prettyPrint(false);
//
//            Month month = Month.of(purchaseOrderItem.getOrderDate().getDate().getMonth());
//            String invoiceName = purchaseOrderItem.getBuyer().getMailingAddress().getName();
//
//            String invoiceCreateMonth = month.getDisplayName(TextStyle.FULL, Locale.US);
//            String invoiceCreateDay = String.valueOf(purchaseOrderItem.getOrderDate().getDate().getDay());
//            String invoiceCreateYear = String.valueOf(purchaseOrderItem.getOrderDate().getDate().getYear());
//
//
//            LocalDate confirmationCurrentDate = LocalDate.now();
//            LocalDate estimateDeliveryMinDate = confirmationCurrentDate.plusDays(4);
//            LocalDate estimateDeliveryMaxDate = confirmationCurrentDate.plusDays(8);
//
//            Month estimateDeliveryMinMonth = estimateDeliveryMinDate.getMonth();
//            Month estimateDeliveryMaxMonth = estimateDeliveryMaxDate.getMonth();
//
//            String confirmationStartDay = String.valueOf(estimateDeliveryMinDate.getDayOfMonth());
//            String confirmationStartMonth = estimateDeliveryMinMonth.getDisplayName(TextStyle.FULL, Locale.US);
//            String confirmationStartYear = String.valueOf(estimateDeliveryMinDate.getYear());
//
//            String confirmationFinDay = String.valueOf(estimateDeliveryMaxDate.getDayOfMonth());
//            String confirmationFinMonth = estimateDeliveryMaxMonth.getDisplayName(TextStyle.FULL, Locale.US);
//            String confirmationFinYear = String.valueOf(estimateDeliveryMaxDate.getYear());
//
//            String orderNumber = String.valueOf(purchaseOrderItem.getId());
//            LocalDateTime currentDate = LocalDateTime.now();
//            Month currentMonth = currentDate.getMonth();
//            String invoiceProcessedMonth = currentMonth.getDisplayName(TextStyle.FULL, Locale.US);
//            String invoiceProcessedDay = String.valueOf(currentDate.getDayOfMonth());
//            String invoiceProcessedYear = String.valueOf(currentDate.getYear());
//            String invoiceTotal = String.valueOf(purchaseOrderItem.getOrderTotals().getTotal().getText());
//            String invoiceBuyerStreet = purchaseOrderItem.getBuyer().getMailingAddress().getStreet();
//            String invoiceBuyerCity = purchaseOrderItem.getBuyer().getMailingAddress().getCity();
//            String invoiceBuyerState = purchaseOrderItem.getBuyer().getMailingAddress().getRegion();
//            String invoiceBuyerZip = String.valueOf(purchaseOrderItem.getBuyer().getMailingAddress().getCode());
//            String invoiceBuyerCountry = purchaseOrderItem.getBuyer().getMailingAddress().getCountry();
//            String invoiceMaxDeliveryDays = String.valueOf(purchaseOrderItem.getShipping().getMaxDeliveryDays());
//            String invoiceMinDeliveryDays = String.valueOf(purchaseOrderItem.getShipping().getMinDeliveryDays());
//            String invoiceStoreName = "Aamstar Bookshop / Hooked On Books";
//            String invoiceStoreStreet = "12 East Bijou";
//            String invoiceStoreCity = "Colorado Springs";
//            String invoiceStoreState = "CO";
//            String invoiceStoreZip = "80903";
//            String invoiceStoreCountry = "U.S.A.";
//            String invoiceStoreEmail = "hookedonbooksspringfield@gmail.com";
//            String invoiceTitle = purchaseOrderItem.getPurchaseOrderItemList().getPurchaseOrderItem().getBook().getTitle();
//            String invoiceAuthor = purchaseOrderItem.getPurchaseOrderItemList().getPurchaseOrderItem().getBook().getAuthor();
//            String invoiceISBN = purchaseOrderItem.getPurchaseOrderItemList().getPurchaseOrderItem().getBook().getIsbn();
//            String invoiceDescription = purchaseOrderItem.getPurchaseOrderItemList().getPurchaseOrderItem().getBook().getDescription().substring(0, 40);
//            String invoiceItemPrice = String.valueOf(purchaseOrderItem.getPurchaseOrderItemList().getPurchaseOrderItem().getBook().getPrice().getText());
//            String invoiceSubTotal = purchaseOrderItem.getOrderTotals().getSubtotal().getText();
//            String invoiceShipping = purchaseOrderItem.getOrderTotals().getShipping().getText();
//            String confirmationEmail = purchaseOrderItem.getBuyer().getEmail();
//            String vendorKey = String.valueOf(purchaseOrderItem.getPurchaseOrderItemList().getPurchaseOrderItem().getBook().getVendorKey());
//
//            DateTimeFormatter shortFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy");
//            DateTimeFormatter longFormat = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy h:mm a");
//
//            String processedDate = currentDate.format(shortFormat);
//            String headerProcessedDate = currentDate.format(longFormat);
//
//            String confirmationStoreOrder = String.valueOf(purchaseOrderItem.getBuyerPurchaseOrder().getId());
//            String newHtml = doc.toString()
//                    .replace("%%HDATE%%", formattedDate)
//                    .replace("%%HHOUR%%", formattedTime)
//                    .replace("%%NAME%%", invoiceName)
//                    .replace("%%MONTH%%", invoiceCreateMonth)
//                    .replace("%%DAY%%", invoiceCreateDay)
//                    .replace("%%ORDER%%", orderNumber)
//                    .replace("%%PMONTH%%", invoiceProcessedMonth)
//                    .replace("%%PDAY%%", invoiceProcessedDay)
//                    .replace("%%PYEAR%%", invoiceProcessedYear)
//                    .replace("%%STREET%%", invoiceBuyerStreet)
//                    .replace("%%CITY%%", invoiceBuyerCity)
//                    .replace("%%STATE%%", invoiceBuyerState)
//                    .replace("%%ZIP%%", invoiceBuyerZip)
//                    .replace("%%COUNTRY%%", invoiceBuyerCountry)
//                    .replace("%%MAXDELIVERYDAYS%%", invoiceMaxDeliveryDays)
//                    .replace("%%MINDELIVERYDAYS%%", invoiceMinDeliveryDays)
//                    .replace("%%SNAME%%", invoiceStoreName)
//                    .replace("%%SSTREET%%", invoiceStoreStreet)
//                    .replace("%%SCITY%%", invoiceStoreCity)
//                    .replace("%%SSTATE%%", invoiceStoreState)
//                    .replace("%%SCOUNTRY%%", invoiceStoreCountry)
//                    .replace("%%SZIP%%", invoiceStoreZip)
//                    .replace("%%TITLE%%", invoiceTitle)
//                    .replace("%%AUTHOR%%", invoiceAuthor)
//                    .replace("%%ISBN%%", invoiceISBN)
//                    .replace("%%DESCRIPTION%%", invoiceDescription)
//                    .replace("%%ITEMPRICE%%", invoiceItemPrice)
//                    .replace("%%ITEMSUBTOTAL%%", invoiceSubTotal)
//                    .replace("%%SHIPPING%%", invoiceShipping)
//                    .replace("%%TOTAL%%", invoiceTotal)
//                    .replace("%%YEAR%%", invoiceCreateYear);
//
//            String confirmationNewHtml = confirmationDoc.toString()
//                    .replace("%%SORDER%%", confirmationStoreOrder)
//                    .replace("%%STREET%%", invoiceBuyerStreet)
//                    .replace("%%CITY%%", invoiceBuyerCity)
//                    .replace("%%STATE%%", invoiceBuyerState)
//                    .replace("%%ZIP%%", invoiceBuyerZip)
//                    .replace("%%MAXDELIVERYDAYS%%", invoiceMaxDeliveryDays)
//                    .replace("%%MINDELIVERYDAYS%%", invoiceMinDeliveryDays)
//                    .replace("%%ORDER%%", orderNumber)
//                    .replace("%%TITLE%%", invoiceTitle)
//                    .replace("%%AUTHOR%%", invoiceAuthor)
//                    .replace("%%DSDAY%%", confirmationStartDay)
//                    .replace("%%DSMONTH%%", confirmationStartMonth)
//                    .replace("%%DSYEAR%%", confirmationStartYear)
//                    .replace("%%FSDAY%%", confirmationFinDay)
//                    .replace("%%FSMONTH%%", confirmationFinMonth)
//                    .replace("%%FSYEAR%%", confirmationFinYear)
//                    .replace("%%SNAME%%", invoiceStoreName)
//                    .replace("%%AUTHOR%%", invoiceAuthor)
//                    .replace("%%EMAIL%%", confirmationEmail)
//                    .replace("%%SHIPPING%%", invoiceShipping)
//                    .replace("%%TOTAL%%", invoiceTotal)
//                    .replace("%%ITEMPRICE%%", invoiceItemPrice);
//
//            String outlookProcessedEmailNewHtml = outlookProcessedEmailDoc.toString()
//                    .replace("%%HDATE%%", formattedDate)
//                    .replace("%%HHOUR%%", formattedTime)
//                    .replace("%%STREET%%", invoiceBuyerStreet)
//                    .replace("%%CITY%%", invoiceBuyerCity)
//                    .replace("%%STATE%%", invoiceBuyerState)
//                    .replace("%%ZIP%%", invoiceBuyerZip)
//                    .replace("%%NAME%%", invoiceName.toUpperCase())
//                    .replace("%%SORDER%%", confirmationStoreOrder)
//                    .replace("%%ORDER%%", orderNumber)
//                    .replace("%%DSDAY%%", confirmationStartDay)
//                    .replace("%%DSMONTH%%", confirmationStartMonth)
//                    .replace("%%DSYEAR%%", confirmationStartYear)
//                    .replace("%%FSDAY%%", confirmationFinDay)
//                    .replace("%%FSMONTH%%", confirmationFinMonth)
//                    .replace("%%FSYEAR%%", confirmationFinYear)
//                    .replace("%%TITLE%%", invoiceTitle)
//                    .replace("%%SEMAIL%%", invoiceStoreEmail)
//                    .replace("%%SNAME%%", invoiceStoreName)
//                    .replace("%%DESCRIPTION%%", invoiceDescription)
//                    .replace("%%MAXDELIVERYDAYS%%", invoiceMaxDeliveryDays)
//                    .replace("%%MINDELIVERYDAYS%%", invoiceMinDeliveryDays)
//                    .replace("%%SHIPPING%%", invoiceShipping)
//                    .replace("%%TOTAL%%", invoiceTotal)
//                    .replace("%%AUTHOR%%", invoiceAuthor)
//                    .replace("%%PDATE%%", processedDate)
//                    .replace("%%HPDATE%%", headerProcessedDate)
//                    .replace("%%EMAIL%%", confirmationEmail)
//                    .replace("%%VENDORKEY%%", vendorKey)
//                    .replace("%%ITEMPRICE%%", invoiceItemPrice);
//
//
//            String outlookOrderEmailNewHtml = outlookOrderEmailDoc.toString()
//                    .replace("%%HDATE%%", formattedDate)
//                    .replace("%%NAME%%", invoiceName.toUpperCase())
//                    .replace("%%HPDATE%%", headerProcessedDate)
//                    .replace("%%EMAIL%%", confirmationEmail)
//                    .replace("%%SORDER%%", confirmationStoreOrder)
//                    .replace("%%MONTH%%", invoiceCreateMonth)
//                    .replace("%%DAY%%", invoiceCreateDay)
//                    .replace("%%YEAR%%", invoiceCreateYear)
//                    .replace("%%HHOUR%%", formattedTime)
//                    .replace("%%SNAME%%", invoiceStoreName)
//                    .replace("%%TOTAL%%", invoiceTotal)
//                    .replace("%%STREET%%", invoiceBuyerStreet)
//                    .replace("%%CITY%%", invoiceBuyerCity)
//                    .replace("%%STATE%%", invoiceBuyerState)
//                    .replace("%%ZIP%%", invoiceBuyerZip)
//                    .replace("%%ORDER%%", orderNumber)
//                    .replace("%%FSDAY%%", confirmationFinDay)
//                    .replace("%%FSMONTH%%", confirmationFinMonth)
//                    .replace("%%FSYEAR%%", confirmationFinYear)
//                    .replace("%%MAXDELIVERYDAYS%%", invoiceMaxDeliveryDays)
//                    .replace("%%MINDELIVERYDAYS%%", invoiceMinDeliveryDays)
//                    .replace("%%TITLE%%", invoiceTitle)
//                    .replace("%%DESCRIPTION%%", invoiceDescription)
//                    .replace("%%AUTHOR%%", invoiceAuthor)
//                    .replace("%%ISBN%%", invoiceISBN)
//                    .replace("%%ITEMPRICE%%", invoiceItemPrice)
//                    .replace("%%SHIPPING%%", invoiceShipping);
//            int index = 0;
//            if (bookDescriptionLines != null) {
//                for (String item : bookDescriptionLines) {
//                    index++;
//                    String key = "%%DESCRIPTION" + index + "%%";
//                    outlookOrderEmailNewHtml = outlookOrderEmailNewHtml.replace(key, item);
//                }
//            }
//
//            if (index == 1) {
//                outlookOrderEmailNewHtml = outlookOrderEmailNewHtml
//                        .replace("%%DESCRIPTION2%%", "")
//                        .replace("%%DESCRIPTION3%%", "")
//                        .replace("%%DESCRIPTION4%%", "");
//            } else if (index == 2) {
//                outlookOrderEmailNewHtml = outlookOrderEmailNewHtml
//                        .replace("%%DESCRIPTION3%%", "")
//                        .replace("%%DESCRIPTION4%%", "");
//            } else if (index == 3) {
//                outlookOrderEmailNewHtml = outlookOrderEmailNewHtml
//                        .replace("%%DESCRIPTION4%%", "");
//            }
//
//
//            InputStream newHtmlInputStream = new ByteArrayInputStream(newHtml.getBytes(StandardCharsets.UTF_8));
//            InputStream confirmationHtmlInputStream = new ByteArrayInputStream(confirmationNewHtml.getBytes(StandardCharsets.UTF_8));
//            InputStream outlookEmailHtmlInputStream = new ByteArrayInputStream(outlookProcessedEmailNewHtml.getBytes(StandardCharsets.UTF_8));
//            InputStream outlookEmailHOrdertmlInputStream = new ByteArrayInputStream(outlookOrderEmailNewHtml.getBytes(StandardCharsets.UTF_8));
//
//            String apiKey = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiODNjNjE4MThkYTU2N2ZkNWNkNjEyM2QyNjUyN2EyMDIwNzU5OGQ5ZDg1NzBlYzI0MzhmZTJkYWMyOTg0NjBiMjgwNDJkNjk2OGRlZjhhMmYiLCJpYXQiOjE3MDMxNzg0OTQuNzE1MzgzLCJuYmYiOjE3MDMxNzg0OTQuNzE1Mzg0LCJleHAiOjQ4NTg4NTIwOTQuNzExMDI0LCJzdWIiOiI2NjU4OTI4MSIsInNjb3BlcyI6WyJ1c2VyLndyaXRlIiwidXNlci5yZWFkIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.OVT8A76Y2nK7X-hPJrHu83NE126nm5lYTWm1LXe3RkBD69BrOiJeWsee_URDt6iTjA_pRseK99roErjMw8_uPBVIEDyrPzRnbDZlpfBElM314qqzMCi_w9PHJn4hhn6wRYY8j-xQ4qRx1ms7Yn3wVRQ1-U5WSfxnp2xlurLkQ4XfbVrO5bbTnO0dOyQw9xFNe1wwH1j4CLxfMKiPKvTIU_ERH-7sEjyxgmNIAfoaZJPKx8p2eP7EYf7QrE5rHcAYxFFCZ0b78lzCY5BrnC79VMbs6pXwoTv1HrDfs6zwGXKajse0JyWWFDz_MoEgxQcNdLKYcKdxswhfcNLD8DKoFEnqdgvvID9qAurV6hkEZIMYH_jhDhB3cj0VPu4KRXWFSnp6lH8VaY14nCEW9pCXDBHLiz_kRCK3ZrWKokxgbrpIZCThI0nVvcoBrwJawmf8HuVzrgA9V67kc_PqgLihvoeTyphuAA1Wb3yPBKsMAm9A0lvCiFXBPYsqdsCgPYMHfVX0EjHIdLCJZOkXvf7lGf_TnS-uwEyNk9VgEXt84zIgqMjA0Y8muEA1pjE7WnzvFKvUWAh2m3NxsorPkMSMIUApVUfN9Ud3OH3qRNshBFsHTDojWWD2T6kOQ84liimQMSPmQ0JC52iaqk5kFh-GxgBA28nlMMTNIsW1yqR4E6E";
//            CloudConvertClient cloudConvertClient = new CloudConvertClient(new StringSettingsProvider(apiKey, "webhook-signing-secret", false));
//
//            if (activeInvoice) {
//                final Result<TaskResponse> uploadImportTaskResponseResult = cloudConvertClient.importUsing().upload(new UploadImportRequest(), newHtmlInputStream);
//                if (uploadImportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                    final TaskResponse uploadImportTaskResponse = uploadImportTaskResponseResult.getBody();
//                    cloudConvertClient.tasks().wait(uploadImportTaskResponse.getId());
//                    final ConvertFilesTaskRequest convertFilesTaskRequest = new ConvertFilesTaskRequest().setInput(uploadImportTaskResponse.getId()).setInputFormat("html").setOutputFormat("pdf");
//                    final Result<TaskResponse> convertTaskResponseResult = cloudConvertClient.tasks().convert(convertFilesTaskRequest);
//                    if (convertTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
//                        final TaskResponse convertTaskResponse = convertTaskResponseResult.getBody();
//                        final Result<TaskResponse> waitConvertTaskResponseResult = cloudConvertClient.tasks().wait(convertTaskResponse.getId());
//                        if (waitConvertTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                            final UrlExportRequest exportRequest = new UrlExportRequest().setInput(convertTaskResponse.getId());
//                            final Result<TaskResponse> exportTaskResponseResult = cloudConvertClient.exportUsing().url(exportRequest);
//                            if (exportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
//                                final TaskResponse exportTaskResponse = exportTaskResponseResult.getBody();
//                                final Result<TaskResponse> waitExportTaskResponseResult = cloudConvertClient.tasks().wait(exportTaskResponse.getId());
//                                if (waitExportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                                    TaskResponse showExportTaskResponse = waitExportTaskResponseResult.getBody();
//                                    final Result<InputStream> inputStreamResult = cloudConvertClient.files().download(showExportTaskResponse.getResult().getFiles().get(0).get("url"));
//                                    if (inputStreamResult.getStatus().getCode() == HttpStatus.SC_OK) {
//                                        Path pdfPath = Paths.get("target/report/" + purchaseOrderItem.getBuyer().getEmail() + "/" + purchaseOrderItem.getId() + "/" + AbebooksUtil.getRandomInvoiceName() + ".pdf");
//                                        Files.copy(inputStreamResult.getBody(), pdfPath);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (activeConfirmation) {
//                final Result<TaskResponse> uploadImportTaskConfirmationResponseResult = cloudConvertClient.importUsing().upload(new UploadImportRequest(), confirmationHtmlInputStream);
//                if (uploadImportTaskConfirmationResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                    final TaskResponse uploadImportTaskResponse = uploadImportTaskConfirmationResponseResult.getBody();
//                    cloudConvertClient.tasks().wait(uploadImportTaskResponse.getId());
//                    final ConvertFilesTaskRequest convertFilesTaskRequest = new ConvertFilesTaskRequest().setInput(uploadImportTaskResponse.getId()).setInputFormat("html").setOutputFormat("jpg");
//                    final Result<TaskResponse> convertTaskResponseResult = cloudConvertClient.tasks().convert(convertFilesTaskRequest);
//                    if (convertTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
//                        final TaskResponse convertTaskResponse = convertTaskResponseResult.getBody();
//                        final Result<TaskResponse> waitConvertTaskResponseResult = cloudConvertClient.tasks().wait(convertTaskResponse.getId());
//                        if (waitConvertTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                            final UrlExportRequest exportRequest = new UrlExportRequest().setInput(convertTaskResponse.getId());
//                            final Result<TaskResponse> exportTaskResponseResult = cloudConvertClient.exportUsing().url(exportRequest);
//                            if (exportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
//                                final TaskResponse exportTaskResponse = exportTaskResponseResult.getBody();
//                                final Result<TaskResponse> waitExportTaskResponseResult = cloudConvertClient.tasks().wait(exportTaskResponse.getId());
//                                if (waitExportTaskResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                                    TaskResponse showExportTaskResponse = waitExportTaskResponseResult.getBody();
//                                    final Result<InputStream> inputStreamResult = cloudConvertClient.files().download(showExportTaskResponse.getResult().getFiles().get(0).get("url"));
//                                    if (inputStreamResult.getStatus().getCode() == HttpStatus.SC_OK) {
//                                        Path pdfPath = Paths.get("target/report/" + purchaseOrderItem.getBuyer().getEmail() + "/" + purchaseOrderItem.getId() + "/" + AbebooksUtil.getRandomConfirmationName() + ".jpg");
//                                        Files.copy(inputStreamResult.getBody(), pdfPath);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (activeProcessedEmail) {
//                final Result<TaskResponse> uploadImportTaskOutlookEmailResponseResult = cloudConvertClient.importUsing().upload(new UploadImportRequest(), outlookEmailHtmlInputStream);
//                if (uploadImportTaskOutlookEmailResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                    final TaskResponse uploadImportTaskOutlookEmailResponse = uploadImportTaskOutlookEmailResponseResult.getBody();
//                    cloudConvertClient.tasks().wait(uploadImportTaskOutlookEmailResponse.getId());
//                    final ConvertFilesTaskRequest convertFilesTaskOutooLookRequest = new ConvertFilesTaskRequest().setInput(uploadImportTaskOutlookEmailResponse.getId()).setInputFormat("html").setOutputFormat("pdf");
//                    final Result<TaskResponse> convertTaskResponseOutlookEmailResult = cloudConvertClient.tasks().convert(convertFilesTaskOutooLookRequest);
//                    if (convertTaskResponseOutlookEmailResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
//                        final TaskResponse convertTaskOutlookEmailResponse = convertTaskResponseOutlookEmailResult.getBody();
//                        final Result<TaskResponse> waitConvertTaskOutlookEmailResponseResult = cloudConvertClient.tasks().wait(convertTaskOutlookEmailResponse.getId());
//                        if (waitConvertTaskOutlookEmailResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                            final UrlExportRequest exportOutlookEmailRequest = new UrlExportRequest().setInput(convertTaskOutlookEmailResponse.getId());
//                            final Result<TaskResponse> exportTaskResponseOutlookEmailResult = cloudConvertClient.exportUsing().url(exportOutlookEmailRequest);
//                            if (exportTaskResponseOutlookEmailResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
//                                final TaskResponse exportTaskOutlookEmailResponse = exportTaskResponseOutlookEmailResult.getBody();
//                                final Result<TaskResponse> waitExportTaskResponseOutlookEmailResult = cloudConvertClient.tasks().wait(exportTaskOutlookEmailResponse.getId());
//                                if (waitExportTaskResponseOutlookEmailResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                                    TaskResponse showExportTaskOutlookEmailResponse = waitExportTaskResponseOutlookEmailResult.getBody();
//                                    final Result<InputStream> inputStreamOutlookEmailResult = cloudConvertClient.files().download(showExportTaskOutlookEmailResponse.getResult().getFiles().get(0).get("url"));
//                                    if (inputStreamOutlookEmailResult.getStatus().getCode() == HttpStatus.SC_OK) {
//                                        Path pdfPath = Paths.get("target/report/" + purchaseOrderItem.getBuyer().getEmail() + "/" + purchaseOrderItem.getId() + "/" + AbebooksUtil.getRandomOutlookEmailProcessedName() + ".pdf");
//                                        Files.copy(inputStreamOutlookEmailResult.getBody(), pdfPath);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (activeOrderEmail) {
//                final Result<TaskResponse> uploadImportTaskOutlookOrderEmailResponseResult = cloudConvertClient.importUsing().upload(new UploadImportRequest(), outlookEmailHOrdertmlInputStream);
//                if (uploadImportTaskOutlookOrderEmailResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                    final TaskResponse uploadImportTaskOutlookOrderEmailResponse = uploadImportTaskOutlookOrderEmailResponseResult.getBody();
//                    cloudConvertClient.tasks().wait(uploadImportTaskOutlookOrderEmailResponse.getId());
//                    final ConvertFilesTaskRequest convertFilesTaskOutooLookOrderRequest = new ConvertFilesTaskRequest().setInput(uploadImportTaskOutlookOrderEmailResponse.getId()).setInputFormat("html").setOutputFormat("pdf");
//                    final Result<TaskResponse> convertTaskResponseOutlookEmailOrderResult = cloudConvertClient.tasks().convert(convertFilesTaskOutooLookOrderRequest);
//                    if (convertTaskResponseOutlookEmailOrderResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
//                        final TaskResponse convertTaskOutlookEmailOrderResponse = convertTaskResponseOutlookEmailOrderResult.getBody();
//                        final Result<TaskResponse> waitConvertTaskOutlookEmailOrderResponseResult = cloudConvertClient.tasks().wait(convertTaskOutlookEmailOrderResponse.getId());
//                        if (waitConvertTaskOutlookEmailOrderResponseResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                            final UrlExportRequest exportOutlookEmailOrderRequest = new UrlExportRequest().setInput(convertTaskOutlookEmailOrderResponse.getId());
//                            final Result<TaskResponse> exportTaskResponseOutlookEmailOrderResult = cloudConvertClient.exportUsing().url(exportOutlookEmailOrderRequest);
//                            if (exportTaskResponseOutlookEmailOrderResult.getStatus().getCode() == (HttpStatus.SC_CREATED)) {
//                                final TaskResponse exportTaskOutlookEmailOrderResponse = exportTaskResponseOutlookEmailOrderResult.getBody();
//                                final Result<TaskResponse> waitExportTaskResponseOutlookEmailOrderResult = cloudConvertClient.tasks().wait(exportTaskOutlookEmailOrderResponse.getId());
//                                if (waitExportTaskResponseOutlookEmailOrderResult.getStatus().getCode() == (HttpStatus.SC_OK)) {
//                                    TaskResponse showExportTaskOutlookEmailOrderResponse = waitExportTaskResponseOutlookEmailOrderResult.getBody();
//                                    final Result<InputStream> inputStreamOutlookEmailOrderResult = cloudConvertClient.files().download(showExportTaskOutlookEmailOrderResponse.getResult().getFiles().get(0).get("url"));
//                                    if (inputStreamOutlookEmailOrderResult.getStatus().getCode() == HttpStatus.SC_OK) {
//                                        Path pdfPath = Paths.get("target/report/" + purchaseOrderItem.getBuyer().getEmail() + "/" + purchaseOrderItem.getId() + "/" + AbebooksUtil.getRandomOutlookEmailThankyouName() + ".pdf");
//                                        Files.copy(inputStreamOutlookEmailOrderResult.getBody(), pdfPath);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//
//
//        }

        return ResponseEntity.ok().body("Success");
    }

}