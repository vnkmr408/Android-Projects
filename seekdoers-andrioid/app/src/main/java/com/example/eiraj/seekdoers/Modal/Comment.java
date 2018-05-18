package com.example.eiraj.seekdoers.Modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abhishek on 30-08-2017.
 */

public class Comment implements Parcelable {

    String comment_ID;
    String comment_author;
    String comment_author_email;
    String comment_date;
    String comment_content;
    String rating;

    protected Comment(Parcel in) {
        comment_ID = in.readString();
        comment_author = in.readString();
        comment_author_email = in.readString();
        comment_date = in.readString();
        comment_content = in.readString();
        rating = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getComment_ID() {
        return comment_ID;
    }

    public void setComment_ID(String comment_ID) {
        this.comment_ID = comment_ID;
    }

    public String getComment_author() {
        return comment_author;
    }

    public void setComment_author(String comment_author) {
        this.comment_author = comment_author;
    }

    public String getComment_author_email() {
        return comment_author_email;
    }

    public void setComment_author_email(String comment_author_email) {
        this.comment_author_email = comment_author_email;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(comment_ID);
        dest.writeString(comment_author);
        dest.writeString(comment_author_email);
        dest.writeString(comment_date);
        dest.writeString(comment_content);
        dest.writeString(rating);

    }
}
