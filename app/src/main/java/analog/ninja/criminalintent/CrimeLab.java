package analog.ninja.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import analog.ninja.criminalintent.database.CrimeBaseHelper;
import analog.ninja.criminalintent.database.CrimeCursorWrapper;
import analog.ninja.criminalintent.database.CrimeDbSchema;

/*To create a singleton, you create a class with a private constructor and a get() method. If the instance
already exists, then get() simply returns the instance. If the instance does not exist yet, then get() will
call the constructor to create it.*/

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    // List is an interface that supports an ordered list of objects of a given type
    // Commonly used implementation of List is ArrayList, which uses regular Java array to store the elements


    // Database vars
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context context) {
        // Diamond notation is short hand for mCrimes = new ArrayList<Crime>();


        //Open Database
        mContext = context.getApplicationContext();
        //Open /data/date/ninja.analog.android.criminalintent/databases/crimeBase.db
        //Creating a new file if DNE. If first time, call onCreate(SQLiteDatabase), save version
        // If not first time, check versions number, if version number in CrimeOpenHelper is higher
        // call onUpgrade(SQLiteDatabase, int, int(.
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();




        /*for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0); // Every other one
            crime.setPosition(i);
            mCrimes.add(crime);
        }*/
    }
    public List<Crime> getCrimes() {
        //Query for all crimes, iterate using the cursor, populate crime list
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        // insert(String, String, ContentValues)
        //First argument is table to insert into, last argument is the value
        // Second argument is nullColumnHack. Allows for empty value.
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);

    }
    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        // update(String, ContentValues, String, String[])
        // update( table, values, where clause, values of arguments in where clause)
        // ? is treated as string, avoid SQL inject attack.
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME, values,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }
}
