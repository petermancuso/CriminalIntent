package analog.ninja.criminalintent;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*To create a singleton, you create a class with a private constructor and a get() method. If the instance
already exists, then get() simply returns the instance. If the instance does not exist yet, then get() will
call the constructor to create it.*/

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    // List is an interface that supports an ordered list of objects of a given type
    // Commonly used implementation of List is ArrayList, which uses regular Java array to store the elements
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context context) {
        // Diamond notation is short hand for mCrimes = new ArrayList<Crime>();
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0); // Every other one
            crime.setPosition(i);
            mCrimes.add(crime);
        }
    }
    public List<Crime> getCrimes() {
        return mCrimes;
    }
    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
