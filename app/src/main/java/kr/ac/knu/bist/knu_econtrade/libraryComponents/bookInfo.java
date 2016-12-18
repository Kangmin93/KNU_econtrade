package kr.ac.knu.bist.knu_econtrade.libraryComponents;

/**
 * Created by stype on 2016-12-19.
 */

public class bookInfo {
    private String mTitle;
    private String mAuthor;
    private String mAvailable;

    public bookInfo()  {
    }

    public bookInfo(String mTitle, String mAuthor, String mAvailable)  {
        this.setmTitle(mTitle);
        this.setmAuthor(mAuthor);
        this.setmAvailable(mAvailable);
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmAvailable() {
        return mAvailable;
    }

    public void setmAvailable(String mAvailable) {
        this.mAvailable = mAvailable;
    }
}
