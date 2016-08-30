package kr.ac.knu.bist.knu_econtrade.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by neu on 16. 8. 29.
 *
 * Scene_Login
 * This activity is about login page of this application.
 *
 * TODO : Add a parse syntax in 79~88 line onto this page.
 */

public class Scene_Login extends Activity {
    EditText Text_UserNumber, Text_UserID, Text_UserPasswd;
    Button   Btn_Login;

    AlertDialog OutApp_Dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_login);

        Text_UserNumber = (EditText)findViewById(R.id.login_number);
        Text_UserID     = (EditText)findViewById(R.id.login_id);
        Text_UserPasswd = (EditText)findViewById(R.id.login_password);
        Btn_Login       = (Button)findViewById(R.id.login_button);

        final AlertDialog.Builder OutApp_Builder = new AlertDialog.Builder(Scene_Login.this);
        OutApp_Builder.setMessage("\'ECON & TRADE\'를 종료하시겠습니까?");
        OutApp_Builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface inp_dialog, int inp_which) {
            }
        });
        OutApp_Builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface inp_dialog, int inp_which) {
                inp_dialog.dismiss(); finish();
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
        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 로그인하기 이전에 로그인 양식 체크를 실시한다.*/
                if (Check_Login_Template()) {
                    String User_outputID = Text_UserID.getText().toString();
                    String User_outputPW = Text_UserPasswd.getText().toString();

/*                    // ID 와 패스워드를 서버에서 대조 시킨다.
                    Login = new Intro_LoginRequest(User_outputID, User_outputPW);

                    // 서버에서 대조한 결과가 맞으면 (ID 및 pw 가 맞아떨어짐)
                    if (Login_Enter = Login.LoginAccess())  {
                        // 유저 정보를 저장하기 위한 저장객체를 생성하고
                        // 이 객체에 정보를 옮기기 위해 현재 로그인하고 있는 유저의 ID 를
                        // LoadUserInfoAccess 에 넣어 실행시킨다.
                        User_InfoInstance = Repo_UserInformSGT.getInstance();
                        GetPHP.LoadUserInfoAccess(User_outputID);*/
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }

    private boolean Check_Login_Template() {
        Matcher Match_ID, Match_Number, Match_Passwd;
        Pattern Pattern_ID, Pattern_Number, Pattern_Passwd;
        Pattern_Number = Pattern.compile("^[0-9]+$");
        Pattern_ID = Pattern.compile("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$");
        Pattern_Passwd = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-흐]+$");

        Match_Number    = Pattern_Number.matcher(Text_UserNumber.getText().toString());
        Match_ID        = Pattern_ID.matcher(Text_UserID.getText().toString());
        Match_Passwd    = Pattern_Passwd.matcher(Text_UserPasswd.getText().toString());

        if(Text_UserNumber.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "학번이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();}
        else if (Text_UserID.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "이메일이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();}
        else if (Text_UserPasswd.getText().toString().length() == 0)
            Toast.makeText(getApplicationContext(), "패스워드가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        else if (!Match_Number.find())
            Toast.makeText(getApplicationContext(), "유효한 학번을 입력해 주십시오.", Toast.LENGTH_SHORT).show();
        else if (!Match_ID.find())
            Toast.makeText(getApplicationContext(), "유효한 이메일 형식을 입력해주세요.", Toast.LENGTH_SHORT).show();
        else if (!Match_Passwd.find())
            Toast.makeText(getApplicationContext(), "비밀번호에 특수문자는 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        else
            return true;

        return false;
    }
}
