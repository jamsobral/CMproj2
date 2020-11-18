package com.example.cmproj2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class FirstFragment extends Fragment {

    private static final String ARG_PARAM1 = "key1";
    private static final String ARG_PARAM2 = "key2";
    private static final String ARG_PARAM3 = "key3";
    private static final String ARG_PARAM4 = "key4";

    private ListView notasView;
    private Notas notas;
    private Button new_note;
    private FirstFragmentInteractionListener mListener;
    private EditText search;
    private boolean search_toogle = false;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private LayoutInflater objLayoutInflater;


    public static FirstFragment newInstance(SharedPreferences prefs, SharedPreferences.Editor prefsEditor) {
        //função usada pela atividade principal
        FirstFragment fragment = new FirstFragment(prefs, prefsEditor);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FirstFragment(SharedPreferences prefs, SharedPreferences.Editor prefsEditor){
        this.prefs = prefs;
        this.prefsEditor = prefsEditor;
    }

    public void setSharedPreferences(){
        prefsEditor.clear();
        Set<String> set = new HashSet<>();
        set.addAll(notas.getIds());
        prefsEditor.putStringSet("key", set);
        for (int i = 0; i < notas.getIds().size(); i++){
            String id = notas.getIds().get(i);
            prefsEditor.putString(id, notas.getTitleById(id));
        }
        prefsEditor.commit();
    }

    public void getSharedPreferences(){
        Set<String> set = prefs.getStringSet("key", null);
        ArrayList<String> ids, titles;
        if (set != null){
            ids = new ArrayList<String>(set);
            titles = new ArrayList<>();
            for (String id: ids){
                titles.add(prefs.getString(id, null));
            }
            notas = new Notas(ids, titles);
        } else {
            notas = new Notas(new ArrayList<String>(), new ArrayList<String>());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        getSharedPreferences();
        ArrayList<String> ids = notas.getIds();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //initArguments();
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        //new note feature ------------------------------------------------------------------------------------------------------------------------
        new_note = (Button) view.findViewById(R.id.save_button);
        new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_new_note_dialog();
            }
        });

        // ------------------------------------------------------------------------------------------------------------------------

        //LIST FEATURE ------------------------------------------------------------------------------------------------------------------------
        notasView = (ListView) view.findViewById(R.id.notes_list);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.note_list_item, notas.getDisplay_titles());
        notasView.setAdapter(itemsAdapter);


        notasView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id_) {
                String title = (String) parent.getAdapter().getItem(position);
                String id = notas.getIdByTitle(title);
                mListener.FirstFragmentInteraction(id, title);
            }
        });
        notasView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                show_erase_dialog(position);
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
                //Toast.makeText(getActivity().getApplicationContext(),search.getText().toString() + " -> SEARCH", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) notasView.getAdapter();
                ArrayList<String> notasFiltradas = new ArrayList<>();
                for (String nota: notas.getTitles()){
                    if (nota.contains(charSequence)){
                        notasFiltradas.add(nota);
                    }
                }
                adapter.clear();
                adapter.addAll(notasFiltradas);
                adapter.notifyDataSetChanged();
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
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) notasView.getAdapter();
            adapter.clear();
            adapter.addAll(notas.getTitles());
            adapter.notifyDataSetChanged();

        });
        //------------------------------------------------------------------------------------------------------------------------

        return view;
    }

    public void show_erase_dialog(int position){
        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(getActivity());
        warningBuilder.setTitle("Apagar ou modificar a nota "+notas.getTitles().get(position)+"?");
        warningBuilder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                removeNotas(position);
            }
        });
        warningBuilder.setNeutralButton("Modificar título", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                show_modify_dialog(notas.getTitles().get(position), position);
            }
        });
        warningBuilder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = warningBuilder.create();
        dialog.show();

    }

    public void show_modify_dialog(String nota, int position){

        View view = objLayoutInflater.inflate(R.layout.dialog_view, null);

        EditText editText = (EditText) view.findViewById(R.id.new_title);

        editText.setText(nota);

        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(getActivity());
        warningBuilder.setTitle("Modificar a nota "+position+"?");
        warningBuilder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String new_note = editText.getText().toString();
                editNotas(new_note, position);
            }
        });
        warningBuilder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = warningBuilder.create();
        dialog.setView(view);
        dialog.show();

    }

    public void show_new_note_dialog(){

        View view = objLayoutInflater.inflate(R.layout.dialog_view, null);

        final EditText editText = (EditText) view.findViewById(R.id.new_title);
        editText.setText("");
        editText.setHint("Insira novo titulo");

        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(getActivity());
        warningBuilder.setTitle("Adicionar uma nota?");
        warningBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String new_note = editText.getText().toString();
                addNotas(new_note);
            }
        });
        warningBuilder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = warningBuilder.create();
        dialog.setView(view);
        dialog.show();

    }
    private void initArguments(){
        if (getArguments() != null) {
            //titles = getArguments().getStringArrayList(ARG_PARAM1);
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

    public void addNotas(String nota){
        notas.newNote(nota);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) notasView.getAdapter();
        adapter.notifyDataSetChanged();
        setSharedPreferences();
    }

    public void removeNotas(int position){
        notas.removeNote(position);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) notasView.getAdapter();
        adapter.notifyDataSetChanged();
        setSharedPreferences();
    }

    public void editNotas(String nota, int position){
        notas.editNote(position, nota);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) notasView.getAdapter();
        adapter.notifyDataSetChanged();
        setSharedPreferences();
    }

    public interface FirstFragmentInteractionListener {
        void FirstFragmentInteraction(String id, String title);
    }


}