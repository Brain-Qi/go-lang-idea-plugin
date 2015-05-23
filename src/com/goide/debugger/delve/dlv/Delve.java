/*
 * Copyright 2013-2015 Sergey Ignatov, Alexander Zolotov, Mihai Toader, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goide.debugger.delve.dlv;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.diagnostic.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Delve {
  private static final Logger LOG = Logger.getInstance(Delve.class);
  private final String USER_AGENT = ApplicationInfo.getInstance().getFullVersion();
  private final String BASE_URL = "http://127.0.0.1:6663";

  public String sendCommand(String command) {
    return sendCommand(command, "");
  }

  public String sendCommand(String command, String arguments) {
    String url = BASE_URL;
    String requestType = "GET";
    if ("status".equals(command)) {
      url += "/state";
    } else {
      LOG.error(String.format("Command %s not found", command));
      return "";
    }

    String response = "";
    try {
      if (requestType == "GET") {
        response = executeGet(url);
      } else if (requestType == "POST") {
        response = executePost(url, arguments);
      }
    } catch (Exception ex) {
      LOG.error(ex);
    }

    return response;
  }

  private String executeGet(String url) throws Exception {
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", USER_AGENT);
    int responseCode = con.getResponseCode();
    LOG.info("\nSending 'GET' request to URL : " + url);
    LOG.info("Response Code : " + responseCode);

    if (responseCode != 200) {
      throw new Exception("Unexpected response received");
    }

    String inputLine;
    StringBuilder response = new StringBuilder();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    try {
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    } finally {
      in.close();
    }

    return response.toString();
  }

  private String executePost(String url, String payload) throws Exception {
    URL obj = new URL(url);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("User-Agent", USER_AGENT);
    con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

    // Send post request
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());;
    try {
      wr.writeBytes(payload);
      wr.flush();
    } finally {
      wr.close();
    }

    int responseCode = con.getResponseCode();
    LOG.info("\nSending 'POST' request to URL : " + url);
    LOG.info("Post parameters : " + payload);
    LOG.info("Response Code : " + responseCode);

    if (responseCode != 200) {
      throw new Exception("Unexpected response received");
    }

    String inputLine;
    StringBuilder response = new StringBuilder();
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    try {
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    } finally {
      in.close();
    }

    return response.toString();
  }
}
