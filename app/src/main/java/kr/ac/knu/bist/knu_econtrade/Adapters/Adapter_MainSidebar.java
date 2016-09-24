package kr.ac.knu.bist.knu_econtrade.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by Vertx on 2016-08-04.
 *
 */

public class Adapter_MainSidebar extends BaseExpandableListAdapter {
    public Context Object_Context;

    private ArrayList<String> List_Group = new ArrayList<>();
    private HashMap<String, ArrayList<String>> List_Child;

    public Adapter_MainSidebar(Context context, ArrayList<String> arrayGroup,
                               HashMap<String, ArrayList<String>> arrayChild) {
        super();
        this.Object_Context = context;
        this.List_Group = arrayGroup;
        this.List_Child = arrayChild;
    }

    @Override
    public int getGroupCount() { return List_Group.size(); }

    @Override
    public int getChildrenCount(int groupPosition) {
        return List_Child.get(List_Group.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition ) {
        return List_Group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition ) {
        return List_Child.get(List_Group.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId( int groupPosition ) { return groupPosition; }

    @Override
    public long getChildId( int groupPosition, int childPosition ) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent ) {
        String groupName = List_Group.get(groupPosition);
        View v = convertView;

        if ( v == null ) {
            LayoutInflater inflater =
                    (LayoutInflater) Object_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (LinearLayout)inflater.inflate( R.layout.listitem_main_group, null);
        }
        TextView View_Subject = (TextView) v.findViewById(R.id.group_menutext);
        View_Subject.setText( groupName );

        Log.d("getGroupView","Produce Group");
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childName = List_Child.get(List_Group.get(groupPosition)).get(childPosition);
        View v = convertView;

        if ( v == null ) {
            LayoutInflater inflater =
                    (LayoutInflater) Object_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (LinearLayout)inflater.inflate( R.layout.listitem_main_children, null);
        }
        TextView View_Child = (TextView) v.findViewById(R.id.child_menutext);
        View_Child.setText( childName );

        Log.d("getChildView","Produce Child");
        return v;
    }

    @Override
    public boolean isChildSelectable( int groupPosition, int childPosition ) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
