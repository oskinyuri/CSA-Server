package io.oskin;

import java.util.Objects;
import java.util.UUID;

public class Note {
    private String mId;
    private String mName;
    private String mText;

    public Note(){
    }

    public Note(String name, String text) {
        mId = UUID.randomUUID().toString();
        mName = name;
        mText = text;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(mId, note.mId) &&
                Objects.equals(mName, note.mName) &&
                Objects.equals(mText, note.mText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mName, mText);
    }
}
