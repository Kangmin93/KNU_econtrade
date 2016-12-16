package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import kr.ac.knu.bist.knu_econtrade.Adapters.BBSListAdapter;
import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by Vertx on 2016-09-10.
 * 16_1010 : dykim의 MainActivity(application11) 을 옮김.
 * TODO : BUG(1) 위로 스크롤이 되어 리스트가 안보이는 경우가 있음.
 * TODO : BUG(2) 글자 겹침 현상 발생.
 */

public class Main_Library_Seat extends AppCompatActivity {

    private String url;
    private java.net.URL URL;

    private Source source;
    private ProgressDialog progressDialog;
    private BBSListAdapter BBSAdapter = null;
    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;
    private GridView grid;
    ArrayAdapter<String> seat;
    private String[][] SEAT_String = new String[13][6];
    private ArrayList<String> Seat_ArrayList;

    @Override
    protected void onStop() { //멈추었을때 다이어로그를 제거해주는 메서드
        super.onStop();
        if (progressDialog != null)
            progressDialog.dismiss(); //다이어로그가 켜져있을경우 (!null) 종료시켜준다
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_library_opening);
        grid = (GridView)findViewById(R.id.grid);
        url = "http://libseat.knu.ac.kr/domian5.asp";
        Seat_ArrayList = new ArrayList<String>();

        grid.setAdapter(seat);
        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
            Toast.makeText(Main_Library_Seat.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else { //인터넷 체크 통과시 실행할 로직
            try {
                process(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                BBSAdapter.notifyDataSetChanged();
                //이때 새로 갱신함.
            } catch (Exception e) {
                Log.d("ERROR", e + "");

            }
        }


    }
    public class gridAdapter extends BaseAdapter{

        LayoutInflater inflater;

        public gridAdapter(){

        }
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    private boolean isInternetCon() {
        cManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cManager.getActiveNetworkInfo();

        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { //와이파이 여부
                return true;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { //모바일 데이터 여부
                return true;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET)
                return true;
        } else {
            // not connected to the internet

        }

        return false;
    }

    private void process() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Library_Seat.this, "", "게시판 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    Log.e("url", URL.toString());
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "euc-kr")); //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse();
                    Log.e("msg", source.fullSequentialParse().toString());
                } catch (Exception e) {
                    Log.e("error", "error");
                    Log.d("ERROR", e + "");
                }

                final Element SEAT = (Element) source.getAllElements(HTMLElementName.TABLE).get(1);
                //tr 0, 1은 가져올 필요 없음.

                for (int i = 0; i < 13; i++) {
                    for (int j = 0; j < 6; j++) {
                        String temp = SEAT.getAllElements(HTMLElementName.TR).get(i + 1).getAllElements(HTMLElementName.TD).get(j).getContent().toString();
                        SEAT_String[i][j]=temp.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "").replaceAll("&nbsp;", "").replaceAll("\t","").replaceAll(" ", "");
                    }
                }
                for(int i=1;i<13;i++){
                    for(int j=0;j<6;j++){
                        if(j%5==0){
                            Double a = Double.parseDouble(SEAT_String[i][2]);
                            Double b = Double.parseDouble(SEAT_String[i][3]);
                            String result = ((b/a * 100)+"").substring(0,4)+"%";
                            Log.e("count",i+"/"+j);
                            SEAT_String[i][5]=result;
                        }
                    }
                }
                for(int i=0;i<13;i++){
                    for(int j=1;j<6;j++){
                        Seat_ArrayList.add(SEAT_String[i][j]);
                    }
                }

               /* Element SEAT_tbody = (Element)SEAT.getAllElements(HTMLElementName.TBODY).get(0);
                String Seat_String = SEAT_tbody.getContent().toString();
                Log.e("mmmmsg", Seat_String);*/


                Handler mHandler = new Handler(Looper.getMainLooper());
                //UI 변경 이 곳에서 할 것.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.library_seat_item, R.id.seat_textview, Seat_ArrayList);
                        grid.setAdapter(adapter);
                        // BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();

    }

}
