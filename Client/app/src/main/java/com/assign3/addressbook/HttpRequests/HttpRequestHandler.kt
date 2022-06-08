package com.assign3.addressbook.HttpRequests

import java.net.HttpURLConnection
import java.net.URL

class HttpRequestHandler {

    fun getURLData(requestUrl: String) : String{
        var url: URL;
        var response: String = "";
        try{
            url = URL(requestUrl)
            var connection: HttpURLConnection = url.openConnection() as HttpURLConnection;
            connection.requestMethod = "GET";

            var responseCode: Int = connection.responseCode;

            if(responseCode == HttpURLConnection.HTTP_OK){
                var line: String;
                var inputStream = connection.inputStream;
                var inputStreamReader = inputStream.bufferedReader();
                response = inputStreamReader.readText();

            }else{
                response = "Response code: " + responseCode;
            }

        } catch (e: Exception){
            throw e
        }
        return response;
    }
}
