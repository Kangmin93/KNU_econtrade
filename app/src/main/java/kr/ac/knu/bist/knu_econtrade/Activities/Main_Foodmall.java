package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.ac.knu.bist.knu_econtrade.Adapters.BBSListAdapter;
import kr.ac.knu.bist.knu_econtrade.FoodmallComponents.FoodmallAdapter;
import kr.ac.knu.bist.knu_econtrade.FoodmallComponents.ListData;
import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by Vertx on 2016-09-10.
 */
public class Main_Foodmall extends AppCompatActivity{

    private static String URL_PRIMARY = "http://coop.knu.ac.kr"; //홈페이지 원본 주소이다.
    private static String GENNOTICE_MIDDLE = "/sub03/sub01_01.html?shop_sqno=";

    private String url;
    private java.net.URL URL;

    private Source source;
    private ProgressDialog progressDialog;
    private FoodmallAdapter BBSAdapter = null;
    private ListView BBSList;
    private int BBSlocate,BBSlocate2, TDlocate;
    private  int tagnum = 0;
    private int Board_num = 0;

    private TextView tv_morning,tv_afternoon,tv_dinner;
    private Spinner spin_food;
    private ArrayAdapter<CharSequence> spinadapter;

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;
    private String BCS_data ="";
    private String BCS_data1 = "";
    private String BCS_data2 = "";
    private String BCS_data3 = "";
    private String BCS_data4 = "";
    private Element BBS_TR1,BBS_TR2,BBS_TR3,BBS_TR4;
    Calendar cal;

    ArrayList<ListData> mListData = new ArrayList<>();

    @Override
    protected void onStop() { //멈추었을때 다이어로그를 제거해주는 메서드
        super.onStop();
        if (progressDialog != null)
            progressDialog.dismiss(); //다이어로그가 켜져있을경우 (!null) 종료시켜준다
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_activity_foodmall);

        cal= Calendar.getInstance();

        tv_morning = (TextView)findViewById(R.id.tv_morning);
        tv_afternoon = (TextView)findViewById(R.id.tv_afternoon);
        tv_dinner = (TextView)findViewById(R.id.tv_dinner);
        spin_food = (Spinner)findViewById(R.id.spin_food);
        spinadapter = ArrayAdapter.createFromResource(this,R.array.food,android.R.layout.simple_spinner_item);
        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_food.setAdapter(spinadapter);



