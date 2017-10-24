package com.example.test;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCookSteps.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCookSteps#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCookSteps extends Fragment {

    private DatabaseHelper dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view=inflater.inflate(R.layout.fragment_cook_steps, container, false);
        dbHelper = new DatabaseHelper(view.getContext());

        Bundle bundle=getArguments();
        int id=bundle.getInt("id");

        ((TextView)view.findViewById(R.id.tv_cookstep)).setText(dbHelper.getStep(id));










        return view;
    }

}
