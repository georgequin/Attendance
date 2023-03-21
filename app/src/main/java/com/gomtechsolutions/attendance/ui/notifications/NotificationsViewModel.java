package com.gomtechsolutions.attendance.ui.notifications;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel implements Parcelable {

    private MutableLiveData<String> mText;
    private String studentName;
    private String studentMatNumber;



//    public NotificationsViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is notifications fragment");
//    }


    public NotificationsViewModel(String studentName, String studentMatNumber) {
        this.studentName = studentName;
        this.studentMatNumber = studentMatNumber;
    }

    protected NotificationsViewModel(Parcel in) {
        studentName = in.readString();
        studentMatNumber = in.readString();
    }

    public static final Creator<NotificationsViewModel> CREATOR = new Creator<NotificationsViewModel>() {
        @Override
        public NotificationsViewModel createFromParcel(Parcel in) {
            return new NotificationsViewModel(in);
        }

        @Override
        public NotificationsViewModel[] newArray(int size) {
            return new NotificationsViewModel[size];
        }
    };

    // creating getter and setter methods.
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String productName) {
        this.studentName = productName;
    }

    public String getStudentMatNumber() {
        return studentMatNumber;
    }

    public void setStudentMatNumber(String studentMatNumber) {
        this.studentMatNumber = studentMatNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentName);
        dest.writeString(studentMatNumber);
    }

    public LiveData<String> getText() {
        return mText;
    }
}