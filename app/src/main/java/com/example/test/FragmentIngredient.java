package com.example.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentIngredient.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentIngredient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentIngredient extends Fragment {

    private DatabaseHelper dbHelper;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);

        dbHelper = new DatabaseHelper(view.getContext());
        Bundle bundle = getArguments();
        int id = bundle.getInt("id");





        ((TextView) view.findViewById(R.id.tv_ingredient)).setText(dbHelper.getMaterial(id));


        return view;

    }

}