        spin_food.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case 0:
                        Board_num = 46;
                        url = URL_PRIMARY + GENNOTICE_MIDDLE + Board_num;
                        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
                            Toast.makeText(Main_Foodmall.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { //인터넷 체크 통과시 실행할 로직
                            try {
                                processgam(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                                spinadapter.notifyDataSetChanged();
                                //BBSAdapter.notifyDataSetChanged();
                                //이때 새로 갱신함.
                            } catch (Exception e) {
                                Log.d("ERROR", e + "");

                            }
                        }
                        break;
                    case 1:
                        Board_num = 57;
                        url = URL_PRIMARY + GENNOTICE_MIDDLE + Board_num;
                        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
                            Toast.makeText(Main_Foodmall.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { //인터넷 체크 통과시 실행할 로직
                            try {
                                processgpilchung(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                                spinadapter.notifyDataSetChanged();
                                //BBSAdapter.notifyDataSetChanged();
                                //이때 새로 갱신함.
                            } catch (Exception e) {
                                Log.d("ERROR", e + "");

                            }
                        }
                        break;
                    case 2:
                        Board_num = 54;
                        url = URL_PRIMARY + GENNOTICE_MIDDLE + Board_num;
                        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
                            Toast.makeText(Main_Foodmall.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { //인터넷 체크 통과시 실행할 로직
                            try {
                                processengteacher(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                                //BBSAdapter.notifyDataSetChanged();
                                //이때 새로 갱신함.
                            } catch (Exception e) {
                                Log.d("ERROR", e + "");

                            }
                        }
                    break;
                    case 3:
                        Board_num = 40;
                        url = URL_PRIMARY + GENNOTICE_MIDDLE + Board_num;
                        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
                            Toast.makeText(Main_Foodmall.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { //인터넷 체크 통과시 실행할 로직
                            try {
                                processengstudent(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                                //BBSAdapter.notifyDataSetChanged();
                                //이때 새로 갱신함.
                            } catch (Exception e) {
                                Log.d("ERROR", e + "");

                            }
                        }
                    break;
                    case 4:
                        Board_num = 36;
                        url = URL_PRIMARY + GENNOTICE_MIDDLE + Board_num;
                        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
                            Toast.makeText(Main_Foodmall.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { //인터넷 체크 통과시 실행할 로직
                            try {
                                processbokjiteacher(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                                //BBSAdapter.notifyDataSetChanged();
                                //이때 새로 갱신함.
                            } catch (Exception e) {
                                Log.d("ERROR", e + "");

                            }
                        }
                    break;
                    case 5:
                        Board_num = 37;
                        url = URL_PRIMARY + GENNOTICE_MIDDLE + Board_num;
                        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
                            Toast.makeText(Main_Foodmall.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { //인터넷 체크 통과시 실행할 로직
                            try {
                                processbokjistudent(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                                //BBSAdapter.notifyDataSetChanged();
                                //이때 새로 갱신함.
                            } catch (Exception e) {
                                Log.d("ERROR", e + "");

                            }
                        }
                    break;
                    case 6:
                        Board_num = 39;
                        url = URL_PRIMARY + GENNOTICE_MIDDLE + Board_num;
                        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
                            Toast.makeText(Main_Foodmall.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { //인터넷 체크 통과시 실행할 로직
                            try {
                                processbokhyunteacher(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                                //BBSAdapter.notifyDataSetChanged();
                                //이때 새로 갱신함.
                            } catch (Exception e) {
                                Log.d("ERROR", e + "");

                            }
                        }
                    break;
                    case 7:
                        Board_num = 56;
                        url = URL_PRIMARY + GENNOTICE_MIDDLE + Board_num;
                        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
                            Toast.makeText(Main_Foodmall.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { //인터넷 체크 통과시 실행할 로직
                            try {
                                processbokhtunstudent(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                                //BBSAdapter.notifyDataSetChanged();
                                //이때 새로 갱신함.
                            } catch (Exception e) {
                                Log.d("ERROR", e + "");

                            }
                        }
                    break;
                    case 8:
                        Board_num = 35;
                        url = URL_PRIMARY + GENNOTICE_MIDDLE + Board_num;
                        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
                            Toast.makeText(Main_Foodmall.this, "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else { //인터넷 체크 통과시 실행할 로직
                            try {
                                processjungbo(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                                //BBSAdapter.notifyDataSetChanged();
                                //이때 새로 갱신함.
                            } catch (Exception e) {
                                Log.d("ERROR", e + "");

                            }
                        }
                    break;
                }
                Log.e("why!!!", "" + i);


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

    }

    private void processgam() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Foodmall.this, "", "게시판 정보를 가져오는중 입니다.");
                        spinadapter.notifyDataSetChanged();
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")) ; //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }


                Element BBS_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_TABLE = (Element) BBS_DIV.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TBODY = (Element) BBS_TABLE.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속



                try {
                    Element BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(0); //TR 접속
                    List<StartTag> TDtags = source.getAllStartTags(HTMLElementName.TD);
                    for (int TDnum = 0; TDnum < TDtags.size(); TDnum++) {
                        if (TDtags.get(TDnum).toString().equals("<td scope=\"row\"  class=\"on\">")) {
                            TDlocate = TDnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                            Log.d("gigigi", TDnum + ""); //arrnum 로깅
                            break;
                        }
                        Log.e("gigigi", TDtags.get(TDnum).toString() + ""); //arrnum 로깅
                    }


                    Element BC_info = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_ul = (Element) BC_info.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_li = (Element)BC_ul.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_p = (Element)BC_li.getAllElements(HTMLElementName.P).get(0);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언

                    BCS_data = BC_p.getContent().toString().trim();
//                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.


                    BCS_data = BCS_data.replace("<br />", "");

//                    mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/


                } catch (Exception e) {
                    Log.d("BCSERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_afternoon.setText(BCS_data);
                        tv_dinner.setText("");
//                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();


    }
    private void processbokhtunstudent() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Foodmall.this, "", "게시판 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")) ; //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }


                Element BBS_DIV1 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_DIV2 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate);
                Element BBS_TABLE1 = (Element) BBS_DIV1.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TABLE2 = (Element) BBS_DIV2.getAllElements(HTMLElementName.TABLE).get(0);
                Element BBS_TBODY1 = (Element) BBS_TABLE1.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속
                Element BBS_TBODY2 = (Element) BBS_TABLE2.getAllElements(HTMLElementName.TBODY).get(0);



                try {
                    Element BBS_TR1 = (Element) BBS_TBODY1.getAllElements(HTMLElementName.TR).get(0); //TR 접속
                    Element BBS_TR2 = (Element) BBS_TBODY2.getAllElements(HTMLElementName.TR).get(1);
                    List<StartTag> TDtags = source.getAllStartTags(HTMLElementName.TD);
                    for(int TDnum = 0; TDnum<TDtags.size();TDnum++)
                    {
                        if (TDtags.get(TDnum).toString().equals("<td scope=\"row\"  class=\"on\">")) {
                            TDlocate = TDnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                            Log.d("gigigi", TDnum + ""); //arrnum 로깅
                            break;
                        } Log.e("gigigi", TDtags.get(TDnum).toString() + ""); //arrnum 로깅
                    }
                    Element BC_info1 = (Element) BBS_TR1.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_info2 = (Element) BBS_TR2.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_ul1 = (Element) BC_info1.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_ul2 = (Element) BC_info2.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_li1 = (Element)BC_ul1.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_li2 = (Element)BC_ul2.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_p1 = (Element)BC_li1.getAllElements(HTMLElementName.P).get(0);
                    Element BC_p2 = (Element)BC_li2.getAllElements(HTMLElementName.P).get(0);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언

                    BCS_data1 = BC_p1.getContent().toString().trim();
                    BCS_data2 = BC_p2.getContent().toString().trim();
                    //                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.


                    BCS_data1 = BCS_data1.replace("<br />", "");
                    BCS_data2 = BCS_data2.replace("<br />", "");

//                    mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/


                } catch (Exception e) {
                    Log.d("BCSERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_afternoon.setText(BCS_data1+"\n"+BCS_data2);
                        tv_dinner.setText("");
//                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();


    }
    private void processbokhyunteacher() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Foodmall.this, "", "게시판 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")) ; //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }


                Element BBS_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_TABLE = (Element) BBS_DIV.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TBODY = (Element) BBS_TABLE.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속



                try {
                    Element BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(0); //TR 접속
                    List<StartTag> TDtags = source.getAllStartTags(HTMLElementName.TD);
                    for (int TDnum = 0; TDnum < TDtags.size(); TDnum++) {
                        if (TDtags.get(TDnum).toString().equals("<td scope=\"row\"  class=\"on\">")) {
                            TDlocate = TDnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                            Log.d("gigigi", TDnum + ""); //arrnum 로깅
                            break;
                        }
                        Log.e("gigigi", TDtags.get(TDnum).toString() + ""); //arrnum 로깅
                    }


                    Element BC_info = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_ul = (Element) BC_info.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_li = (Element)BC_ul.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_p = (Element)BC_li.getAllElements(HTMLElementName.P).get(0);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언

                    BCS_data = BC_p.getContent().toString().trim();
//                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.


                    BCS_data = BCS_data.replace("<br />", "");

//                    mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/


                } catch (Exception e) {
                    Log.d("BCSERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_afternoon.setText(BCS_data);
                        tv_dinner.setText("");
//                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();


    }
    private void processbokjistudent() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Foodmall.this, "", "게시판 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")) ; //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }


                Element BBS_DIV1 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_DIV2 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate + 16);
                Element BBS_TABLE1 = (Element) BBS_DIV1.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TABLE2 = (Element) BBS_DIV2.getAllElements(HTMLElementName.TABLE).get(0);
                Element BBS_TBODY1 = (Element) BBS_TABLE1.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속
                Element BBS_TBODY2 = (Element) BBS_TABLE2.getAllElements(HTMLElementName.TBODY).get(0);



                try {
                    Element BBS_TR1 = (Element) BBS_TBODY1.getAllElements(HTMLElementName.TR).get(0); //TR 접속
                    Element BBS_TR2 = (Element) BBS_TBODY1.getAllElements(HTMLElementName.TR).get(1);
                    Element BBS_TR3 = (Element) BBS_TBODY1.getAllElements(HTMLElementName.TR).get(2);
                    Element BBS_TR4 = (Element) BBS_TBODY2.getAllElements(HTMLElementName.TR).get(0);

                    List<StartTag> TDtags = source.getAllStartTags(HTMLElementName.TD);
                    for (int TDnum = 0; TDnum < TDtags.size(); TDnum++) {
                        if (TDtags.get(TDnum).toString().equals("<td scope=\"row\"  class=\"on\">")) {
                            TDlocate = TDnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                            Log.d("gigigi", TDnum + ""); //arrnum 로깅
                            break;
                        }
                        Log.e("gigigi", TDtags.get(TDnum).toString() + ""); //arrnum 로깅
                    }
                    Element BC_info1 = (Element) BBS_TR1.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_info2 = (Element) BBS_TR2.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_info3 = (Element) BBS_TR3.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_info4 = (Element) BBS_TR4.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_ul1 = (Element) BC_info1.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_ul2 = (Element) BC_info2.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_ul3 = (Element) BC_info3.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_ul4 = (Element) BC_info4.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    List<StartTag> litags1 = BC_ul3.getAllStartTags(HTMLElementName.LI);
                    List<StartTag> litags2 = BC_ul4.getAllStartTags(HTMLElementName.LI);
                    for (int i = 0; i < litags1.size(); i++)
                    {
                        Element BC_li3 = (Element)BC_ul3.getAllElements(HTMLElementName.LI).get(i);
                        BCS_data3 +=BC_li3.getContent().toString().trim()+"\n";
                    }
                    for(int i = 0;i<litags2.size();i++)
                    {
                        Element BC_li4 = (Element)BC_ul4.getAllElements(HTMLElementName.LI).get(i);
                        BCS_data4 += BC_li4.getContent().toString().trim()+"\n";
                    }
                    Element BC_li1 = (Element)BC_ul1.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_li2 = (Element)BC_ul2.getAllElements(HTMLElementName.LI).get(0);

                    Element BC_p1 = (Element)BC_li1.getAllElements(HTMLElementName.P).get(0);
                    //Element BC_p2 = (Element)BC_li2.getAllElements(HTMLElementName.P).get(0);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언

                    BCS_data1 = BC_p1.getContent().toString().trim();
                    BCS_data2 = BC_li2.getContent().toString().trim();

                    //                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.


                    BCS_data1 = BCS_data1.replace("<br />", "");
                    BCS_data2 = BCS_data2.replace("<br />", "").replace("<p>","").replace("</p>","");
                    BCS_data3 = BCS_data3.replace("<br />", "").replace("<p>","").replace("</p>","");
                    BCS_data4 = BCS_data4.replace("<br />", "").replace("<p>","").replace("</p>","");

//                    mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/


                } catch (Exception e) {
                    Log.d("BCSERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_afternoon.setText(BCS_data1+"\n"+BCS_data2+"\n"+BCS_data3);
                        tv_dinner.setText(BCS_data4);
//                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();


    }
    private void processbokjiteacher() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Foodmall.this, "", "게시판 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")) ; //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }


                Element BBS_DIV1 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_DIV2 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate);
                Element BBS_TABLE1 = (Element) BBS_DIV1.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TABLE2 = (Element) BBS_DIV2.getAllElements(HTMLElementName.TABLE).get(0);
                Element BBS_TBODY1 = (Element) BBS_TABLE1.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속
                Element BBS_TBODY2 = (Element) BBS_TABLE2.getAllElements(HTMLElementName.TBODY).get(0);



                try {
                    Element BBS_TR1 = (Element) BBS_TBODY1.getAllElements(HTMLElementName.TR).get(0); //TR 접속
                    Element BBS_TR2 = (Element) BBS_TBODY2.getAllElements(HTMLElementName.TR).get(1);
                    List<StartTag> TDtags = source.getAllStartTags(HTMLElementName.TD);
                    for(int TDnum = 0; TDnum<TDtags.size();TDnum++)
                    {
                        if (TDtags.get(TDnum).toString().equals("<td scope=\"row\"  class=\"on\">")) {
                            TDlocate = TDnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                            Log.d("gigigi", TDnum + ""); //arrnum 로깅
                            break;
                        } Log.e("gigigi", TDtags.get(TDnum).toString() + ""); //arrnum 로깅
                    }
                    Element BC_info1 = (Element) BBS_TR1.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_info2 = (Element) BBS_TR2.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_ul1 = (Element) BC_info1.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_ul2 = (Element) BC_info2.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_li1 = (Element)BC_ul1.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_li2 = (Element)BC_ul2.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_p1 = (Element)BC_li1.getAllElements(HTMLElementName.P).get(0);
                    Element BC_p2 = (Element)BC_li2.getAllElements(HTMLElementName.P).get(0);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언

                    BCS_data1 = BC_p1.getContent().toString().trim();
                    BCS_data2 = BC_p2.getContent().toString().trim();
                    //                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.


                    BCS_data1 = BCS_data1.replace("<br />", "");
                    BCS_data2 = BCS_data2.replace("<br />", "");

//                    mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/


                } catch (Exception e) {
                    Log.d("BCSERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_afternoon.setText(BCS_data1+"\n"+BCS_data2);
                        tv_dinner.setText("");
//                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();


    }
    private void processengteacher() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Foodmall.this, "", "게시판 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")) ; //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }


                Element BBS_DIV1 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_DIV2 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate + 6);
                Element BBS_TABLE1 = (Element) BBS_DIV1.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TABLE2 = (Element) BBS_DIV2.getAllElements(HTMLElementName.TABLE).get(0);
                Element BBS_TBODY1 = (Element) BBS_TABLE1.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속
                Element BBS_TBODY2 = (Element) BBS_TABLE2.getAllElements(HTMLElementName.TBODY).get(0);



                try {
                    Element BBS_TR1 = (Element) BBS_TBODY1.getAllElements(HTMLElementName.TR).get(0); //TR 접속
                    Element BBS_TR2 = (Element) BBS_TBODY2.getAllElements(HTMLElementName.TR).get(0);
                    List<StartTag> TDtags = source.getAllStartTags(HTMLElementName.TD);
                    for(int TDnum = 0; TDnum<TDtags.size();TDnum++)
                    {
                        if (TDtags.get(TDnum).toString().equals("<td scope=\"row\"  class=\"on\">")) {
                            TDlocate = TDnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                            Log.d("gigigi", TDnum + ""); //arrnum 로깅
                            break;
                        } Log.e("gigigi", TDtags.get(TDnum).toString() + ""); //arrnum 로깅
                    }
                    Element BC_info1 = (Element) BBS_TR1.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_info2 = (Element) BBS_TR2.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_ul1 = (Element) BC_info1.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_ul2 = (Element) BC_info2.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_li1 = (Element)BC_ul1.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_li2 = (Element)BC_ul2.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_p1 = (Element)BC_li1.getAllElements(HTMLElementName.P).get(0);
                    Element BC_p2 = (Element)BC_li2.getAllElements(HTMLElementName.P).get(0);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언

                    BCS_data1 = BC_p1.getContent().toString().trim();
                    BCS_data2 = BC_p2.getContent().toString().trim();
                    //                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.


                    BCS_data1 = BCS_data1.replace("<br />", "");
                    BCS_data2 = BCS_data2.replace("<br />", "");

//                    mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/


                } catch (Exception e) {
                    Log.d("BCSERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_afternoon.setText(BCS_data1);
                        tv_dinner.setText(BCS_data2);
//                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();


    }
    private void processgpilchung() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Foodmall.this, "", "게시판 정보를 가져오는중 입니다.");
                        spinadapter.notifyDataSetChanged();
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")) ; //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }


                Element BBS_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_TABLE = (Element) BBS_DIV.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TBODY = (Element) BBS_TABLE.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속
                BCS_data = "";




                try {
                    Element BBS_TR = (Element) BBS_TBODY.getAllElements(HTMLElementName.TR).get(0); //TR 접속
                    List<StartTag> TDtags = source.getAllStartTags(HTMLElementName.TD);
                    for (int TDnum = 0; TDnum < TDtags.size(); TDnum++) {
                        if (TDtags.get(TDnum).toString().equals("<td scope=\"row\"  class=\"on\">")) {
                            TDlocate = TDnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                            Log.d("gigigi", TDnum + ""); //arrnum 로깅
                            break;
                        }
                        Log.e("gigigi", TDtags.get(TDnum).toString() + ""); //arrnum 로깅
                    }

                    Element BC_info = (Element) BBS_TR.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_ul = (Element) BC_info.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    for(int i=0;i < 8;i++)
                    {
                        Element BC_li = (Element)BC_ul.getAllElements(HTMLElementName.LI).get(i);
                        //Element BC_p = (Element)BC_li.getAllElements(HTMLElementName.P).get(1);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언

                        BCS_data += BC_li.getContent().toString().trim();
                        BCS_data += "\n";
                    }

//                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.


                    BCS_data = BCS_data.replace("<br />", "").replace("<p>","").replace("</p>","");

//                    mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/


                } catch (Exception e) {
                    Log.d("BCSERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_afternoon.setText(BCS_data);
                        tv_dinner.setText("");
//                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();


    }
    private void processjungbo() throws IOException {


        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Foodmall.this, "", "게시판 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")) ; //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }
                for (int arrnum = BBSlocate + 1; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate2 = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }


                Element BBS_DIV1 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_DIV2 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate2);
                Element BBS_TABLE1 = (Element) BBS_DIV1.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TABLE2 = (Element) BBS_DIV2.getAllElements(HTMLElementName.TABLE).get(0);
                Element BBS_TBODY1 = (Element) BBS_TABLE1.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속
                Element BBS_TBODY2 = (Element) BBS_TABLE2.getAllElements(HTMLElementName.TBODY).get(0);



                try {
                    BBS_TR1 = (Element) BBS_TBODY1.getAllElements(HTMLElementName.TR).get(0); //TR 접속
                    BBS_TR2 = (Element) BBS_TBODY1.getAllElements(HTMLElementName.TR).get(1);
                    BBS_TR3 = (Element) BBS_TBODY2.getAllElements(HTMLElementName.TR).get(0);
                    BBS_TR4 = (Element) BBS_TBODY2.getAllElements(HTMLElementName.TR).get(1);

                    List<StartTag> TDtags = BBS_TR1.getAllStartTags(HTMLElementName.TD);
                    for (int TDnum = 0; TDnum < TDtags.size(); TDnum++) {
                        if (TDtags.get(TDnum).toString().equals("<td scope=\"row\"  class=\"on\">")) {
                            TDlocate = TDnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                            Log.d("gigigi", TDnum + ""); //arrnum 로깅
                            break;
                        }
                        Log.e("gigigi", TDtags.get(TDnum).toString() + ""); //arrnum 로깅
                    }


//                    mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/


                } catch (Exception e) {
                    Log.d("BCSERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(TDlocate != 5)
                        {
                            Element BC_info1 = (Element) BBS_TR1.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                            Element BC_info2 = (Element) BBS_TR2.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                            Element BC_info3 = (Element) BBS_TR3.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                            Element BC_info4 = (Element) BBS_TR4.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                            Element BC_ul1 = (Element) BC_info1.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                            Element BC_ul2 = (Element) BC_info2.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                            Element BC_ul3 = (Element) BC_info3.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                            Element BC_ul4 = (Element) BC_info4.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                            List<StartTag> litags1 = BC_ul2.getAllStartTags(HTMLElementName.LI);
                            List<StartTag> litags2 = BC_ul4.getAllStartTags(HTMLElementName.LI);

                            for (int i = 0; i < litags1.size(); i++)
                            {
                                Element BC_li2 = (Element)BC_ul2.getAllElements(HTMLElementName.LI).get(i);
                                BCS_data2 +=BC_li2.getContent().toString().trim()+"\n";
                            }
                            for(int i = 0;i<litags2.size();i++)
                            {
                                Element BC_li4 = (Element)BC_ul4.getAllElements(HTMLElementName.LI).get(i);
                                BCS_data4 += BC_li4.getContent().toString().trim()+"\n";
                            }

                            Element BC_li1 = (Element)BC_ul1.getAllElements(HTMLElementName.LI).get(0);
                            Element BC_li3 = (Element)BC_ul3.getAllElements(HTMLElementName.LI).get(0);

                            Element BC_p1 = (Element)BC_li1.getAllElements(HTMLElementName.P).get(0);
                            Element BC_p2 = (Element)BC_li3.getAllElements(HTMLElementName.P).get(0);
                            //Element BC_p2 = (Element)BC_li2.getAllElements(HTMLElementName.P).get(0);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언

                            BCS_data1 = BC_p1.getContent().toString().trim();
                            BCS_data3 = BC_p2.getContent().toString().trim();

                            //                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.


                            BCS_data1 = BCS_data1.replace("<br />", "").replace("<p>","").replace("</p>","");
                            BCS_data2 = BCS_data2.replace("<br />", "").replace("<p>","").replace("</p>","");
                            BCS_data3 = BCS_data3.replace("<br />", "").replace("<p>","").replace("</p>","");
                            BCS_data4 = BCS_data4.replace("<br />", "").replace("<p>","").replace("</p>","");
                            tv_afternoon.setText(BCS_data1+"\n"+BCS_data2);
                            tv_dinner.setText(BCS_data3+"\n"+BCS_data4);
                        }else{

                            Element BC_info2 = (Element) BBS_TR2.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.

                            Element BC_info4 = (Element) BBS_TR4.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.

                            Element BC_ul2 = (Element) BC_info2.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.

                            Element BC_ul4 = (Element) BC_info4.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                            List<StartTag> litags1 = BC_ul2.getAllStartTags(HTMLElementName.LI);
                            List<StartTag> litags2 = BC_ul4.getAllStartTags(HTMLElementName.LI);
                            for (int i = 0; i < litags1.size(); i++)
                            {
                                Element BC_li2 = (Element)BC_ul2.getAllElements(HTMLElementName.LI).get(i);
                                BCS_data2 +=BC_li2.getContent().toString().trim()+"\n";
                            }
                            for(int i = 0;i<litags2.size();i++)
                            {
                                Element BC_li4 = (Element)BC_ul4.getAllElements(HTMLElementName.LI).get(i);
                                BCS_data4 += BC_li4.getContent().toString().trim()+"\n";
                            }


                            //Element BC_p2 = (Element)BC_li2.getAllElements(HTMLElementName.P).get(0);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언



                            //                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.



                            BCS_data2 = BCS_data2.replace("<br />", "").replace("<p>","").replace("</p>","");

                            BCS_data4 = BCS_data4.replace("<br />", "").replace("<p>","").replace("</p>","");
                            tv_afternoon.setText(BCS_data2);
                            tv_dinner.setText(BCS_data4);
                        }
//                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();


    }
    private void processengstudent() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_Foodmall.this, "", "게시판 정보를 가져오는중 입니다.");
                    }
                }, 0);

                try {
                    URL = new URL(url);
                    InputStream html = URL.openStream();
                    source = new Source(new InputStreamReader(html, "utf-8")) ; //소스를 UTF-8 인코딩으로 불러온다.
                    source.fullSequentialParse(); //순차적으로 구문분석
                } catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> tabletags = source.getAllStartTags(HTMLElementName.DIV); // DIV 타입의 모든 태그들을 불러온다.

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"week_table mt5\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }


                Element BBS_DIV1 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element BBS_DIV2 = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate);
                Element BBS_TABLE1 = (Element) BBS_DIV1.getAllElements(HTMLElementName.TABLE).get(0); //테이블 접속
                Element BBS_TABLE2 = (Element) BBS_DIV2.getAllElements(HTMLElementName.TABLE).get(0);
                Element BBS_TBODY1 = (Element) BBS_TABLE1.getAllElements(HTMLElementName.TBODY).get(0); //데이터가 있는 TBODY 에 접속
                Element BBS_TBODY2 = (Element) BBS_TABLE2.getAllElements(HTMLElementName.TBODY).get(0);



