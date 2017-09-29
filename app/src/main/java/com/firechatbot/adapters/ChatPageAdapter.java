package com.firechatbot.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.firechatbot.fragments.ChatFragment;
import com.firechatbot.fragments.ContactsFragment;
import com.firechatbot.utils.AppConstants;

public class ChatPageAdapter extends FragmentPagerAdapter{

    public ChatPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new ContactsFragment();
            case 1:
                return new ChatFragment();
        }
        return new ChatFragment();
    }

    @Override
    public int getCount() {
        return AppConstants.TABS_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1)
            return "Chat";
        return "Contacts";
    }
}
