package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.knu.bist.knu_econtrade.Adapters.BBSListAdapter;
import kr.ac.knu.bist.knu_econtrade.noticeComponents.Info_ListData;
import kr.ac.knu.bist.knu_econtrade.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Vertx on 2016-09-10.
 * 16-10-10 : SubActivity 의 내용을 옮김.
 */

/*build.gradle (module : app) 부분 중 compile 'com.commit451:PhotoView:1.2.4'에서
*  private PhotoViewAttacher photo;를 호출하였음. 이 후 에러가 발생할 경우 photoview attcher 관련 키워드를 검색하여
*  library를 갱신해줄 것.
* */

public class Main_UnivNot_DetailScene extends AppCompatActivity {

    private String url;
    private java.net.URL URL;

    private Source source;
    private ProgressDialog progressDialog;
    private BBSListAdapter BBSAdapter = null;
    private ListView BBSList;
    private int BBSlocate;
    private TextView title, content, attachedfile;
    private ImageView imageview;
    private List<String> imageList = null;
    private List<URL> urlList = null;
    private LinearLayout linearLayout = null;
    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;
    private ArrayList<String> attached_list;
    private ScrollView scroll;
    private PhotoViewAttacher photo;
    ArrayList<Info_ListData> mListData = new ArrayList<>();
    private String attached_link1, attached_link2, attached_link3, attached_link4, attached_link5;
    private ArrayList<String> attached_link;
    private String doc_no, appl_no, file_nbr = "";
    ArrayList<String> Attach_name;
    ArrayList<String> Attach_link;
    private ArrayAdapter<Element> adapter;
    private DownloadManager dm;
    private DownloadManager.Request request;
    private Uri urlToDownload;
    private long latestId = -1;
    private ArrayList<String> attach_name_list;
    private ListView attached_listview;
    private NotificationManager nManger;
    private long download_file_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        title = (TextView) findViewById(R.id.knu_notice_title);
        scroll = (ScrollView) findViewById(R.id.myscrollView);
        linearLayout = (LinearLayout) findViewById(R.id.imageLayout);
        content = (TextView) findViewById(R.id.knu_notice);
        imageview = (ImageView) findViewById(R.id.knu_notice_image);
        attached_listview = (ListView) findViewById(R.id.attached_listview);
        Attach_name = new ArrayList<String>();
        Attach_link = new ArrayList<String>();
        attached_link = new ArrayList<String>();
        Intent intent = getIntent(); // 앞서 보낸 url을 받는다.
        url = intent.getExtras().getString("url");
        //attached link sample
        //http://knu.ac.kr/wbbs/wbbs/bbs/btin/download.action?appFile.bbs_cde=1&appFile.doc_no=1313499&appFile.appl_no=000000&appFile.file_nbr=0&bbs_cde=1&btin.doc_no=1313499&btin.bbs_cde=1&btin.appl_no=000000
        attached_link1 = "http://knu.ac.kr/wbbs/wbbs/bbs/btin/download.action?appFile.bbs_cde=1&appFile.doc_no=";
        attached_link2 = "&appFile.appl_no=";
        attached_link3 = "&appFile.file_nbr=";
        attached_link4 = "&bbs_cde=1&btin.doc_no=";
        attached_link5 = "&btin.bbs_cde=1&btin.appl_no=";
        dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        attached_listview = (ListView) findViewById(R.id.attached_listview);

        /*                          선언                       선언                      선언                             선언                                선언                 */

