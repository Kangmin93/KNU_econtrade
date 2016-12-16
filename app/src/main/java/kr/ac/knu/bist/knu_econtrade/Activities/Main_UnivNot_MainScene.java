package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.ac.knu.bist.knu_econtrade.Adapters.BBSListAdapter;
import kr.ac.knu.bist.knu_econtrade.noticeComponents.Info_ListData;
import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by Vertx on 2016-09-10.
 * 10-10-28 : 덕윤이가 만들어놓은 공지사항 게시판을 옮김, 및 DEPRECATED 된 함수 대체완료.
 */
public class Main_UnivNot_MainScene extends AppCompatActivity {

    private static String URL_PRIMARY = "http://knu.ac.kr"; //홈페이지 원본 주소이다.
    private static String GENNOTICE_MIDDLE = "/wbbs/wbbs/bbs/btin/list.action?bbs_cde=1&btin.page=";
    //URL Format = http://knu.ac.kr/wbbs/wbbs/bbs/btin/list.action?bbs_cde=1&btin.page="number" <- 이부분이 바뀜.
    // &popupDeco=false&btin.search_type=&btin.search_text=&menu_idx=67
    private static String GETNOTICE = "&popupDeco=false&btin.search_type=&btin.search_text=&menu_idx=67";
    //홈페이지 의 게시판을 나타내는 뒤 주소, 비슷한 게시판들은 거의 파싱이 가능하므로 응용하여 사용하자.
    private String url;
    private java.net.URL URL;

    private Source source;
    private ProgressDialog progressDialog;
    private BBSListAdapter BBSAdapter = null;
    private ListView BBSList;
    private int BBSlocate;
    private int Board_num= 1;

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;

    ArrayList<Info_ListData> mListData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_notice);

        View footer = getLayoutInflater().inflate(R.layout.listview_footer, null, false) ;

        BBSList = (ListView)findViewById(R.id.listView); //리스트선언
        BBSList.addFooterView(footer);
        BBSAdapter = new BBSListAdapter(this, mListData);
        BBSList.setAdapter(BBSAdapter); //리스트에 어댑터를 먹여준다.
        BBSList.setOnItemClickListener( //리스트 클릭시 실행될 로직 선언
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        //화면 갱신 footer event 처리를 위해 getcount로 크기를 받아와서 처리한다.
                        if(position==BBSAdapter.getCount()){
                            Board_num++;
                            url = URL_PRIMARY + GENNOTICE_MIDDLE+ Board_num + GETNOTICE;
                            try {
                                process();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            BBSAdapter.notifyDataSetChanged();
                            Log.e("last","마지막 아이템 클릭");
                        }else {
                            Info_ListData mData = mListData.get(position); // 클릭한 포지션의 데이터를 가져온다.
                            String URL_BCS = mData.getmUrl(); //가져온 데이터 중 url 부분만 적출해낸다.
                            Intent intent = new Intent(getApplicationContext(), Main_UnivNot_DetailScene.class);
                            intent.putExtra("url", URL_PRIMARY + URL_BCS);
                            startActivity(intent);
                            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_PRIMARY + URL_BCS))); //적출해낸 url 을 이용해 URL_PRIMARY 와 붙이고
                        }
                    }
                });


        url = URL_PRIMARY + GENNOTICE_MIDDLE+ Board_num + GETNOTICE; //파싱하기전 PRIMARY URL 과 공지사항 URL 을 합쳐 완전한 URL 을만든다.


        if(isInternetCon()) { //false 반환시 if 문안의 로직 실행
            Toast.makeText(Main_UnivNot_MainScene.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }else{ //인터넷 체크 통과시 실행할 로직
            try {
                process(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                BBSAdapter.notifyDataSetChanged();
                //이때 새로 갱신함.
            } catch (Exception e) {
                Log.d("ERROR", e + "");

            }
        }
    }

    private boolean isInternetCon() {
        cManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cManager.getActiveNetworkInfo();

        if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) { //와이파이 여부
                    return false;
                }
                else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { //모바일 데이터 여부
                    return false;
                }
                else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET)
                    return false;
        }
        else {
            // not connected to the internet
        }
        return true;
    }

    private void process() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_UnivNot_MainScene.this, "", "게시판 정보를 가져오는중 입니다.");
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
                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);



            }

        }.start();


    }
}
