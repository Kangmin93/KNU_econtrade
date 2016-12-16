package kr.ac.knu.bist.knu_econtrade.noticeComponents;

/**
 * Created by dykim on 2016-09-09.
 * 2016-10-28 : liliilli Info_ListData 이름을 Info_ListData 로 수정함
 */
public class Info_ListData { // 데이터를 받는 클래스

    private String mType;
    private String mTitle;
    private String mUrl;
    private String mWriter;
    private String mDate;


    public Info_ListData()  {
    }

    public Info_ListData(String mType, String mTitle, String mUrl, String mWriter, String mDate)  { //데이터를 받는 클래스 메서드
        this.setmType(mType);
        this.setmTitle(mTitle);
        this.setmUrl(mUrl);
        this.setmWriter(mWriter);
        this.setmDate(mDate);

    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmWriter() {
        return mWriter;
    }

    public void setmWriter(String mWriter) {
        this.mWriter = mWriter;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
// 리스트 적용부분 >
