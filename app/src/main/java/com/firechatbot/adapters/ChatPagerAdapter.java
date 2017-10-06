package com.firechatbot.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.firechatbot.fragments.ChatFragment;
import com.firechatbot.fragments.ContactsFragment;
import com.firechatbot.fragments.SettingsFragment;
import com.firechatbot.utils.AppConstants;

public class ChatPagerAdapter extends FragmentStatePagerAdapter{

    public ChatPagerAdapter(FragmentManager fm) {
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
            case 2:
                return new SettingsFragment();
        }
        return new ChatFragment();
    }

    @Override
    public int getCount() {
        return AppConstants.TABS_COUNT;
    }


}
