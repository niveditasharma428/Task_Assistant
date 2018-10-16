package com.example.admin.task_assistant.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.task_assistant.fragment.FragAddGroupUser;
import com.example.admin.task_assistant.fragment.FragAssignGroupTask;
import com.example.admin.task_assistant.fragment.FragGroup;
import com.example.admin.task_assistant.fragment.FragMyGroup;
import com.example.admin.task_assistant.fragment.FragMyGroupTask;
import com.example.admin.task_assistant.fragment.FragViewTask;

public class GroupFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    String source, groupmember, groupname,al;
    Bundle bundle;

   /* private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();*/

    public GroupFragmentAdapter(Context context, FragmentManager fm, String source, String groupmember, String groupname, String al) {
        super(fm);
        mContext = context;
        this.source = source;
        this.groupname = groupname;
        this.groupmember = groupmember;
        this.al=al;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        bundle = new Bundle();
        bundle.putString("groupmembers", groupmember);
        bundle.putString("groupname", groupname);
        bundle.putString("al",al);

        System.out.println("DivyaGroupmember:-" + groupmember + "--" + groupname);

        System.out.println("DivyaSource:-" + source);
        if (position == 0 && source.equalsIgnoreCase("group")) {
            fragment = new FragGroup();
        } else if (position == 0 && source.equalsIgnoreCase("task")) {
            fragment = new FragViewTask();
            fragment.setArguments(bundle);
        } else if (position == 1 && source.equalsIgnoreCase("group")) {
            fragment = new FragAddGroupUser();
        } else if (position == 1 && source.equalsIgnoreCase("task")) {
            fragment = new FragAssignGroupTask();
            fragment.setArguments(bundle);
        } else if (position == 2 && source.equalsIgnoreCase("group")) {
            fragment = new FragMyGroup();
            fragment.setArguments(bundle);
        }
       else if (position == 2 && source.equalsIgnoreCase("task")) {
            fragment = new FragMyGroupTask();
            fragment.setArguments(bundle);
        }
        return fragment;
       // return mFragmentList.get(position);
    }

    @Override
    public int getCount() {

       /* System.out.println("DivyaTabCounts:-"+mFragmentList.size());
        return mFragmentList.size();*/
       return  3;
    }

  /*  @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
*/

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0 && source.equalsIgnoreCase("group")) {
            title = "Created Groups";
        } else if (position == 0 && source.equalsIgnoreCase("task")) {
            title = "View Task";
        } else if (position == 1 && source.equalsIgnoreCase("group")) {
            title = "Create Groups";
        } else if (position == 1 && source.equalsIgnoreCase("task")) {
            title = "Assign Task";
        } else if (position == 2 && source.equalsIgnoreCase("group")) {
            title = "My Groups";
        } else if (position == 2 && source.equalsIgnoreCase("task")) {
            title = "My Group Tasks";
        }

        return title;
    }


   /* public void addFragment(Fragment fragment, String title, int position) {
        mFragmentList.add(position, fragment);
        mFragmentTitleList.add(position, title);
    }

    public void removeFragment(Fragment fragment, int position) {
        mFragmentList.remove(position);
        mFragmentTitleList.remove(position);
    }*/
}
