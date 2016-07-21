package analog.ninja.criminalintent;

import java.util.Date;
import java.util.UUID;

// Model Layer containing information about a crime

public class Crime {
    private UUID mId;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private String mTitle;
    private int mPosition;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getmSuspect(){
        return mSuspect;
    }
    public void setmSuspect(String suspect){
        mSuspect = suspect;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


    public Crime() {
        //calls constructor below
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }



    public void setTitle(String title) {
        mTitle = title;
    }


}