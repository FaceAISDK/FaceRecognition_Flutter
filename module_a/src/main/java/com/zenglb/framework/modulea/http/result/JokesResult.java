package com.zenglb.framework.modulea.http.result;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;


/**
 * just a demo !
 * Created by zenglb on 2017/2/9.
 */
@Entity
public class JokesResult implements Parcelable {
    private String topic;
    private String start_time;
    @Unique
    private String id;

    boolean selected;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        dest.writeString(this.topic);
        dest.writeString(this.start_time);
        dest.writeString(this.id);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public boolean getSelected() {
        return this.selected;
    }

    public JokesResult() {
    }

    protected JokesResult(Parcel in) {
        this.topic = in.readString();
        this.start_time = in.readString();
        this.id = in.readString();
        this.selected = in.readByte() != 0;
    }

    @Generated(hash = 1244182622)
    public JokesResult(String topic, String start_time, String id,
            boolean selected) {
        this.topic = topic;
        this.start_time = start_time;
        this.id = id;
        this.selected = selected;
    }

    public static final Creator<JokesResult> CREATOR = new Creator<JokesResult>() {
        @Override
        public JokesResult createFromParcel(Parcel source) {
            return new JokesResult(source);
        }

        @Override
        public JokesResult[] newArray(int size) {
            return new JokesResult[size];
        }
    };
}
