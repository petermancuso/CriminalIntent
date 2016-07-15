package analog.ninja.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public abstract class SingleFragmentActivity extends AppCompatActivity{

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        // Give FM a fragment to manage. If DNE, create it.
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();

            // Fragment Transactions are used to add/remove/detach or replace
            // fragments in the fragment list. Code says Create a new fragment transaction,
            // include one add operation in it, and then commit it.”
            fm.beginTransaction()
                    // Parameters: (container view ID (activity_fragment.xml), newly created crime fragment)
                    .add(R.id.fragment_container, fragment)
                    .commit();

            //Container view tells the FragmentManager where in the activity’s view the fragment’s view should appear
            // and it is used as a unique identifier for a fragment in the FragmentManager’s list.
        }
    }
}
