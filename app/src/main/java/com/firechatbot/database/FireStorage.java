package com.firechatbot.database;


import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firechatbot.activities.ChatActivity;
import com.firechatbot.activities.UserDetailActivity;
import com.firechatbot.utils.AppConstants;
import com.firechatbot.utils.AppUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FireStorage {


    private static FireStorage mInstance;
    private StorageReference mStorageReference;

    private FireStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageReference = storage.getReference();
    }

    public static FireStorage getInstance() {
        if (mInstance == null)
            mInstance = new FireStorage();
        return mInstance;
    }

    /**
     * Method to upload user profile image and get url.
     */
    public void uploadUserImage(Uri file, String phoneNumber, final Activity activity) {
        UploadTask uploadTask = mStorageReference.child(AppConstants.USER_NODE).child(phoneNumber)
                .child(AppConstants.USER_PROFILE_IMAGE + file.getLastPathSegment()).putFile(file);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {
                    Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                    ((UserDetailActivity) activity).uploadData(downloadUrl);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AppUtils.displayToast(activity,e.getMessage());
            }
        });
    }


    /**
     * Method to upload image during user chat.
     * */
    public void uploadImageDuringChat(Uri file, final String chatRoomId, final Activity activity)
    {
        UploadTask uploadTask = mStorageReference.child(AppConstants.MESSAGE_NODE)
                .child(AppConstants.USER_PROFILE_IMAGE + file.getLastPathSegment()).putFile(file);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata()!=null)
                {
                    Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                    ((ChatActivity)activity).getImageUrlToUpload(downloadUrl,chatRoomId);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AppUtils.displayToast(activity,e.getMessage());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * (float)taskSnapshot.getBytesTransferred()) / (float)taskSnapshot.getTotalByteCount();
               // System.out.println("Upload is " + progress + "% done");
            }
        });
    }



    /**
     *     Method to upload user profile image from fb.
     */
   /* public void uploadFbImage(Uri file, String userId, final Activity activity) {
        try {
            InputStream stream = new FileInputStream(new File(String.valueOf(file)));
            UploadTask uploadTask = mStorageReference.child(AppConstants.USER_NODE).child(userId).child(AppConstants.USER_PROFILE_IMAGE + file.getLastPathSegment()).putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot != null)
                        ((UserDetailActivity) activity).uploadData(taskSnapshot.getDownloadUrl());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity,"Failed to upload",Toast.LENGTH_LONG).show();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
}
