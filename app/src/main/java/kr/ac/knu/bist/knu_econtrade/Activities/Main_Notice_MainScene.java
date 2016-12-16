package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.htmlparser.jericho.Source;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import kr.ac.knu.bist.knu_econtrade.noticeComponents.Info_ListData;
import kr.ac.knu.bist.knu_econtrade.R;
import kr.ac.knu.bist.knu_econtrade.noticeComponents.adapterNoticeinfoItem;

/**
 * Created by Vertx on 2016-09-10.
 */
public class Main_Notice_MainScene extends AppCompatActivity {

    private static String URL_PRIMARY = "http://knu.ac.kr"; //홈페이지 원본 주소이다.
    private static String GENNOTICE_MIDDLE = "/wbbs/wbbs/bbs/btin/list.action?bbs_cde=1&btin.page=";
    //URL Format = http://knu.ac.kr/wbbs/wbbs/bbs/btin/list.action?bbs_cde=1&btin.page="number" <- 이부분이 바뀜.
    // &popupDeco=false&btin.search_type=&btin.search_text=&menu_idx=67
    private static String GETNOTICE = "&popupDeco=false&btin.search_type=&btin.search_text=&menu_idx=67";
    //홈페이지 의 게시판을 나타내는 뒤 주소, 비슷한 게시판들은 거의 파싱이 가능하므로 응용하여 사용하자.

    private static String URL_ECON_MIDDLE = "https://econ.knu.ac.kr:10890/index.php?mid=board_notice";
    private static String URL_ECONRSS = "https://econ.knu.ac.kr:10890/index.php?mid=board_notice&act=rss";

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;

    private XmlPullParser xpp;

    private View emptyView;
    private Source source;
    private ProgressDialog progressDialog;

    private RecyclerView recylerViewContainer;
    private adapterNoticeinfoItem mAdapter;
    private ArrayList<Info_ListData> mListData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notice_list);

        View footer = getLayoutInflater().inflate(R.layout.listview_footer, null, false);
        setDesignComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getItemlistFromWebpage();
    }

    // CUSTOM METHODS
    private void setDesignComponents() {
        recylerViewContainer = (RecyclerView) findViewById(R.id.notice_board_list);
        mAdapter = new adapterNoticeinfoItem(getApplicationContext(), mListData);
        recylerViewContainer.setAdapter(mAdapter);
        // FOOTER 추가해줘야함

        emptyView = findViewById(R.id.emptyview);
        setRecycleViewIsEmpty();
    }

    private void setRecycleViewIsEmpty() {
        if (mListData.isEmpty()) {
            recylerViewContainer.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recylerViewContainer.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void getItemlistFromWebpage() {
        if (isInternetConnect() == true) {
            Toast.makeText(getApplicationContext(), "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            try {
                processRetrieveItems();
                recylerViewContainer.getAdapter().notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("ERROR", e + "");

            }
        }
    }

    private boolean isInternetConnect() {
        cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cManager.getActiveNetworkInfo();

        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { //와이파이 여부
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { //모바일 데이터 여부
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET)
                return true;
        } else {
            // not connected to the internet
        }
        return false;
    }

    private void processRetrieveItems() throws IOException {
        StringBuffer buffer = new StringBuffer(); // 스트링 버퍼를 준비한다.
        // 사실 listNoticeItem 객체를 생성하여 인자를 담아야 됨.

        try {
            // URL 스트림을 개방한다.
            URL urlStream = new URL(URL_ECONRSS);
            InputStream inputStream = urlStream.openStream();

            // RSS xml을 파싱할 객체도 만든다.
            XmlPullParserFactory xppFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = xppFactory.newPullParser();
            xpp.setInput(new InputStreamReader(inputStream, "UTF-8"));

            String tag;
            xpp.next();
            int eventType = xpp.getEventType();

            // xml 파서가 문서 끝을 가리킬 때까지 계속 구문을 반복한다.
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); // 태그 이름을 가져온다.

                        if (tag.equals("item"))
                            ;
                        else if (tag.equals("title")) {
                            xpp.next();
                            xpp.getText();
                        } else if (tag.equals("dc:creator")) {
                            xpp.next();
                            xpp.getText();
                        } else if (tag.equals("link")) {
                            xpp.next();
                            xpp.getText();
                        } else if (tag.equals("pubDate")) {
                            xpp.next();
                            xpp.getText();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG :
                        tag = xpp.getName();
                        // 공지 정보를 다 받았으면 객체를 만들어서 add한다.

                }
            }
        } catch (Exception e) {
            Log.d("ERROR", e + "");
        }
    }
}