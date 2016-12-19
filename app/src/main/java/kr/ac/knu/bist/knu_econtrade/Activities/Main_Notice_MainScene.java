package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.ac.knu.bist.knu_econtrade.noticeComponents.Info_ListData;
import kr.ac.knu.bist.knu_econtrade.R;
import kr.ac.knu.bist.knu_econtrade.noticeComponents.adapterNoticeinfoItem;

import static java.security.AccessController.getContext;

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

    // asdfasdf
    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;
    private String url;
    private java.net.URL URL;

    private int BBSlocate;
    private int Board_num= 1;

    private XmlPullParser xpp;
    private Source source;
    private ProgressDialog progressDialog;

    private LinearLayoutManager mLinearLayoutManager;
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
        url = URL_PRIMARY + GENNOTICE_MIDDLE+ Board_num + GETNOTICE; //파싱하기전 PRIMARY URL 과 공지사항 URL 을 합쳐 완전한 URL 을만든다.
        getItemlistFromWebpage();
    }

    // CUSTOM METHODS
    private void setDesignComponents() {
        mLinearLayoutManager = new LinearLayoutManager(Main_Notice_MainScene.this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager.setReverseLayout(false);

        recylerViewContainer = (RecyclerView) findViewById(R.id.notice_board_list);
        recylerViewContainer.setLayoutManager(mLinearLayoutManager);
        recylerViewContainer.setHasFixedSize(true);

        mAdapter = new adapterNoticeinfoItem(getApplicationContext(), mListData);
        recylerViewContainer.setAdapter(mAdapter);
        // FOOTER 추가해줘야함
    }

    private void getItemlistFromWebpage() {
        if (isInternetConnect() == true) {
            Toast.makeText(getApplicationContext(), "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            try {
//                processRetrieveItems(); // RSS를 이용하여 아이템을 얻음.
                process();
                mAdapter.notifyDataSetChanged(); // 갱신
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
                return false;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { //모바일 데이터 여부
                return false;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET)
                return false;
        } else {
            // not connected to the internet
        }
        return true;
    }

    private void processRetrieveItems() throws IOException {
        StringBuffer buffer = new StringBuffer(); // 스트링 버퍼를 준비한다.

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
            // 사실 listNoticeItem 객체를 생성하여 인자를 담아야 됨.
            Info_ListData temporalNotice = new Info_ListData();

            // xml 파서가 문서 끝을 가리킬 때까지 계속 구문을 반복한다.
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG: // 태그가 시작될 때

                        tag = xpp.getName(); // 태그 이름을 가져온다.

                        if (tag.equals("item")) // 글에 대한 정보가 있을 시
                            ;
                        else if (tag.equals("title")) { // 글 제목을 가져온다.
                            xpp.next();
                            temporalNotice.setmTitle(xpp.getText());
                        } else if (tag.equals("dc:creator")) { // 작성자를 가져온다.
                            xpp.next();
                            temporalNotice.setmWriter(xpp.getText());
                        } else if (tag.equals("link")) { // 해당 글의 정보를 얻기 위해 URL를 가져온다.
                            xpp.next();
                            temporalNotice.setmUrl(xpp.getText());
                        } else if (tag.equals("pubDate")) { // 작성일을 가져온다.
                            xpp.next();
                            temporalNotice.setmDate(xpp.getText());
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG : // 태그가 끝날 때
                        tag = xpp.getName();
                        // 공지 정보를 다 받았으면 객체를 만들어서 add한다.
                        if(tag.equals("item")) {
                            mListData.add(temporalNotice);
                            temporalNotice = new Info_ListData();
                        }
                        break;
                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
            Log.d("ERROR", e + "");
        }
    }

    private void process() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Notice_MainScene.this, "", "게시판 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")); //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for(int arrnum = 0;arrnum < tabletags.size(); arrnum++){ //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if(tabletags.get(arrnum).toString().equals("<div class=\"board_list\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum+""); //arrnum 로깅
                        break;
                    }
                }



                Element BBS_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_TABLE = (Element) BBS_DIV.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TBODY = (Element) BBS_TABLE.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속


                for(int C_TR = 0; C_TR < BBS_TBODY.getAllElements(HTMLElementName.TR).size();C_TR++){ //여기서는 이제부터 게시된 게시물 데이터를 불러와 게시판 인터페이스를 구성할 것이다.


                    // 소스의 효율성을 위해서는 for 문을 사용하는것이 좋지만 , 이해를 돕기위해 소스를 일부로 늘려 두었다.

                    try {
                        Element BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(C_TR); //TR 접속

                        Element BC_TYPE = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(0); //타입 을 불러온다. [공지]일때는 가능

                        Element BC_info = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(1);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                        Element BC_a = (Element) BC_info.getAllElements(HTMLElementName.A).get(0); //BC_info 안의 a 태그를 가져온다.
                        String BCS_url = BC_a.getAttributeValue("href"); //a 태그의 herf 는 BCS_url 로 선언
                        // String BCS_title = BC_a.getAttributeValue("title"); //a 태그의 title 은 BCS_title 로 선언
                        Element BC_writer = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(3); //글쓴이를 불러온다.
                        Element BC_date = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(4); // 날짜를 불러온다.
                        String BCS_title = BC_a.getContent().toString().trim();
                        String BCS_type = BC_TYPE.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
                        String BCS_writer = BC_writer.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
                        String BCS_date = BC_date.getContent().toString(); // 작성일자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.

                        mListData.add(new Info_ListData(BCS_type, BCS_title, BCS_url, BCS_writer, BCS_date)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/



                    }catch(Exception e){
                        Log.d("BCSERROR",e+"");
                    }
                }//for end


                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged(); // 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);



            }

        }.start();


    }
}