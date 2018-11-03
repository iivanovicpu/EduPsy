package chat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTesting {
    public static void main(String[] args) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
        String formatDateTime = currentDateTime.format(format1);
        System.out.println(formatDateTime);

        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime2 = currentDateTime.format(format2);
        System.out.println(formatDateTime2);

        DateTimeFormatter format3 = DateTimeFormatter.ISO_DATE_TIME;
//        DateTimeFormatter format3 = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss");
        String formatDateTime3 = currentDateTime.format(format3);
        String format = SimpleDateFormat.getInstance().format(currentDateTime.toLocalDate());
        System.out.println(formatDateTime3);
        System.out.println(format);
    }
}
