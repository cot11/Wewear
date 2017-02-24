package com.example.cot11.wewear;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

/**
 * Created by cot11 on 2017-02-24.
 */

public class DB_Manager {

    private ArrayList<String> result;
    private String path = "https://wewear.kr/Login.php";
    private String user_name = "";
    private String user_email = "";

    public ArrayList<String> Userinfo(String name, String email)
    {
        this.user_name = name;
        this.user_email = email;
        try
        {
            result = new InsertUserInformation().execute().get();
        } catch (InterruptedException e)
        {

        }catch (ExecutionException e)
        {

        }
        return  result;
    }
    class InsertUserInformation extends AsyncTask<Void,Void,ArrayList<String>>
    {
        @Override
        protected ArrayList<String> doInBackground(Void... voids)
        {
            try
            {

                URL url = new URL(path);
                trustAllHosts();

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });

                HttpURLConnection con = httpsURLConnection;

                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                String params = "$user_name="+user_name + "user_email=" + user_email;

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(params.getBytes());
                outputStream.flush();
                outputStream.close();

                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line = null;
                while((line =rd.readLine()) != null)
                {
                    Log.d("BufferReader",line);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(ArrayList<String> qresult)
        {
            super.onPostExecute(qresult);
        }
    }


    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType)
                    throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType)
                    throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub

            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
