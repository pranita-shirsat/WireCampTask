package com.example.prasad.wirecamptask.Network;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




public class GetAsyncTask extends AsyncTask<Void, Void, String> {

  private String mURLString;
  private StringBuilder response;
  private Activity mActivity;
  private getResponse mGetResponse;

  public GetAsyncTask(Activity mActivity, String mURLString, getResponse getResponse) {
    this.mURLString = mURLString;
    this.mActivity = mActivity;
    this.mGetResponse = getResponse;
  }

  @Override
  protected String doInBackground(Void... params) {
    try {
      URL url = new URL(mURLString);
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
      try {
        if (urlConnection.getResponseCode() == 200) {
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
          StringBuilder stringBuilder = new StringBuilder();
          String line;
          while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
          }
          response= stringBuilder;
          bufferedReader.close();
          return "success";
        } else {
          return "error";
        }
      } finally {
        urlConnection.disconnect();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "error";
  }

  @Override
  protected void onPostExecute(String s) {
    super.onPostExecute(s);
    if (mGetResponse != null) {
      mGetResponse.response(s,response);
    }
  }

  public interface getResponse {
    void response(String s, StringBuilder response);
  }

}
