package com.assign3.addressbook.HttpRequests

import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

class HttpRequestHandler {

    fun getURLData(requestUrl: String) : String{
        var url: URL;
        var response = "";
        try{
            url = URL(requestUrl)
            var connection: HttpURLConnection = url.openConnection() as HttpURLConnection;
            connection.requestMethod = "GET";

            var responseCode: Int = connection.responseCode;

            if(responseCode == HttpURLConnection.HTTP_OK){
                var inputStream = connection.inputStream
                var inputStreamReader = inputStream.bufferedReader();
                response = inputStreamReader.readText()
            }else{
                response = "Response code: " + responseCode;
            }

        } catch (e: Exception){
            throw e
        }
        return response;
    }

    fun getURLImage(requestUrl: String) : Bitmap? {
        var url: URL;
        try{
            url = URL(requestUrl)
            var connection: HttpURLConnection = url.openConnection() as HttpURLConnection;
            connection.requestMethod = "GET";

            var responseCode: Int = connection.responseCode;

            if(responseCode == HttpURLConnection.HTTP_OK){
                var response = connection.content as Bitmap;

                return response;
            }else{
                Log.d("response: ",responseCode.toString());
            }

        } catch (e: Exception){
            throw e
        }
        return null;
    }
}