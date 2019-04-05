package com.example.kk.wedontchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


;

public class TabsAccessorAdapter extends FragmentPagerAdapter
{
String username;
String user;
String password;
    private chatFragment chatsFragment;

    private ContactsFragment contactsFragment;

    public TabsAccessorAdapter(FragmentManager fm,String username,String user,String password) {
        super(fm);
this.username=username;
this.user=user;
this.password=password;

    }

    @Override
    public Fragment getItem(int i) {

        switch(i)
        {
            case 0:
                chatsFragment = new chatFragment();
                Bundle bun=new Bundle();
                bun.putString("user",user);
                bun.putString("password",password);
                chatsFragment.setArguments(bun);
                 return chatsFragment;
            case 2:
                SettingsFragment settingsFragment = new SettingsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                settingsFragment.setArguments(bundle);

return settingsFragment;
            case 1:
                contactsFragment = new ContactsFragment();
return contactsFragment;
                default:

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0:

                return "微信";
            case 2:
               return "我";
            case 1:
                return "聊天界面";
            default:

        }
        return super.getPageTitle(position);
    }
}
