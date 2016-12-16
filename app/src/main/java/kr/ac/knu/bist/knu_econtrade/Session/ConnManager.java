package kr.ac.knu.bist.knu_econtrade.Session;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by stype on 2016-12-16.
 */

public class ConnManager extends AsyncTask<String, Void, String> {
    public final static String main_url = "http://yes.knu.ac.kr/";
    public final static String login_url = "comm/comm/support/login/login.action";
    public final static String record_url = "cour/scor/certRec/certRecEnq/listCertRecEnqs.action";
    private static HttpURLConnection conn;
    private static boolean m_session = false;
    private static String m_cookies = "";

    @Override
    protected String doInBackground(String... args) {
        URL url = null;
        String ret = "";
        BufferedReader in = null;
        try {
            url = new URL(args[0]);
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if(m_session) {
                conn.setRequestProperty("Cookie", m_cookies);
            }

            OutputStream ostream = conn.getOutputStream();
            ostream.write(makeParams(args).getBytes());
            ostream.flush();
            ostream.close();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String current;

            while((current = buffer.readLine()) != null)
                ret+=current;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        saveCookie(conn);
    }

    public static void saveCookie(HttpURLConnection conn) {
        String cookieTemp = conn.getHeaderField("Set-Cookie");
        if(m_session == false && cookieTemp != null) {
            m_cookies = cookieTemp;
            m_session = true;
        }
    }

    public String makeParams(String... args) {
        String params = "";
        if(args.length>1)
            params += args[1] + "=" + args[2];
        for(int i = 3; i<args.length; i = i+2) {
            params += "&"+args[i]+"="+args[i+1];
        }
        return params;
    }

}