                try {
                    Element BBS_TR1 = (Element) BBS_TBODY1.getAllElements(HTMLElementName.TR).get(0); //TR 접속
                    Element BBS_TR2 = (Element) BBS_TBODY2.getAllElements(HTMLElementName.TR).get(1);
                    List<StartTag> TDtags = source.getAllStartTags(HTMLElementName.TD);
                    for(int TDnum = 0; TDnum<TDtags.size();TDnum++)
                    {
                        if (TDtags.get(TDnum).toString().equals("<td scope=\"row\"  class=\"on\">")) {
                            TDlocate = TDnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                            Log.d("gigigi", TDnum + ""); //arrnum 로깅
                            break;
                        } Log.e("gigigi", TDtags.get(TDnum).toString() + ""); //arrnum 로깅
                    }
                    Element BC_info1 = (Element) BBS_TR1.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_info2 = (Element) BBS_TR2.getAllElements(HTMLElementName.TD).get(TDlocate);//URL(herf) TITLE(title) 을 담은 정보를 불러온다.
                    Element BC_ul1 = (Element) BC_info1.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_ul2 = (Element) BC_info2.getAllElements(HTMLElementName.UL).get(0); //BC_info 안의 a 태그를 가져온다.
                    Element BC_li1 = (Element)BC_ul1.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_li2 = (Element)BC_ul2.getAllElements(HTMLElementName.LI).get(0);
                    Element BC_p1 = (Element)BC_li1.getAllElements(HTMLElementName.P).get(0);
                    Element BC_p2 = (Element)BC_li2.getAllElements(HTMLElementName.P).get(0);
//                    String BCS_url = BC_p.getAttributes().toString(); //a 태그의 herf 는 BCS_url 로 선언

                    BCS_data1 = BC_p1.getContent().toString().trim();
                    BCS_data2 = BC_p2.getContent().toString().trim();
                    //                    String BCS_type = BC_p.getContent().toString(); // 타입값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.
//                    String BCS_writer = BC_p.getContent().toString(); // 작성자값을 담은 엘레먼트의 컨텐츠를 문자열로 변환시켜 가져온다.


                    BCS_data1 = BCS_data1.replace("<br />", "");
                    BCS_data2 = BCS_data2.replace("<br />", "");

//                    mListData.add(new ListData(BCS_type, BCS_title, BCS_url, BCS_writer)); //데이터가 모이면 데이터 리스트 클래스에 데이터들을 등록한다.
                        /* Log.d("BCSARR","타입:"+BCS_type+"\n제목:" +BCS_title +"\n주소:"+BCS_url +"\n글쓴이:" + BCS_writer + "\n날짜:" + BCS_date);*/


                } catch (Exception e) {
                    Log.d("BCSERROR", e + "");
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_afternoon.setText(BCS_data1+"\n"+BCS_data2);
                        tv_dinner.setText("");
//                        BBSAdapter.notifyDataSetChanged(); //모든 작업이 끝나면 리스트 갱신
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start();


    }


    private boolean isInternetCon() {
        cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); //모바일 데이터 여부
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //와이파이 여부
        return !mobile.isConnected() && !wifi.isConnected(); //결과값을 리턴
    }
}
