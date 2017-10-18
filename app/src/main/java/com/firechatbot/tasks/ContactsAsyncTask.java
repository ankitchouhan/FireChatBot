package com.firechatbot.tasks;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.firechatbot.activities.MainActivity;
import com.firechatbot.beans.ContactBean;

import java.util.ArrayList;
import java.util.List;

public class ContactsAsyncTask extends AsyncTask<Void, Void, List<ContactBean>> {

    private Context mContext;

    public ContactsAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected List<ContactBean> doInBackground(Void... voids) {

        List<ContactBean> list = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ContactBean contactsBean = new ContactBean();
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    contactsBean.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    Cursor cr = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
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

    @Override
    protected void onPostExecute(List<ContactBean> list) {
        super.onPostExecute(list);
        if (mContext instanceof MainActivity)
            ((MainActivity) mContext).getContactsFromAsync(list);

    }
}
