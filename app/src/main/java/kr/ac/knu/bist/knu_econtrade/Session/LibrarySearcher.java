package kr.ac.knu.bist.knu_econtrade.Session;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by stype on 2016-12-18.
 */

public class LibrarySearcher extends AsyncTask<String,Void,String>{
    HttpURLConnection conn = null;
    public final static String kudos_search = "http://pyxis.knu.ac.kr/pyxis-api/1/collections/1/search";
    String ret = "";
    @Override
    protected String doInBackground(String... strings) {
        try {
            String data = "";
            if (strings.length > 1) {
                data = URLEncoder.encode(strings[1], "UTF-8") + "=" + URLEncoder.encode(strings[2], "UTF-8");
                for (int i = 3; i < strings.length; i = i + 2)
                    data += "&" + URLEncoder.encode(strings[i], "UTF-8") + "=" + URLEncoder.encode(strings[i + 1], "UTF-8");
            }
            URL url = new URL(strings[0] + "?" + data);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                ret += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
