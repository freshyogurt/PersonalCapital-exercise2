package com.personalcapital;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 * This class parses http request with single query string to a request understood by AWS Elasticsearch Service.
 */
public class ProxyWithStream implements RequestStreamHandler {
    JSONParser parser = new JSONParser();

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of ProxyWithStream");


        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();
        String responseCode = "200";
        String esUrl = "https://search-personalcapital-gfo5mzdnneaonjafu5ml54k5by.us-east-1.es.amazonaws.com/plans/_search?q=";

        try {
            JSONObject event = (JSONObject)parser.parse(reader);
            if (event.get("queryStringParameters") != null) {
                JSONObject qps = (JSONObject)event.get("queryStringParameters");
                if (qps.get("planName") != null) {
                    String planName = (String)qps.get("planName");
                    esUrl += "PLAN_NAME:\"" + URLEncoder.encode(planName, "UTF-8")+"\"";
                }
                else if (qps.get("sponsorName") != null) {
                    String sponsorName = (String)qps.get("sponsorName");
                    esUrl += "SPONSOR_DFE_NAME:\"" + URLEncoder.encode(sponsorName, "UTF-8")+"\"";
                }
                else if (qps.get("sponsorMailState") != null) {
                    String sponsorMailState = (String)qps.get("sponsorMailState");
                    esUrl += "SPONS_DFE_MAIL_US_STATE:\"" + URLEncoder.encode(sponsorMailState, "UTF-8")+"\"";
                }
                else if (qps.get("sponsorLocState") != null) {
                    String sponsorLocState = (String)qps.get("sponsorLocState");
                    esUrl += "SPONS_DFE_LOC_US_STATE:\"" + URLEncoder.encode(sponsorLocState, "UTF-8")+"\"";
                }
            }

            StringBuffer response = new StringBuffer();
            URL tmp = new URL(esUrl);
            HttpURLConnection con = (HttpURLConnection) tmp.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            JSONParser parser = new JSONParser();
            JSONObject responseBody = (JSONObject) parser.parse(response.toString());

            responseJson.put("statusCode", responseCode);
            responseJson.put("body", responseBody.toString());

        } catch(ParseException pex) {
            responseJson.put("statusCode", "400");
            responseJson.put("exception", pex);
        }

        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toJSONString());
        writer.close();
    }
}