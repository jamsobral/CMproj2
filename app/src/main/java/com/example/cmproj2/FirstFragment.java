package com.example.cmproj2;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class FirstFragment extends Fragment {

    private static final String ARG_PARAM1 = "key1";
    private static final String ARG_PARAM2 = "key2";
    private static final String ARG_PARAM3 = "key3";
    private static final String ARG_PARAM4 = "key4";

    private ListView Notas;
    private String[] list_notas;
    private Button new_note;
    private FirstFragmentInteractionListener mListener;
    private EditText search;
    private boolean search_toogle = false;

    public static FirstFragment newInstance(String[] list_nots) {
        //função usada pela atividade principal
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, list_nots);
        fragment.setArguments(args);
        return fragment;
    }

    public FirstFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        initArguments();

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        //new note feature ------------------------------------------------------------------------------------------------------------------------
        new_note = (Button) view.findViewById(R.id.save_button);
        new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(),"NEW NOTE DIALOG", Toast.LENGTH_LONG).show();
            }
        });

        // ------------------------------------------------------------------------------------------------------------------------

        //LIST FEATURE ------------------------------------------------------------------------------------------------------------------------
        Notas = (ListView) view.findViewById(R.id.notes_list);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.note_list_item, list_notas);
        Notas.setAdapter(itemsAdapter);

        Notas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),"SHORT position="+position+"!!!id="+id, Toast.LENGTH_LONG).show();
                mListener.FirstFragmentInteraction(position,list_notas[position]);
            }
        });
        Notas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),"LONG position="+position+"!!!id="+id, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        // ------------------------------------------------------------------------------------------------------------------------


        //SEARCH FEATURE ------------------------------------------------------------------------------------------------------------------------
        //inflating custom view with edit text
        LayoutInflater objLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View snackView = objLayoutInflater.inflate(R.layout.search_snac, null);
        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.addView(snackView, 0);

        search = (EditText) snackView.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updatelist();
                Toast.makeText(getActivity().getApplicationContext(),search.getText().toString() + " -> SEARCH", Toast.LENGTH_LONG).show();
            }
            @Override public void afterTextChanged(Editable editable) { }
        });
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {
            if (!search_toogle)
                snackbar.show();
            else {
                snackbar.dismiss();
                search.setText("");
            }
            search_toogle = !search_toogle;
        });
        //------------------------------------------------------------------------------------------------------------------------

        return view;
    }

    private void updatelist(){

    }

    private void initArguments(){
        if (getArguments() != null) {
            list_notas = getArguments().getStringArray(ARG_PARAM1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FirstFragmentInteractionListener) {

            // This will initialize the variable. It will return an exception if it is not
            //  implemented in the java code of the variable context (in our case the
            //  context is the MainActivity.
            mListener = (FirstFragmentInteractionListener) context;
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

    public interface FirstFragmentInteractionListener {
        void FirstFragmentInteraction(int spinner,String title);
    }
}