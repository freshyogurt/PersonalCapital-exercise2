package com.personalcapital;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MyRequestHandler implements RequestHandler<Request, JSONObject> {

    public JSONObject handleRequest(Request request, Context context) {
        JSONObject json = new JSONObject();
        json.put("planName", request.getPlanName());
        return json;
    }
}
