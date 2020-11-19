package com.example.cmproj2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.second_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.save_button){
            String text = text_note.getText().toString();
            new SaveTask().execute(note_id, text);

            return true;
        }else if(id == R.id.back_button){
            String text = text_note.getText().toString();
            mListener.SecondFragmentInteraction(note_id, text);

            return true;
        }
        return super.onOptionsItemSelected(item);
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

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        TextView title = view.findViewById(R.id.insert_title);
        title.setText(note_title);

        text_note = view.findViewById(R.id.text_note);
        text_note.setText("");

        new LoadTask().execute(note_id);

        return view;
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

                    String line = scan.nextLine()+'\n';
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