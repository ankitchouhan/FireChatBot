package com.firechatbot.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.activities.MainActivity;
import com.firechatbot.adapters.ContactAdapter;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;
import com.firechatbot.interfaces.OnContactsReceived;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener, OnContactsReceived, TextWatcher {

    private TextView profileImageTv;
    private TextView nameTv, phoneTv;
    private RecyclerView recyclerViewRv;
    private ContactAdapter mContactAdapter;
    private List<UserDetailBean> mContactList;
    private List<ContactBean> mList;
    private List<ContactBean> mDbList;
    private Activity mActivity;
    private TextView toolbarHeadingTv;
    private ImageView toolbarAddIv;
    private EditText searchEt;
   // private NestedScrollView scrollViewSv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        initViews(view);
        initVariables();
        return view;
    }

    /**
     * Method to initialize views.
     */
    private void initViews(View view) {
        nameTv = view.findViewById(R.id.tv_name);
        phoneTv = view.findViewById(R.id.tv_phone);
        profileImageTv = view.findViewById(R.id.tv_fragment_profile_icon);
        recyclerViewRv = view.findViewById(R.id.rv_contacts);
        view.findViewById(R.id.rl_search_parent).setOnClickListener(this);
        Toolbar toolbarTb = view.findViewById(R.id.tb_toolbar);
        //scrollViewSv = view.findViewById(R.id.nsv_scroll);
        //SearchView searchView = view.findViewById(R.id.sv_search);
        // searchView.setOnQueryTextListener(this);
        //searchView.setQueryHint(getString(R.string.search_hint));
        searchEt = view.findViewById(R.id.et_search);
        //RelativeLayout searchParent = view.findViewById(R.id.rl_search_parent);
        searchEt.addTextChangedListener(this);
        mActivity = getActivity();
        ((AppCompatActivity) mActivity).setSupportActionBar(toolbarTb);
        if (toolbarTb != null) {
            toolbarHeadingTv = toolbarTb.findViewById(R.id.tv_heading);
            toolbarAddIv = toolbarTb.findViewById(R.id.iv_add);
        }
        toolbarForContacts();
    }

    /**
     * Method to initialize variables.
     */
    private void initVariables() {
        mList = new ArrayList<>();
        mDbList = new ArrayList<>();
       /* if (((MainActivity) mActivity).contactDetails() != null) {
            mList.clear();
            mList.addAll(((MainActivity) mActivity).contactDetails());
        }*/
           /* mList.clear();
            mList.addAll(getContacts());
            mContactAdapter.notifyDataSetChanged();*/
        mContactAdapter = new ContactAdapter(mActivity, mList);
        recyclerViewRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewRv.setAdapter(mContactAdapter);
        /*scrollViewSv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                float y = recyclerViewRv.getChildAt(0).getY();
                scrollViewSv.scrollTo(1, (int) y);
            }
        });*/
    }

    @Override
    public void syncContacts(List<ContactBean> list) {
        mList.clear();
        mList.addAll(list);
        mContactAdapter.notifyDataSetChanged();
        //filterContacts();
    }

    /**
     * Method to get contacts details
     */
    /*private List<ContactBean> getContacts() {
        List<ContactBean> list = new ArrayList<>();
        Cursor cursor = mActivity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ContactBean contactsBean = new ContactBean();
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    contactsBean.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    Cursor cr = mActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?", new String[]{id}, null);
                    if (cr != null && cr.moveToFirst()) {
                        contactsBean.setPhone(cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        cr.close();
                    }
                }
                if (contactsBean.getName() != null && contactsBean.getPhone() != null)
                    list.add(contactsBean);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }*/


    /**
     * Method to display contact image.
     */
    private String setContactImage(String name) {
        String[] contact = name.split(" ");
        String displayName = "";
        for (String data : contact) {
            displayName = displayName + data.charAt(0);
        }
        return displayName;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // mContactAdapter.getFilter().filter(newText.toLowerCase().trim());
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_search_parent) {
            searchEt.requestFocus();
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchEt, InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    public void getContacts(List<UserDetailBean> list) {
        mContactList = list;
        filterContacts();
    }

    @Override
    public void getCurrentUser(UserDetailBean bean) {
        if (bean != null) {
            nameTv.setText(bean.getFirstName() + " " + bean.getLastName());
            phoneTv.setText(bean.getPhone());
            profileImageTv.setText(setContactImage(bean.getFirstName() + " " + bean.getLastName()));
        }
    }



    /**
     * Method to filter contact.
     */
    private void filterContacts() {
        List<ContactBean> nonUserList = new ArrayList<>();
        List<UserDetailBean> appUserList = new ArrayList<>();
        List<ContactBean> contactUser = new ArrayList<>();
        if (mContactList!=null&&mContactList.size()>0)
        {
            for (ContactBean bean : mList) {
                int count = 0;
                String number = bean.getPhone();
                if (number.contains("-"))
                    number = number.replaceAll("-", "");
                if (number.contains(" "))
                    number = number.replaceAll(" ", "");
                if (number.contains("+91"))
                    number = number.replace("+91", "");
                if (number.length() == 11)
                    number = number.substring(1);
                for (UserDetailBean dbContact : mContactList) {
                    if (number.trim().equals(dbContact.getPhone().trim())) {
                        count = 1;
                        appUserList.add(dbContact);
                        break;
                    }
                }
                if (count == 1) {
                    ContactBean contactBean = new ContactBean();
                    contactBean.setPhone(number);
                    contactBean.setName(bean.getName());
                    contactUser.add(contactBean);
                    bean.setStatus(1);
                    mDbList.add(bean);
                } else {
                    bean.setStatus(0);
                    nonUserList.add(bean);
                }
            }
        }

        ((MainActivity)mActivity).getAppUsersList(appUserList);
        ((MainActivity)mActivity).getAppUser(contactUser);
        if (mContactList!=null)
            mList.clear();
        mList.addAll(mDbList);
        mList.addAll(nonUserList);
        mContactAdapter.notifyDataSetChanged();
        mContactAdapter.updateFilteredList();
    }


    /**
     * Method to set toolbar for contact fragment.
     */
    public void toolbarForContacts() {
        toolbarHeadingTv.setText(getString(R.string.contacts));
        toolbarAddIv.setVisibility(View.VISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mContactAdapter.getFilter().filter(editable.toString().toLowerCase().trim());
    }
}
