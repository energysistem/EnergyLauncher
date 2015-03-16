package com.energysistem.energylauncher.tvboxlauncher.util;

import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;

import java.util.Comparator;

public class SortBasedOnName implements Comparator
{
    public int compare(Object o1, Object o2)
    {

        AppInfo dd1 = (AppInfo)o1;// where FBFriends_Obj is your object class
        AppInfo dd2 = (AppInfo)o2;

        return dd1.getTitle().compareToIgnoreCase(dd2.getTitle());//where uname is field name
    }

}