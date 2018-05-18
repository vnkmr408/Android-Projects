package com.example.eiraj.seekdoers.Modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abhishek on 26-08-2017.
 */

public class Tag implements Parcelable{

    private String term_id;
    private String job_category;
    private String slug;
    private boolean selected;

    public Tag() {
        selected =false;
    }

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getJob_category() {
        return job_category;
    }

    public void setJob_category(String job_category) {
        this.job_category = job_category;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(term_id);
        dest.writeString(job_category);
        dest.writeString(slug);
        dest.writeByte((byte) (selected ? 1 : 0));

    }

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    public Tag(Parcel in) {
        term_id = in.readString();
        job_category = in.readString();
        slug = in.readString();
        selected  = in.readByte() != 0;
    }
}
