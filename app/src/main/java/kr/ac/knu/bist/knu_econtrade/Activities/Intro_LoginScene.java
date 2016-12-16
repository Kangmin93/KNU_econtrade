package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.htmlparser.jericho.Source;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.knu.bist.knu_econtrade.R;
import kr.ac.knu.bist.knu_econtrade.Session.ConnManager;

/**
 * Created by neu on 16. 8. 29.
 * <p>
 * Intro_LoginScene
 * This activity is about login page of this application.
 * <p>
 * TODO : Add a parse syntax in 79~88 line onto this page.
 */

public class Intro_LoginScene extends Activity {

    AlertDialog OutApp_Dialog = null;

    EditText Text_Number;
    EditText Text_ID;
    EditText Text_UserPasswd;
    Button Button_Login;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_login);

        Text_Number = (EditText) findViewById(R.id.login_number);
        Text_ID = (EditText) findViewById(R.id.login_id);
        Text_UserPasswd = (EditText) findViewById(R.id.login_password);
        Button_Login = (Button) findViewById(R.id.login_button_login);

        final AlertDialog.Builder OutApp_Builder = new AlertDialog.Builder(Intro_LoginScene.this);

        OutApp_Builder.setMessage("\'ECON & TRADE\'를 종료하시겠습니까?");
        OutApp_Builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface inp_dialog, int inp_which) {
            }
        });
        OutApp_Builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface inp_dialog, int inp_which) {
                inp_dialog.dismiss();
                finish();
            }
        });
        OutApp_Dialog = OutApp_Builder.create();

        /**
         *  Button_Login -----*-----*-----*-----*-----*----
         *  로그인을 하기 위한 버튼.
         *
         *  누르면 Check_Login_Template() 로 로그인에 필요한 기본 양식들을 체크하며
         *  TRUE 반환시
         *  로그인에 성공한 유저 정보를 위한 저장소와,
         *  서버에서 PHP를 받기 위한 GetPHP 객체를 생성한다.
         *  GetPHP.LoadUserInfoAccess 로 Context 와 유저ID (조회용) 을 보낸다.
         *
         *  1) 아이디와 비밀번호란이 다 채워졌는지 체크한다.
         *  2) Intro_LoginRequest 로 아이디 및 비밀번호를 조회한다
         *  3) User_InfoInstance 저장소를 생성한다.
         *  4) GetPHP.LoadUserInfoAccess 로 해당 로그인 유저의 정보를 가져온다.
         */
        Button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 로그인하기 이전에 로그인 양식 체크를 실시한다.
                 *  @return boolean */
//              boolean Login_Enter = Check_Login_Template();

                String User_outputNB = Text_Number.getText().toString();
                String User_outputID = Text_ID.getText().toString();
                String User_outputPW = Text_UserPasswd.getText().toString();

                Login(v, User_outputID,User_outputPW,User_outputNB);
            }
        });
    }

    private boolean Check_Login_Template() {
        Matcher Match_Number ,Match_ID, Match_Passwd;
        Pattern Pattern_Number, Pattern_ID, Pattern_Passwd;

        Pattern_Number = Pattern.compile("^[a-zA-Z]+$");
        Pattern_ID = Pattern.compile("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$");
        Pattern_Passwd = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐]+$");

        Match_Number    = Pattern_Number.matcher(Text_Number.getText().toString());
        Match_ID        = Pattern_ID.matcher(Text_ID.getText().toString());
        Match_Passwd    = Pattern_Passwd.matcher(Text_UserPasswd.getText().toString());

        if (Text_Number.getText().toString().length() != 11) {
            Toast.makeText(getApplicationContext(), "학생번호가 제대로 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        else if (Text_ID.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "이메일이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        else if (Text_UserPasswd.getText().toString().length() == 0)
            Toast.makeText(getApplicationContext(), "패스워드가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        else if (Match_Number.find())
            Toast.makeText(getApplicationContext(), "학생번호에는 숫자만 기입해 주세요.", Toast.LENGTH_SHORT).show();
        else if (!Match_ID.find())
            Toast.makeText(getApplicationContext(), "유효한 이메일 형식을 입력해주세요.", Toast.LENGTH_SHORT).show();
        else if (!Match_Passwd.find())
            Toast.makeText(getApplicationContext(), "비밀번호에 특수문자는 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        else
            return true;

        return false;
    }

    private boolean Login(View v, String id, String pw, String num) {
        ConnManager manager = new ConnManager();
        String ret = null;
        try {
            ret = manager.execute(ConnManager.main_url + ConnManager.login_url, "user.usr_id",id,"user.passwd",pw,"user.user_div","20","user.stu_persnl_nbr",num).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Source source = new Source(ret);
        source.fullSequentialParse();
        if(source.getElementById("login")==null) {
            Intent intent = new Intent(getApplicationContext(),Main_MainScene.class);
            startActivity(intent);
            finish();
        } else {
            Snackbar.make(v,"Login Failed",Snackbar.LENGTH_LONG).show();
        }
        return true;
    }
}
