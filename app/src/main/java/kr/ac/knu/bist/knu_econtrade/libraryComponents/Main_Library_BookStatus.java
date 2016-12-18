package kr.ac.knu.bist.knu_econtrade.libraryComponents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import kr.ac.knu.bist.knu_econtrade.R;
import kr.ac.knu.bist.knu_econtrade.Session.LibrarySearcher;
import kr.ac.knu.bist.knu_econtrade.gradeComponents.adapterGradeItem;
import kr.ac.knu.bist.knu_econtrade.noticeComponents.Info_ListData;
import kr.ac.knu.bist.knu_econtrade.noticeComponents.adapterNoticeinfoItem;

/**
 * Created by Vertx on 2016-09-10.
 */
public class Main_Library_BookStatus extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recylerViewContainer;
    private adapterBookinfo mAdapter;
    ArrayList<bookInfo> mListData = new ArrayList<>();

    private EditText book_search_name;
    private Button book_search_button;
    private String book_search_name_get;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_library_book);

        book_search_button = (Button) findViewById(R.id.library_search);
        book_search_name = (EditText) findViewById(R.id.book_search_name);
        recylerViewContainer = (RecyclerView) findViewById(R.id.book_list);
        mAdapter = new adapterBookinfo(getApplicationContext(),mListData);
        layoutManager = new LinearLayoutManager(this);
        recylerViewContainer.setLayoutManager(layoutManager);
        recylerViewContainer.setAdapter(mAdapter);

        book_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                book_search_name_get = book_search_name.getText().toString();
                if (book_search_name_get.equals("")) {
                    Toast.makeText(getApplicationContext(), "도서 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray array = getBookList(book_search_name_get,10,0);
                    try {
                        if(array != null) {
                            mListData.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                mListData.add(new bookInfo(object.getString("titleStatement"), object.getString("author"), "대출가능"));
                            }
                        }
                        else
                            Log.d("author","failed");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                recylerViewContainer.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public JSONArray getBookList(String bookname, int max, int offset) {
        LibrarySearcher lib = new LibrarySearcher();
        String raw = "";
        JSONObject object = null;
        JSONArray ret = null;
        try {
            raw = lib.execute(LibrarySearcher.kudos_search, "all", "l|a|"+ bookname,"max",max+"","offset",offset+"").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            object = new JSONObject(raw);
            ret = object.getJSONObject("data").getJSONArray("list");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
