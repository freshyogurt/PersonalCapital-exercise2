package com.personalcapital;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class csv2json {

    // maximum number of json objects in each file
    // this value guarantees file size less than 10MB
    private static int MAX_COUNT = 2400;

    public static void convert() {
        // clean up old result folder and create new one
        File dir = new File("json");
        deleteDir(dir);
        dir.mkdirs();

        JsonFactory fac = new JsonFactory();
        try (BufferedReader in = new BufferedReader(new FileReader("f_5500_2016_latest.csv"))) {
            String[] headers = in.readLine().split(",");
            int startID = 1 - MAX_COUNT, nextID = 1, seq = 1;
            while (nextID == startID + MAX_COUNT) {
                startID = nextID;
                nextID = generate(in, headers, startID, seq++);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int generate(BufferedReader in, String[] headers, int startID, int seq) {
        JsonFactory fac = new JsonFactory();

        int id = startID;
        try(JsonGenerator gen = fac.createGenerator(new File("json","data" + seq + ".json"), JsonEncoding.UTF8)
                .setPrettyPrinter(new MinimalPrettyPrinter(""))) {
            //gen.writeStartArray();
            String line;
            while ((line = in.readLine()) != null && (id - startID) < MAX_COUNT) {
                gen.writeStartObject();
                gen.writeObjectFieldStart("index");
                gen.writeObjectField("_index", "plans");
                gen.writeObjectField("_type", "plan");
                gen.writeObjectField("_id", String.valueOf(id++));
                gen.writeEndObject();
                gen.writeEndObject();
                gen.writeRaw('\n');

                gen.writeStartObject();
                String[] values = line.split(",");
                for (int i = 0 ; i < headers.length ; i++) {
                    String value = i < values.length ? values[i] : null;
                    gen.writeObjectField(headers[i], value);
                }
                gen.writeEndObject();
                gen.writeRaw('\n');
            }
            //gen.writeEndArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static void main(String[] args) {
        csv2json.convert();
    }
}
