package com.example.cmproj2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    private static final String ARG_PARAM1 = "key1";
    private static final String ARG_PARAM2 = "key2";
    private static final String ARG_PARAM3 = "key3";
    private static final String ARG_PARAM4 = "key4";

    private int note_index;
    private String note_title;

    private SecondFragment.SecondFragmentInteractionListener mListener;

    public SecondFragment(){

    }

    public static SecondFragment newInstance(Integer param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note_index = getArguments().getInt(ARG_PARAM1);
            note_title = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        initArguments();

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        TextView title = view.findViewById(R.id.insert_title);
        title.setText(note_title);

        Button back = view.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // By pressing the button, it will print the sentence FragmentOne: <mParam1_data>
                System.out.println("FragmentTwo: " + note_index);

                // It will also call the function onFragmentOneInteraction() in the MainActivity.
                // This is the communication from a Fragment to Activity (in a nutshell)
                mListener.SecondFragmentInteraction(Integer.valueOf(note_index));
            }
        });
        return view;
    }


    private void initArguments(){
        if (getArguments() != null) {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SecondFragment.SecondFragmentInteractionListener) {

            // This will initialize the variable. It will return an exception if it is not
            //  implemented in the java code of the variable context (in our case the
            //  context is the MainActivity.
            mListener = (SecondFragment.SecondFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SecondFragmentInteractionListener {
        void SecondFragmentInteraction(int spinner);
    }
}