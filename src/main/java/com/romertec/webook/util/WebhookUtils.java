package com.romertec.webook.util;

import java.util.Base64;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebhookUtils {


    public static final String FTP_HOST = "storage.bunnycdn.com"; // Cambia según sea necesario
    public static final int FTP_PORT = 21;
    public static final String FTP_USERNAME = "romertec-storage-zone"; // Cambia según sea necesario
    public static final String FTP_PASSWORD = "e5f71a92-09a2-45fb-a4f059b90cec-e7dd-4c59"; // Cambia según sea necesario

    public static final String CLOUD_CONVERT_API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiODNjNjE4MThkYTU2N2ZkNWNkNjEyM2QyNjUyN2EyMDIwNzU5OGQ5ZDg1NzBlYzI0MzhmZTJkYWMyOTg0NjBiMjgwNDJkNjk2OGRlZjhhMmYiLCJpYXQiOjE3MDMxNzg0OTQuNzE1MzgzLCJuYmYiOjE3MDMxNzg0OTQuNzE1Mzg0LCJleHAiOjQ4NTg4NTIwOTQuNzExMDI0LCJzdWIiOiI2NjU4OTI4MSIsInNjb3BlcyI6WyJ1c2VyLndyaXRlIiwidXNlci5yZWFkIiwidGFzay5yZWFkIiwidGFzay53cml0ZSIsIndlYmhvb2sucmVhZCIsIndlYmhvb2sud3JpdGUiLCJwcmVzZXQucmVhZCIsInByZXNldC53cml0ZSJdfQ.OVT8A76Y2nK7X-hPJrHu83NE126nm5lYTWm1LXe3RkBD69BrOiJeWsee_URDt6iTjA_pRseK99roErjMw8_uPBVIEDyrPzRnbDZlpfBElM314qqzMCi_w9PHJn4hhn6wRYY8j-xQ4qRx1ms7Yn3wVRQ1-U5WSfxnp2xlurLkQ4XfbVrO5bbTnO0dOyQw9xFNe1wwH1j4CLxfMKiPKvTIU_ERH-7sEjyxgmNIAfoaZJPKx8p2eP7EYf7QrE5rHcAYxFFCZ0b78lzCY5BrnC79VMbs6pXwoTv1HrDfs6zwGXKajse0JyWWFDz_MoEgxQcNdLKYcKdxswhfcNLD8DKoFEnqdgvvID9qAurV6hkEZIMYH_jhDhB3cj0VPu4KRXWFSnp6lH8VaY14nCEW9pCXDBHLiz_kRCK3ZrWKokxgbrpIZCThI0nVvcoBrwJawmf8HuVzrgA9V67kc_PqgLihvoeTyphuAA1Wb3yPBKsMAm9A0lvCiFXBPYsqdsCgPYMHfVX0EjHIdLCJZOkXvf7lGf_TnS-uwEyNk9VgEXt84zIgqMjA0Y8muEA1pjE7WnzvFKvUWAh2m3NxsorPkMSMIUApVUfN9Ud3OH3qRNshBFsHTDojWWD2T6kOQ84liimQMSPmQ0JC52iaqk5kFh-GxgBA28nlMMTNIsW1yqR4E6E";

    public static String extractEmailAddress(String to) {
        String emailPatternString = "<([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})>";
        Pattern emailPattern = Pattern.compile(emailPatternString);
        Matcher emailMatcher = emailPattern.matcher(to);
        if (emailMatcher.find()) {
            return emailMatcher.group(1);
        } else {
            return to;
        }
    }

    public static String getByteArrayFromImageURL(URL url) {
        try {
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
        }
        return null;
    }

    public static void convertInputStreamToFile(InputStream inputStream, File file) {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomInvoiceName() {
        List<String> invoices = new ArrayList<>();
        invoices.add("invoice1");
        invoices.add("quickInvoice2");
        invoices.add("urgentInvoice3");
        invoices.add("paidInvoice4");
        invoices.add("unpaidInvoice5");
        invoices.add("invoice6Today");
        invoices.add("invoice7Yesterday");
        invoices.add("invoice8Due");
        invoices.add("invoice9Late");
        invoices.add("invoice10Early");
        invoices.add("monthlyInvoice11");
        invoices.add("invoice12Discount");
        invoices.add("invoice13Full");
        invoices.add("invoice14Partial");
        invoices.add("invoice15Overdue");
        invoices.add("invoice16Reminder");
        invoices.add("invoice17First");
        invoices.add("invoice18Second");
        invoices.add("invoice19Third");
        invoices.add("invoice20Final");
        invoices.add("invoice21Copy");
        invoices.add("invoice22Original");
        invoices.add("invoice23Revised");
        invoices.add("invoice24Cancelled");
        invoices.add("invoice25Refunded");
        invoices.add("invoice26Issued");
        invoices.add("invoice27Received");
        invoices.add("invoice28Processed");
        invoices.add("invoice29Updated");
        invoices.add("invoice30Corrected");
        invoices.add("invoice31Emailed");
        invoices.add("invoice32Printed");
        invoices.add("invoice33Scanned");
        invoices.add("invoice34Fax");
        invoices.add("invoice35Digital");
        invoices.add("invoice36Paper");
        invoices.add("invoice37New");
        invoices.add("invoice38Old");
        invoices.add("invoice39Next");
        invoices.add("invoice40Previous");
        invoices.add("invoice41Fast");
        invoices.add("invoice42Slow");
        invoices.add("invoice43Manual");
        invoices.add("invoice44Automatic");
        invoices.add("invoice45Required");
        invoices.add("invoice46Optional");
        invoices.add("invoice47Complex");
        invoices.add("invoice48Simple");
        invoices.add("invoice49Modified");
        invoices.add("invoice50Standard");
        invoices.add("invoice51Custom");
        invoices.add("invoice52Regular");
        invoices.add("invoice53Irregular");
        invoices.add("invoice54Quick");
        invoices.add("invoice55Detailed");
        invoices.add("invoice56Summary");
        invoices.add("invoice57Total");
        invoices.add("invoice58Itemized");
        invoices.add("invoice59Bulk");
        invoices.add("invoice60Individual");
        invoices.add("invoice61Monthly");
        invoices.add("invoice62Yearly");
        invoices.add("invoice63OneTime");
        invoices.add("invoice64Recurring");
        invoices.add("invoice65PastDue");
        invoices.add("invoice66Current");
        invoices.add("invoice67Future");
        invoices.add("invoice68First");
        invoices.add("invoice69Last");
        invoices.add("invoice70Online");
        invoices.add("invoice71Offline");
        invoices.add("invoice72Electronic");
        invoices.add("invoice73Paperless");
        invoices.add("invoice74AutoPay");
        invoices.add("invoice75ManualPay");
        invoices.add("invoice76PartialPay");
        invoices.add("invoice77Overpay");
        invoices.add("invoice78Underpay");
        invoices.add("invoice79CorrectPay");
        invoices.add("invoice80LatePay");
        invoices.add("invoice81OnTimePay");
        invoices.add("invoice82ShortPay");
        invoices.add("invoice83OverdueNotice");
        invoices.add("invoice84ReminderNotice");
        invoices.add("invoice85FinalNotice");
        invoices.add("invoice86FirstNotice");
        invoices.add("invoice87CreditNote");
        invoices.add("invoice88DebitNote");
        invoices.add("invoice89BalanceNote");
        invoices.add("invoice90AdjustmentNote");
        invoices.add("invoice91Proforma");
        invoices.add("invoice92Commercial");
        invoices.add("invoice93Export");
        invoices.add("invoice94Import");
        invoices.add("invoice95Domestic");
        invoices.add("invoice96International");
        invoices.add("invoice97Master");
        invoices.add("invoice98Consolidated");
        invoices.add("invoice99Split");
        Random random = new Random();
        int randomIndex = random.nextInt(invoices.size()); // Genera un índice aleatorio
        String randomElement = invoices.get(randomIndex); // Obtiene el elemento con ese índice
        return randomElement;
    }

}
