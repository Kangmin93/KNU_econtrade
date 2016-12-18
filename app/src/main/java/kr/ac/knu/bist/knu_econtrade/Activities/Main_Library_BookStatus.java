package kr.ac.knu.bist.knu_econtrade.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by Vertx on 2016-09-10.
 */
public class Main_Library_BookStatus extends AppCompatActivity{

    private EditText book_search_name;
    private Button book_search_button;
    private ListView book_list;
    private String book_search_name_get;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_library_book);

        book_search_button = (Button)findViewById(R.id.library_search);
        book_search_name=(EditText)findViewById(R.id.book_search_name);
        book_list = (ListView)findViewById(R.id.listView);

        /*                                                  선언                              */
        book_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                book_search_name_get = book_search_name.getText().toString();
                if(book_search_name_get.equals("")){
                    Toast.makeText(getApplicationContext(),"도서 이름을 입력하세요",Toast.LENGTH_SHORT).show();
                }else{

                }

            }
        });


    }

    private void bookSearch(String book_name) {

        //학교 도서관 홈페이지에 book name을 검색 인자로 request를 날려 결과를 받아오고 listview에 뿌려준다.


    }
}
