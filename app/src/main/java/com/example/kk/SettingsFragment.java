package com.example.kk.wedontchat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
private View settingsFragment;

    public String get;
    public SettingsFragment() {
        // Required empty public constructor
    }


    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        settingsFragment = inflater.inflate(R.layout.fragment_settings, container, false);
        get= (String) getArguments().get("username");

initui();

        // Inflate the layout for this fragment
        return settingsFragment;
    }
    private void initui() {
TextView showname=settingsFragment.findViewById(R.id.chit);
showname.setText(get);
    }

}
