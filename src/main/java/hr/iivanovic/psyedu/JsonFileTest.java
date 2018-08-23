package hr.iivanovic.psyedu;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import hr.iivanovic.psyedu.db.Subject;

/**
 * @author iivanovic
 * @date 29.08.16.
 */
public class JsonFileTest {
    public static void main(String[] args) {
        writeToFile();
        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader("/home/iivanovic/private/psy-edu/src/main/resources/public/subjects.json"));

            JSONArray jsonObject = (JSONArray) obj;

            Subject subject = new Subject();
            System.out.println(jsonObject.get(0));

//            long age = (Long) jsonObject.get("age");
//            System.out.println(age);

            // loop array
//            JSONArray msg = (JSONArray) jsonObject.get("messages");
//            Iterator<String> iterator = msg.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile() {
        JSONObject obj = new JSONObject();
        obj.put("name", "mkyong.com");
        obj.put("age", new Integer(100));

        JSONArray list = new JSONArray();
        list.add("msg 1");
        list.add("msg 2");
        list.add("msg 3");

        obj.put("messages", list);

        try {

            FileWriter file = new FileWriter("/home/iivanovic/private/psy-edu/src/main/resources/public/subjects_.json");
            file.write(obj.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(obj);
    }
}
