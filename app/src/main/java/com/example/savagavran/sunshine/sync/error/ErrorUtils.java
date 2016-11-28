package com.example.savagavran.sunshine.sync.error;

import com.example.savagavran.sunshine.sync.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter = ApiClient.getClient()
                .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error = new APIError();

        try {
            String json = new Gson().toJson(response);
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);
            JsonObject networkResponseFull = jsonObject.getAsJsonObject("rawResponse").getAsJsonObject("networkResponse");

            error.setCode(Integer.parseInt(networkResponseFull.get("code").toString()));
            error.setMessage(networkResponseFull.get("message").toString());
        } catch (Exception e) {
            return new APIError();
        }

        return error;
    }
}
