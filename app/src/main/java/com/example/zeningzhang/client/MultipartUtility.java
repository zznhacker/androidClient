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
//                writer.append("--" + boundary).append(LINE_FEED);
//                writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
//                        .append(LINE_FEED);
//                writer.append("Content-Type: text/plain; charset=" + charset).append(
//                                LINE_FEED);
//                writer.append(LINE_FEED);
//                writer.append(value).append(LINE_FEED);
//                writer.flush();
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
                    Log.d("zznmizzou",byteArrayImage.toString());
                    String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                    jsonObject.put("file",encodedImage);

//                    writer.append("file"+':'+'"');
//                    writer.flush();
//

//                writer.append("--" + boundary).append(LINE_FEED);
//                writer.append("Content-Disposition: form-data; name=\"" + fieldName
//                                        + "\"; filename=\"" + fileName + "\"")
//                        .append(LINE_FEED);
//                writer.append("Content-Type: "
//                                        + URLConnection.guessContentTypeFromName(fileName))
//                        .append(LINE_FEED);
//                writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
//                writer.append(LINE_FEED);
//                writer.flush();
         
//                FileInputStream inputStream = new FileInputStream(uploadFile);
//                byte[] buffer = new byte[8194];
//                int bytesRead = -1;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                        outputStream.write(buffer, 0, bytesRead);
//                    }
//                outputStream.flush();
//                inputStream.close();
//                 writer.append('"'+",");
//                writer.append(LINE_FEED);
//                writer.flush();
            }
     
                /**
          * Adds a header field to the request.
          * @param name - name of the header field
          * @param value - value of the header field
          */
                public void addHeaderField(String name, String value) {
//                writer.append(name + ": " + value).append(LINE_FEED);
                writer.flush();
            }
         
                /**
          * Completes the request and receives response from the server.
          * @return a list of Strings as response in case the server returned
          * status OK, otherwise an exception is thrown.
          * @throws IOException
          */
                public List<String> finish() throws IOException {
                List<String> response = new ArrayList<String>();
                    writer.write(jsonObject.toString());
                    writer.flush();
                    Log.d("zznmizzou",jsonObject.toString());
//
//                writer.append(LINE_FEED).flush();
//                writer.append("--" + boundary + "--").append(LINE_FEED);
//                writer.close();
         
                // checks server's status code first
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