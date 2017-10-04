package com.firechatbot.fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firechatbot.R;
import com.firechatbot.activities.MainActivity;
import com.firechatbot.adapters.ContactAdapter;
import com.firechatbot.beans.ContactBean;
import com.firechatbot.beans.UserDetailBean;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private TextView profileImageTv;
    private TextView nameTv, phoneTv;
    private RecyclerView recyclerViewRv;
    private ContactAdapter mContactAdapter;
    //private List<ContactBean> mList;
    private Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        initViews(view);
        //initVariables();
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
        SearchView searchView = view.findViewById(R.id.sv_search);
        searchView.setOnQueryTextListener(this);
        //TODO remove static string.
        searchView.setQueryHint("Search");
        mActivity = getActivity();
    }

    /**
     * Method to initialize variables.
     */
    private void initVariables() {
        //mList = new ArrayList<>();
        mContactAdapter = new ContactAdapter(getContacts());
        recyclerViewRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewRv.setAdapter(mContactAdapter);
    }

    /**
     * Method to set current user details.
     */
    private void getCurrentUser() {
        UserDetailBean detailBean = ((MainActivity) mActivity).mUserDetails;
        if (detailBean!=null)
        {
            nameTv.setText(detailBean.getFirstName() + " " + detailBean.getLastName());
            phoneTv.setText(detailBean.getPhone());
            profileImageTv.setText(setContactImage(detailBean.getFirstName()+" "+detailBean.getLastName()));
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (((MainActivity) mActivity).contactDetails()) {
           /* mList.clear();
            mList.addAll(getContacts());
            mContactAdapter.notifyDataSetChanged();*/
            initVariables();
            getCurrentUser();
        }
    }

    /**
     *Method to request contact permission.
     */
   /* private void requestContactPermission()
    {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.READ_CONTACTS}, AppConstants.CONTACTS_REQUEST_CODE);
        else
        {
            mList.clear();
            mList.addAll(getContacts());
            mContactAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstants.CONTACTS_REQUEST_CODE)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                mList.clear();
                mList.addAll(getContacts());
                mContactAdapter.notifyDataSetChanged();
            }
            else
                AppUtils.displayToast(mActivity,getString(R.string.permission_denied));
        }
    }
*/

    /**
     * Method to get contacts details
     */
    private List<ContactBean> getContacts() {
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
    }


    /**
     * Method to display contact image.
     * */
    private String setContactImage(String name)
    {
        String[] contact = name.split(" ");
        String displayName="";
        for (String data : contact)
        {
            displayName = displayName+data.charAt(0);
        }
        return displayName;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mContactAdapter.getFilter().filter(newText.toLowerCase().trim());
        return true;
    }
}
