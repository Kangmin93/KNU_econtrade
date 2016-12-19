package kr.ac.knu.bist.knu_econtrade.noticeComponents;

/**
 * Created by dykim on 2016-12-18.
 */
public class SeatListData {

    private String name;
    private String percentage;
    private String number;

    public SeatListData(String mName, String mPercentage, String mNumber){
        this.setName(mName);
        this.setNumber(mNumber);
        this.setPercentage(mPercentage);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPercentage() {

        return percentage;
    }

    public String getNumber() {
        return number;
    }
}
