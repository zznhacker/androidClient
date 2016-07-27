package com.example.zeningzhang.client;

/**
 * Created by ZeningZhang on 7/25/16.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
  * This utility class provides an abstraction layer for sending multipart HTTP
  * POST requests to a web server.
  * @author www.codejava.net
  *
  */
public class MultipartUtility {
//        private final String boundary;
//        private static final String LINE_FEED = "\r\n";
        private HttpURLConnection httpConn;
        private String charset;
        private OutputStream outputStream;
        private PrintWriter writer;
        private JSONObject jsonObject;
     
                /**
          * This constructor initializes a new HTTP POST request with content type
          * is set to multipart/form-data
          * @param requestURL
          * @param charset
          * @throws IOException
          */
                public MultipartUtility(String requestURL, String charset)
                throws IOException {
                    this.charset = charset;
                    jsonObject = new JSONObject();

                    // creates a unique boundary based on time stamp
//                    boundary = "===" + System.currentTimeMillis() + "===";

                    URL url = new URL(requestURL);
                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setUseCaches(false);
                    httpConn.setRequestMethod("POST");
                    httpConn.setDoOutput(true); // indicates POST method
                    httpConn.setDoInput(true);

                    httpConn.setRequestProperty("Connection", "Keep-Alive");
                    httpConn.setRequestProperty("Cache-Control", "no-cache");


                httpConn.setRequestProperty("Content-Type",
                                "application/json");
//                httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
//                httpConn.setRequestProperty("Test", "Bonjour");
                outputStream = httpConn.getOutputStream();
                writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                                true);
            }
     
                /**
          * Adds a form field to the request
          * @param name field name
          * @param value field value
          */
                public void addFormField(String name, String value) throws JSONException {
                  jsonObject.put(name,value);
            }
     
                /**
          * Adds a upload file section to the request
          * @param fieldName name attribute in <input type="file" name="..." />
          * @param uploadFile a File to be uploaded
          * @throws IOException
          */
                public void addFilePart(String fieldName, File uploadFile)
                        throws IOException, JSONException {
                    String fileName = uploadFile.getName();
                    File imgPath = new File(fileName);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    Log.d("zznmizzou",fieldName+" and "+fileName);
                    Bitmap image = BitmapFactory.decodeFile(fieldName);

                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] byteArrayImage = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                    try {
                        jsonObject.put("file",encodedImage).wait(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

         
                /**
          * Completes the request and receives response from the server.
          * @return a list of Strings as response in case the server returned
          * status OK, otherwise an exception is thrown.
          * @throws IOException
          */
                public List<String> finish() throws IOException, JSONException {
                List<String> response = new ArrayList<String>();
                Log.d("zznmizzouJSON",jsonObject.get("file").toString());

                writer.write(jsonObject.toString());
                writer.flush();
                int status = httpConn.getResponseCode();
                if (status == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                        httpConn.getInputStream()));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                                response.add(line);
                            }
                        reader.close();
                        httpConn.disconnect();
                    } else {
                        throw new IOException("Server returned non-OK status: " + status);
                    }
         
                return response;
            }
}