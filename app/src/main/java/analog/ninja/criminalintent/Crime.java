package analog.ninja.criminalintent;

import java.util.Date;
import java.util.UUID;

// Model Layer containing information about a crime

public class Crime {
    private UUID mId;
    private Date mDate;
    private boolean mSolved;
    private String mTitle;

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }


    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


    public Crime() {
        // Generate unique identifiers
        mId = UUID.randomUUID();
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