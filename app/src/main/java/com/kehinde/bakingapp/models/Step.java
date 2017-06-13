package com.kehinde.bakingapp.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.kehinde.bakingapp.util.Constants;

/**
 * Created by kehinde on 6/2/17.
 */

public class Step implements Parcelable{

    private int id;
    private String shortDescription,description,videoURL;

    public Step() {
    }

    public Step(int id, String shortDescription, String description, String videoURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
    }

    public Step(Bundle bundle){
        this.shortDescription=bundle.getString(Constants.SHORT_DESCRIPTION);
        this.description=bundle.getString(Constants.DESCRIPTION);
        this.videoURL=bundle.getString(Constants.VIDEO_URL);
    }

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public Bundle toBundle(Step step){
        Bundle bundle=new Bundle();
        bundle.putString(Constants.DESCRIPTION,step.getDescription());
        bundle.putString(Constants.SHORT_DESCRIPTION,step.getShortDescription());
        bundle.putString(Constants.VIDEO_URL,step.getVideoURL());
        return bundle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
    }
}
