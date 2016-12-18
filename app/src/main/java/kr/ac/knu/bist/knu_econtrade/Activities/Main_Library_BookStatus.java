package kr.ac.knu.bist.knu_econtrade.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by Vertx on 2016-09-10.
 */
public class Main_Library_BookStatus extends AppCompatActivity{

    private EditText book_search_name;
    private Button book_search_button;
    private ListView book_list;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_library_book);

        book_search_button = (Button)findViewById(R.id.library_search);
        book_search_name=(EditText)findViewById(R.id.book_search_name);
        book_list = (ListView)findViewById(R.id.listView);

        /*                                                  선언                              */



    }
}
