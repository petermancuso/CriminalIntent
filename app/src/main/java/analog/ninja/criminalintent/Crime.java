package analog.ninja.criminalintent;

import java.util.UUID;

public class Crime {

    public Crime() {

        mId = UUID.randomUUID();
    }
    private UUID mId;

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    private String mTitle;

    public void setTitle(String title) {
        mTitle = title;
    }


}