        attached_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //file download 할 부분.
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(attached_link.get(position)));
                request.setTitle(attach_name_list.get(position));
                download_file_ID= dm.enqueue(request);
            }
        });

        if (isInternetCon()) { //false 반환시 if 문안의 로직 실행
            Toast.makeText(getApplicationContext(), "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            finish();
        } else { //인터넷 체크 통과시 실행할 로직
            try {
                process(); //네트워크 관련은 따로 쓰레드를 생성해야 UI 쓰레드와 겹치지 않는다. 그러므로 Thread 가 선언된 process 메서드를 호출한다.
                BBSAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.d("ERROR", e + "");

            }
        }


    }//onCreate end

    /*              On Create                           On Create                   On create                           Oncreate                        */
    protected void onStop() { //멈추었을때 다이어로그를 제거해주는 메서드
        super.onStop();
        if (progressDialog != null)
            progressDialog.dismiss(); //다이어로그가 켜져있을경우 (!null) 종료시켜준다
    }


    private void process() throws IOException {

        new Thread() {
            @Override
            public void run() {
                Handler Progress = new Handler(Looper.getMainLooper()); //네트워크 쓰레드와 별개로 따로 핸들러를 이용하여 쓰레드를 생성한다.
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(Main_UnivNot_DetailScene.this, "", "게시판 정보를 가져오는중 입니다.");
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

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //DIV 모든 태그중 bbsContent 태그가 몇번째임을 구한다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"board_view\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }

                }


                Element BBS_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate); //BBSlocate 번째 의 DIV 를 모두 가져온다.
                Element Notice_Title = (Element) BBS_DIV.getAllElements(HTMLElementName.H2).get(0); // <h2>태그로 제목을 해놨음.
                final String Notice_title = Notice_Title.getContent().toString();
                Element Notice_Date = (Element) BBS_DIV.getAllElements(HTMLElementName.DD).get(0); //board_view의 voard info로 접근
                final String Notice_date = Notice_Date.getContent().toString();
                Element Notice_Writer = (Element) BBS_DIV.getAllElements(HTMLElementName.DD).get(1);
                final String Notice_writer = Notice_Writer.getContent().toString();


                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //content를 편하게 긁어올 수 있도록 for을 다시 한번 돌린다.


                    if (tabletags.get(arrnum).toString().equals("<div id=\"viewcontent\" class=\"board_cont\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }
                Element Notice_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate);
                String Notice = Notice_DIV.getContent().toString(); //.replaceAll("/wbbs/files/bbs/", "http://knu.ac.kr/wbbs/files/bbs/");
                imageList = getImgSrc(Notice);
                int srcInt = Notice.indexOf("<img");
                Log.e("mmsg", srcInt + "");

                for (int arrnum = 0; arrnum < tabletags.size(); arrnum++) { //content를 편하게 긁어올 수 있도록 for을 다시 한번 돌린다.


                    if (tabletags.get(arrnum).toString().equals("<div class=\"attach\">")) {
                        BBSlocate = arrnum; //DIV 클래스가 bbsContent 면 arrnum 값을 BBSlocate 로 몇번째인지 저장한다.
                        Log.d("BBSLOCATES", arrnum + ""); //arrnum 로깅
                        break;
                    }
                }
                Element Attach_DIV = (Element) source.getAllElements(HTMLElementName.DIV).get(BBSlocate);
                try {
                    List<Element> Temp = Attach_DIV.getAllElements(HTMLElementName.A);
                    Attach_name = new ArrayList<String>();
                    Attach_link = new ArrayList<String>();

                    for (int i = 0; i < Temp.size(); i++) {
                        Element Attach_Link = (Element) Temp.get(i);
                        Log.e("test", Attach_Link.toString());
                        if (i % 2 == 0) {
                            Attach_name.add(Attach_Link.getContent().toString());
                            Attach_link.add(Attach_Link.toString());
                        }
                    }
                    attach_name_list = new ArrayList<String>();
                    Log.e("attach name size is", Attach_name.size() + "");
                    for (int i = 0; i < Attach_name.size(); i++) {
                        attach_name_list.add(Attach_name.get(i).toString());
                        Log.e("attach_link get", Attach_link.get(i) + "");
                    }


                    for (int i = 0; i < Attach_name.size(); i++) {
                        String attach_link = Temp.get(2 * i).toString();

                        Log.e("attach_link", attach_link);

                        int link_start = attach_link.indexOf("'");
                        int link_end = attach_link.indexOf("'", link_start + 1);
                        appl_no = attach_link.substring(link_start + 1, link_end);

                        int link_start2 = attach_link.indexOf("'", link_end + 1);
                        int link_end2 = attach_link.indexOf("'", link_start2 + 1);
                        file_nbr = attach_link.substring(link_start2 + 1, link_end2);

                        int link_start3 = attach_link.indexOf("'", link_end2 + 1);
                        int link_end3 = attach_link.indexOf("'", link_start3 + 1);
                        doc_no = attach_link.substring(link_start3 + 1, link_end3);
                        Log.e("asdfasdfa", link_start + "/" + link_end + "/" + appl_no + "/" + file_nbr + "/" + doc_no);

                        attached_link.add(attached_link1 + appl_no + attached_link2 + file_nbr + attached_link3 + doc_no + attached_link4 + appl_no + attached_link5 + file_nbr + "");
                        Log.e("attached_link", attached_link.get(i));
                    }
                    /*attached_link는 파일을 다운로드 받을 수 있는 url 주소입니다.
                    * Attach_name은 파싱한 file명입니다.
                    * */

                } catch (Exception e) {
                    //error가 발생하면 첨부된 파일이 없다는 것임.
                    //**첨부파일 유무 검사해야됨.**
                    e.printStackTrace();
                }
                if (srcInt != -1) {
                    Notice = Notice.substring(0, srcInt);
                }
                final String Notice2 = Notice;
                Bitmap[] bm = new Bitmap[10];


                //imag src 유무 검사
                try {
                    if (!imageList.isEmpty()) {
                        URL[] imageUrl = new URL[10];
                        for (int i = 0; i < imageList.size(); i++) {//image view 동적추가
                            imageUrl[i] = new URL("http://knu.ac.kr" + imageList.get(i));
                            InputStream is = imageUrl[i].openStream();
                            bm[i] = BitmapFactory.decodeStream(is);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                final Bitmap[] finalBm = new Bitmap[10];
                for (int i = 0; i < 10; i++) {
                    finalBm[i] = bm[i];
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        title.setText("제목: " + Notice_title + "\n" + "작성자: " + Notice_writer + "/" + "게시일: " + Notice_date);
                        //content.setText(Html.fromHtml(Notice));
                        //attach_name_list.size() ->첨부된 파일 개수
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.attach_item, R.id.attach_name, attach_name_list);
                        attached_listview.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(attached_listview);

                        content.setText(Html.fromHtml(Notice2));
                        content.setMovementMethod(LinkMovementMethod.getInstance());
                        for (int i = 0; i < 10; i++) {
                            if (finalBm[i] != null) {
                                ImageView imgView = new ImageView(getApplicationContext());
                                imgView.setImageBitmap(finalBm[i]);
                                photo = new PhotoViewAttacher(imgView);
                                linearLayout.addView(imgView, i);
                            }
                        }
                        progressDialog.dismiss(); //모든 작업이 끝나면 다이어로그 종료
                    }
                }, 0);


            }

        }.start(); //thread end


    }

    public void writeFile(InputStream is, OutputStream os) throws IOException {
        int c = 0;
        while ((c = is.read()) != -1)
            os.write(c);
        os.flush();
    }

    private boolean isInternetCon() {
        cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); //모바일 데이터 여부
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //와이파이 여부
        return !mobile.isConnected() && !wifi.isConnected(); //결과값을 리턴
    }

    public static List<String> getImgSrc(String str) {
        Pattern nonValidPattern = Pattern
                .compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");

        List<String> result = new ArrayList<String>();
        Matcher matcher = nonValidPattern.matcher(str);
        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(completeReceiver, completeFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(completeReceiver);
    }

    private BroadcastReceiver completeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("다운로드가 완료되었습니다.");
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
            PendingIntent pending = PendingIntent.getActivity(context,0,target,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pending);
            nManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManger.notify(123456, builder.build());

        }

    };

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
