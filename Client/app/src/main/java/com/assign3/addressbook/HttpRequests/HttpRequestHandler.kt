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
            connection.setRequestProperty("Content-Type", "application/json");

            var responseCode: Int = connection.responseCode;

            if(responseCode == HttpURLConnection.HTTP_OK){
                var line: String;
                var inputStream = connection.inputStream
                var inputStreamReader = inputStream.bufferedReader();
                response = inputStreamReader.readText()

                while(inputStreamReader.readLine().also { line = it } != null){
                    response += line
                }
            }else{
                response = "Response code: " + responseCode;
            }

        } catch (e: Exception){
            throw e
        }
        return response;
    }
}
