package com.example.cmproj2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

public class SecondFragment extends Fragment {

    private static final String ARG_PARAM1 = "key1";
    private static final String ARG_PARAM2 = "key2";
    private static final String ARG_PARAM3 = "key3";
    private static final String ARG_PARAM4 = "key4";

    private String note_id, note_title, note_content;
    private EditText text_note;

    private SecondFragment.SecondFragmentInteractionListener mListener;

    public SecondFragment(){

    }

    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note_id = getArguments().getString(ARG_PARAM1);
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

        text_note = view.findViewById(R.id.text_note);
        text_note.setText("");

        new LoadTask().execute(note_id);

        Button back = view.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // It will also call the function onFragmentOneInteraction() in the MainActivity.
                // This is the communication from a Fragment to Activity (in a nutshell)
                String text = text_note.getText().toString();
                mListener.SecondFragmentInteraction(note_id, text);
            }
        });

        Button save = view.findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = text_note.getText().toString();
                new SaveTask().execute(note_id, text);
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
        void SecondFragmentInteraction(String id, String note);
    }

    private class SaveTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... args) {
            // args[0] = titulo nota, args[1] = descricao nota
            try {
                System.out.println(args[0] + ".txt");
                PrintStream output = new PrintStream(getContext().openFileOutput(args[0] + ".txt", MODE_PRIVATE));
                output.println(args[1]);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class LoadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... file) {
            // file = array de tamanho 1
            Scanner scan = null;
            try {
                scan = new Scanner(getActivity().openFileInput(file[0] + ".txt"));
                String allText = ""; // read entire file
                while (scan.hasNextLine()) {

                    String line = scan.nextLine();
                    line += '\n';
                    allText += line;
                }
                return allText;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            note_content = s;
            text_note.setText(s);
        }
    }
}