package kr.ac.knu.bist.knu_econtrade.gradeComponents;

/**
 * Created by Vertx on 2016-08-04.
 */
public class listGradeItem {
    private Integer itemYear;
    private String itemSemester;

    private String itemName;
    private String itemSort;
    private String itemCode;
    private Integer itemUnit;
    private String itemRank;

    public listGradeItem() {}

    public listGradeItem(Integer itemYear, String itemSemester,
                         String itemName, String itemSort,
                         String itemCode, Integer itemUnit, String itemRank) {
        this.itemYear = itemYear;
        this.itemSemester = itemSemester;
        this.itemName = itemName;
        this.itemSort = itemSort;
        this.itemUnit = itemUnit;
        this.itemRank = itemRank;
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSort() {
        return itemSort;
    }

    public void setItemSort(String itemSort) {
        this.itemSort = itemSort;
    }

    public Integer getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(Integer itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemRank() {
        return itemRank;
    }

    public void setItemRank(String itemRank) {
        this.itemRank = itemRank;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
