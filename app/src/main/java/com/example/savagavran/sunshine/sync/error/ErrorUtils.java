package com.example.savagavran.sunshine.sync.error;

import okhttp3.ResponseBody;

public class ErrorUtils {

    public static APIError parseError(ResponseBody response) {

        APIError error = new APIError();

        try {
            String message = response.source().toString();
            String code = message.substring(message.indexOf("\"cod\":")+7, message.indexOf("\"cod\":")+10);
            int icode = Integer.parseInt(code);
            error.setCode(icode);
            String reason = message.substring(message.indexOf("message\":")+10, message.length()-3);
            error.setMessage(reason);
        } catch (Exception e) {
            return new APIError();
        }

        return error;
    }
}
