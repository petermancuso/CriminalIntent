package analog.ninja.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class CrimeListFragment extends Fragment{

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static final int REQUEST_CRIME = 1;
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private int crimeChanged;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    //Lets FragmentManager know that CrimeListFragment needs to receive menu callbacks.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        // RecyclerView requires a LayoutManager to work.
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Connects Adaptor to RecyclerView

        // Retrieve State of subtitle after rotation
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI(0);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CRIME){
            crimeChanged = data.getIntExtra(ARG_CRIME_ID, 0);
         }
    }

    // Called when CrimeListActivity is resumed from detailed view.
    // Detail activity is destroyed and back stack from layout manager resumes CrimeListFragment.
    @Override
    public void onResume() {
        super.onResume();
        updateUI(crimeChanged);
    }

    //Save state of subtitle for use after rotation
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    // Part of the Callback interface, along with onAttach.
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    //Inflates menu resource
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        //Trigger a re-creation of the action items
        //when the user presses on the Show Subtitle action item.
        //Changes text according to current state.
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    //
    // Called when user click toolbar icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Check id that you assigned
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                updateUI(0);
                mCallbacks.onCrimeSelected(crime);;
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        //Generate subtitle string
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        // Shows or hide subtitle based on current state.
        if (!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        //The toolbar is still refered to as 'action bar' for legacy reasons
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    //Connects the Adapter to RecyclerView. Creates a CrimeAdapter and set it on the RecyclerView.
    public void updateUI(int c) {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        // If returning from detail view, notify adaptor of data change
        } else{

            //mAdapter.notifyItemChanged(c);
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        //Updates subtitle upon return from CrimeFragment
        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView) {
            super(itemView);
            // itemView, which is the View for the entire wor, is set as the receiver of click events.
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }
        // Update fields to reflect the state of crime
        // Called by CrimeAdapter.onBindViewHolder
        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }
        @Override
        public void onClick(View v) {
            mCallbacks.onCrimeSelected(mCrime);
        }
    }

    // The RecyclerView will communicate with this adapter when a ViewHolder needs to be created or
    // connected with a Crime object.The RecyclerView itself will not know anything about the Crime
    // object, but the Adapter will know all the details
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }
        // onCreateViewHolder is called by the RecyclerView when it needs a new View to display an item.
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //inflate a layout list_item_crime.xml.
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        // Method will bind a ViewHolder’s View to your model object. It receives the ViewHolder
        // and a position in your data set. To bind your View, you use that position to find the
        // right model data. Then you update the View to reflect that model data.
        // that position is the index of the Crime in your array. Once you pull it out, you
        // bind that Crime to your View by sending its title to your ViewHolder’s TextView.
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        //swaps out the crimes displayed.
        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }
    }
}
