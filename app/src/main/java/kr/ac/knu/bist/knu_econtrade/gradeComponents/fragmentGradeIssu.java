package kr.ac.knu.bist.knu_econtrade.gradeComponents;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import kr.ac.knu.bist.knu_econtrade.Activities.Main_UnivNot_MainScene;
import kr.ac.knu.bist.knu_econtrade.R;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * Created by liliilli
 */
public class fragmentGradeIssu extends Fragment {

    private static String URL_PRIMARY = "http://my.knu.ac.kr/stpo/stpo/scor/certRecEnq/list.action";
    private String url;
    private java.net.URL URL;
    private int foundBBSindex;

    private ConnectivityManager cManager;
    private NetworkInfo mobile;
    private NetworkInfo wifi;

    private View activityView;
    private View emptyView;
    private Source source;
    private ProgressDialog progressDialog;

    private RecyclerView recylerViewContainer;
    private adapterGradeItem mAdapter;
    private ArrayList<listGradeItem> listChildItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activityView = inflater.inflate(R.layout.fragment_grade, container, false);
        setDesignComponents();
        return activityView;
    }
    @Override
    public void onResume() {
        super.onResume();
        recylerViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getItemlistFromWebpage();
    }

    // CUSTOM METHODS
    private void setDesignComponents() {
        recylerViewContainer = (RecyclerView) activityView.findViewById(R.id.grade_board_list);
        mAdapter = new adapterGradeItem(this.getContext(), listChildItems);
        recylerViewContainer.setAdapter(mAdapter);

        emptyView = activityView.findViewById(R.id.emptyview);
        setRecycleViewIsEmpty();
    }

    private void setRecycleViewIsEmpty() {
        if (listChildItems.isEmpty()) {
            recylerViewContainer.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recylerViewContainer.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void getItemlistFromWebpage() {
        if (isInternetCon() == true) {
            Toast.makeText(getActivity(), "인터넷에 연결되지않아 불러오기를 중단합니다.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            try {
                processRetrieveItems();
                recylerViewContainer.getAdapter().notifyDataSetChanged();
            }
            catch (Exception e) {
                Log.d("ERROR", e + "");

            }
        }
    }

    private boolean isInternetCon() {
        cManager=(ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cManager.getActiveNetworkInfo();

        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { //와이파이 여부
                return false;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) { //모바일 데이터 여부
                return false;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET)
                return false;
        } else {
            // not connected to the internet
        }
        return true;
    }

    private void processRetrieveItems() throws IOException {

        new Thread() {
            @Override
            public void run() {
                super.run();
                android.os.Handler Progress = new android.os.Handler(Looper.getMainLooper());
                Progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        progressDialog =
//                                ProgressDialog.show(getContext(), "", "정보를 가져오는 중입니다");
                    }
                }, 0);

                try {
                    url = URL_PRIMARY;
                    URL = new URL(url);
                    InputStream inputHTML = URL.openStream();
                    source = new Source(new InputStreamReader(inputHTML, "utf-8"));
                    source.fullSequentialParse();
                }
                catch (Exception e) {
                    Log.d("ERROR", e + "");
                }

                List<StartTag> retrieveTableTags = source.getAllStartTags(HTMLElementName.DIV);
                for(int index = 0; index < retrieveTableTags.size(); index++) {
                    if(retrieveTableTags.get(index).toString().equals("<div class=\"con_area\">")) {
                        foundBBSindex = index;
                        Log.d("BBSLOCATES", index+"");
                        break;
                    }
                }

                Element BBSDiv = source.getAllElements(HTMLElementName.DIV).get(foundBBSindex);
                Element BBSTable = BBSDiv.getAllElements(HTMLElementName.TABLE).get(0); //listCertRetEnqs
                Element BBSTBody = BBSTable.getAllElements(HTMLElementName.TBODY).get(0);

                for(int C_TR = 0; C_TR < BBSTBody.getAllElements(HTMLElementName.TR).size(); C_TR++) {
                    try {
                        Element BBStrConnected = (Element) BBSTBody.getAllElements(HTMLElementName.TR).get(C_TR);

                        Element BBSYeaRnSem = BBStrConnected.getAllElements(HTMLElementName.TD).get(0);
                        Element BBSSort = BBStrConnected.getAllElements(HTMLElementName.TD).get(1);
                        Element BBSCode = BBStrConnected.getAllElements(HTMLElementName.TD).get(2);
                        Element BBSName = BBStrConnected.getAllElements(HTMLElementName.TD).get(3);
                        Element BBSUnit = BBStrConnected.getAllElements(HTMLElementName.TD).get(4);
                        Element BBSRank = BBStrConnected.getAllElements(HTMLElementName.TD).get(5);

                        Integer itemYear = Integer.valueOf(BBSYeaRnSem.getContent().toString().substring(0, 3));
                        Integer itemSem = Integer.valueOf(BBSYeaRnSem.getContent().toString().substring(4));
                        String  itemSort = BBSSort.getContent().toString();
                        String  itemName = BBSName.getContent().toString();
                        String  itemCode = BBSCode.getContent().toString();
                        Integer itemUnit = Integer.valueOf(BBSUnit.getContent().toString());
                        String  itemRank = BBSRank.getContent().toString();

                        listChildItems.add(new listGradeItem(itemYear, itemSem,
                                itemName, itemSort,
                                itemCode, itemUnit, itemRank));
                    }
                    catch (Exception e) {
                        Log.d("BCSERROR", e + "");
                    }
                }

                android.os.Handler postHandler = new android.os.Handler(Looper.getMainLooper());
                postHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recylerViewContainer.getAdapter().notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }, 0);
            }


        }.start();
    }

//    private void doHideAllKeyboards() {
//        doHideKeyboard(getContext(), Text_Value);
//        doHideKeyboard(getContext(), Text_Description);
//    }
//    private void doHideKeyboard(Context context, TextView textview) {
//        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        mInputMethodManager.hideSoftInputFromWindow(textview.getWindowToken(), 0);
//    }
//    private void doChangeFragmentIntoDaiyFrag() {
//        doRemoteFragmentRefresh();
//        makeConfirmSnackbar();
//    }
//    private void doRemoteFragmentRefresh() {
//        ((mainGradeActivity)getActivity()).setCurrentItem(1, true);
//        ((mainGradeActivity)getActivity()).getmViewPager().getAdapter().notifyDataSetChanged();
//    }
//    private void makeConfirmSnackbar() {
//        try {
//            Snackbar.make(getView(), "추가되었습니다", Snackbar.LENGTH_LONG).setAction("취소하기", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    deleteRecentItem();
//                }
//            }).show();
//        }
//        catch (Exception e) {
//            Log.e("WTF", "Snackbar missing null object reference : " + e.toString());
//        }
//    }
//    private void deleteRecentItem() {
//        if (checkIsRecentItemDeleted()) {
//            ((mainGradeActivity)getActivity()).setCurrentItem(0, true);
//            Snackbar.make(getView(), "취소되었습니다", Snackbar.LENGTH_SHORT).show();
//        } else {
//            Snackbar.make(getView(), "다시 시도해보세요.", Snackbar.LENGTH_SHORT).setAction("RETRY", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    deleteRecentItem();
//                }
//            }).show();
//        }
//
//    }
//    private boolean checkIsRecentItemDeleted() {
//        return false;
//    }
}
