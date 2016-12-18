package kr.ac.knu.bist.knu_econtrade.gradeComponents;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import kr.ac.knu.bist.knu_econtrade.R;
import kr.ac.knu.bist.knu_econtrade.Session.ConnManager;


/**
 * Created by liliilli
 */
public class fragmentGradeIssu extends Fragment {
    private View activityView;
    private ProgressDialog progressDialog;

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recylerViewContainer;
    private adapterGradeItem mAdapter;
    private ArrayList<listgradeItem> listChildItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activityView = inflater.inflate(R.layout.fragment_grade, container, false);
        setDesignComponents();
        return activityView;
    }

    @Override
    public void onResume() {
        super.onResume();
        recylerViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getItemlistFromWebpage();
    }

    // CUSTOM METHODS
    private void setDesignComponents() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);

        recylerViewContainer = (RecyclerView) activityView.findViewById(R.id.grade_board_list);
        mAdapter = new adapterGradeItem(this.getContext(), listChildItems);
        recylerViewContainer.setLayoutManager(linearLayoutManager);
        recylerViewContainer.setHasFixedSize(true);
        recylerViewContainer.setAdapter(mAdapter);

    }

    private void getItemlistFromWebpage() {
        processRetrieveItems();
        recylerViewContainer.getAdapter().notifyDataSetChanged();
    }

    private void processRetrieveItems() {
        ConnManager manager = new ConnManager();
        try {
            String ret = manager.execute(ConnManager.main_url+ConnManager.record_url,"certRecEnq.recDiv","1","id","certRecEnqGrid","columnsProperty","certRecEnqColumns","rowsProperty",
                    "certRecEnqs","emptyMessageProperty","certRecEnqNotFoundMessage","viewColumn","yr_trm,subj_div_cde,subj_cde,subj_nm,unit,rec_rank_cde","checkable","false",
                    "showRowNumber","false","paged","false","serverSortingYn","false","lastColumnNoRender","false","_","").get();

            Source source = new Source(ret);
            source.fullSequentialParse();
            Element tr = source.getAllElements(HTMLElementName.DIV).get(1);
            List<Element> elements = tr.getAllElements(HTMLElementName.TR);
            for(int i = 1; i < elements.size(); i++) {
                Element e = elements.get(i);
                List<Element> elements_sub = e.getAllElements();
                Integer itemYear = new Integer(elements_sub.get(1).getTextExtractor().toString().substring(0,4));
                String itemSemester = elements_sub.get(1).getTextExtractor().toString().substring(5);
                String itemName = elements_sub.get(4).getTextExtractor().toString();
                String itemSort = elements_sub.get(2).getTextExtractor().toString();
                String itemCode = elements_sub.get(3).getTextExtractor().toString();
                Integer itemUnit;
                if(elements_sub.get(5).getTextExtractor().toString() != "") {
                    itemUnit = new Integer(elements_sub.get(5).getTextExtractor().toString());
                } else {
                    itemUnit = 0;
                }
                String itemRank = elements_sub.get(6).getTextExtractor().toString();
                listChildItems.add(new listgradeItem(itemYear,itemSemester,itemName,itemSort,itemCode,itemUnit,itemRank));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
