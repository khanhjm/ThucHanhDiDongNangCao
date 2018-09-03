package khanhjm.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnOpen;
    private boolean isFragmentDisplay = false;
    // Saved instance state key
    static final String STATE_FRAGMENT = "state_of_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOpen = (Button) findViewById(R.id.btnOpen);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentDisplay)
                    displayFragment();
                else
                    closeFragment();
            }
        });

        if (savedInstanceState != null) {
            isFragmentDisplay = savedInstanceState.getBoolean(STATE_FRAGMENT);
            if (isFragmentDisplay)
                btnOpen.setText("Close");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_FRAGMENT, isFragmentDisplay);
        super.onSaveInstanceState(outState);
    }

    public void displayFragment() {
        SimpleFragment fragment = SimpleFragment.newsInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, fragment).commit();
        btnOpen.setText("Close");
        isFragmentDisplay = true;
    }

    public void closeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SimpleFragment fragment = (SimpleFragment) fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment).commit();
        }
        btnOpen.setText("Open");
        isFragmentDisplay = false;
    }
}
