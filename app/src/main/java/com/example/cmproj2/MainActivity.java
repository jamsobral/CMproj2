package com.example.cmproj2;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class MainActivity extends AppCompatActivity implements FirstFragment.FirstFragmentInteractionListener,
                                                                FirstFragment.addNotasListener,
                                                                FirstFragment.removeNotasListener,
                                                                FirstFragment.editNotasListener,
                                                                SecondFragment.SecondFragmentInteractionListener {

    private static final String ARG_PARAM1 = "key1";
    private static final String ARG_PARAM2 = "key2";
    private static final String ARG_PARAM3 = "key3";
    private static final String ARG_PARAM4 = "key4";

    private Context context;
    private static LayoutInflater objLayoutInflater;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

    private ArrayList<String> titles;
    private ArrayList<String> content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sharedpreferences saves titles
        prefs = getPreferences(MODE_PRIVATE);
        prefsEditor = prefs.edit();
        titles = new ArrayList<>();
        getSharedPreferences();

        //PREPARAR E LANÇAR O PRIMEIRO FRAGMENTO

        FirstFragment firstFragment = FirstFragment.newInstance(titles); //VALORES INICIAIS PARA NAO IR EM BRANCO
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_activity, firstFragment, "mainfrag");
        fragmentTransaction.commit();

        context = MainActivity.this;
        objLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        content = new ArrayList<>();

    }

    @Override
    protected void onStop() {
        super.onStop();
        setSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setSharedPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setSharedPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSharedPreferences();
    }

    public void setSharedPreferences(){
        Set<String> set = new HashSet<>();
        set.addAll(titles);
        prefsEditor.putStringSet("key", set);
        prefsEditor.commit();

    }

    public void getSharedPreferences(){
        Set<String> set = prefs.getStringSet("key", null);
        if (set != null){
            titles = new ArrayList(set);
        }
    }

    public void saveToFileSystem(){

    }
    public void show_erase_dialog(int position){
        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(context);
        warningBuilder.setTitle("Apagar ou modificar a nota "+position+"?");
        warningBuilder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                removeNotasInteraction(position);
                titles.remove(position);
            }
        });
        warningBuilder.setNeutralButton("Modificar título", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                show_modify_dialog(titles.get(position), position);
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

        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(context);
        warningBuilder.setTitle("Modificar a nota "+position+"?");
        warningBuilder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String new_note = editText.getText().toString();
                editNotasInteraction(new_note, position);
                titles.set(position, new_note);
            }
        });
        warningBuilder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = warningBuilder.create();
        dialog.setView(view);
        dialog.show();

    }

    public void show_new_note_dialog(){
        //STATIC CONTEXT POR ISSO OS DIALOGS TEM DE SER CHAMADOS A PARTIR DA MAIN ACTIVITY

        View view = objLayoutInflater.inflate(R.layout.dialog_view, null);

        final EditText editText = (EditText) view.findViewById(R.id.new_title);
        editText.setText("");
        editText.setHint("Insira novo titulo");

        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(context);
        warningBuilder.setTitle("Adicionar uma nota?");
        warningBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String new_note = editText.getText().toString();
                titles.add(new_note);
                addNotasInteraction(new_note);
            }
        });
        warningBuilder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = warningBuilder.create();
        dialog.setView(view);
        dialog.show();

    }

    @Override
    public void FirstFragmentInteraction(int position, String title) {

        // With the information from the FragmentOne, we will create a new Fragment, called
        //  FragmentTwo.
        // We will also store this fragment in the memory, with the tag "fragTwo", but we will
        //  also store the transaction (addToBackStack() method) in the memory so we can use it later on.
        SecondFragment fragmentTwo = SecondFragment.newInstance(position, title, content.get(position));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_activity , fragmentTwo, "fragTwo");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();

    }

    @Override
    public void addNotasInteraction(String new_note) {
        // You will arrive here from FragmentOne
        // It will print the following sentence, with the data provided by the fragment

        FirstFragment fragmentOne = (FirstFragment) getSupportFragmentManager().findFragmentByTag("mainfrag");
        fragmentOne.addNotas(new_note);

    }

    @Override
    public void removeNotasInteraction(int position) {
        // You will arrive here from FragmentOne
        // It will print the following sentence, with the data provided by the fragment
        FirstFragment fragmentOne = (FirstFragment) getSupportFragmentManager().findFragmentByTag("mainfrag");
        fragmentOne.removeNotas(position);

    }

    public void editNotasInteraction(String nota, int position){
        // You will arrive here from FragmentOne
        // It will print the following sentence, with the data provided by the fragment
        FirstFragment fragmentOne = (FirstFragment) getSupportFragmentManager().findFragmentByTag("mainfrag");
        fragmentOne.editNotas(nota, position);

    }

    @Override
    public void SecondFragmentInteraction(String TAG, String note, int position) {

        if (TAG.equals("back")) {
            FirstFragment fragmentOne = (FirstFragment) getSupportFragmentManager().findFragmentByTag("fragOne");
            // With this call, the FragmentOne will pop-up in the screen (it will call the onCreateView())
            //  with the new arguments.
            getSupportFragmentManager().popBackStack();
        } else {
            content.set(position, note);
        }

    }


}