package kr.ac.knu.bist.knu_econtrade.Informations;

/**
 * Created by dykim on 2016-09-09.
 * 2016-10-28 : liliilli Info_ListData 이름을 Info_ListData 로 수정함
 */
public class Info_ListData { // 데이터를 받는 클래스

    public String mType;
    public String mTitle;
    public String mUrl;
    public String mWriter;
    public String mDate;


    public Info_ListData()  {


    }

    public Info_ListData(String mType, String mTitle, String mUrl, String mWriter, String mDate)  { //데이터를 받는 클래스 메서드
        this.mType = mType;
        this.mTitle = mTitle;
        this.mUrl = mUrl;
        this.mWriter = mWriter;
        this.mDate = mDate;

    }

}
// 리스트 적용부분 >
