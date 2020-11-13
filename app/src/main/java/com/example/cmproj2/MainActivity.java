package com.example.cmproj2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class MainActivity extends AppCompatActivity implements FirstFragment.FirstFragmentInteractionListener, SecondFragment.SecondFragmentInteractionListener{

    private static final String ARG_PARAM1 = "key1";
    private static final String ARG_PARAM2 = "key2";
    private static final String ARG_PARAM3 = "key3";
    private static final String ARG_PARAM4 = "key4";

    private static Context context;
    private static LayoutInflater objLayoutInflater;

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor ;

    private static String[] list_notas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sharedpreferences saves titles
        prefs = getPreferences(MODE_PRIVATE);
        prefsEditor = prefs.edit();
        list_notas = new String[]{};
        getSharedPreferences();

        //PREPARAR E LANÇAR O PRIMEIRO FRAGMENTO

        FirstFragment firstFragment = FirstFragment.newInstance(list_notas); //VALORES INICIAIS PARA NAO IR EM BRANCO
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_activity, firstFragment, "mainfrag");
        fragmentTransaction.commit();

        //DIALOGS context and inflator
        context = MainActivity.this;
        objLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        set.addAll(Arrays.asList(list_notas));
        prefsEditor.putStringSet("key",set);
        prefsEditor.commit();

    }

    public void getSharedPreferences(){
        Set<String> set = prefs.getStringSet("key",null);
        list_notas = set.toArray(new String[0]);
    }

    public static void show_erase_dialog(int position){
        //STATIC CONTEXT POR ISSO OS DIALOGS TEM DE SER CHAMADOS A PARTIR DA MAIN ACTIVITY

        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(context);
        warningBuilder.setTitle("Apagar ou modificar a nota "+position+"?");
        warningBuilder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //TODO erase note on position "position"
            }
        });
        warningBuilder.setNeutralButton("Modificar título", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                show_modify_dialog(position);
            }
        });
        warningBuilder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = warningBuilder.create();
        dialog.show();

    }

    public static void show_modify_dialog(int position){
        //STATIC CONTEXT POR ISSO OS DIALOGS TEM DE SER CHAMADOS A PARTIR DA MAIN ACTIVITY

        View view = objLayoutInflater.inflate(R.layout.dialog_view, null);

        final EditText editText = (EditText) view.findViewById(R.id.new_title);

        //TODO replace this edittext set text
        editText.setText("get note title from shared preferences for position "+ position);

        AlertDialog.Builder warningBuilder = new AlertDialog.Builder(context);
        warningBuilder.setTitle("Modificar a nota "+position+"?");
        warningBuilder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //TODO save new title (editext.getText) on shared preferences no id "position"
            }
        });
        warningBuilder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = warningBuilder.create();
        dialog.setView(view);
        dialog.show();

    }

    public static void show_new_note_dialog(){
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
                list_notas = append(list_notas,editText.getText().toString());
                //TODO update list viewer
            }
        });
        warningBuilder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = warningBuilder.create();
        dialog.setView(view);
        dialog.show();

    }

    static String[] append(String[] arr, String element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    @Override
    public void FirstFragmentInteraction(int spinner, String title) {

        // You will arrive here from FragmentOne
        // It will print the following sentence, with the data provided by the fragment
        System.out.println("MainActivity (from one): " + spinner);

        // With the information from the FragmentOne, we will create a new Fragment, called
        //  FragmentTwo.
        // We will also store this fragment in the memory, with the tag "fragTwo", but we will
        //  also store the transaction (addToBackStack() method) in the memory so we can use it later on.
        SecondFragment fragmentTwo = SecondFragment.newInstance(spinner,title);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_activity , fragmentTwo, "fragTwo");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();

    }

    @Override
    public void SecondFragmentInteraction(int spinner) {
        System.out.println("MainActivity (from two): " + spinner);

        FirstFragment fragmentOne = (FirstFragment) getSupportFragmentManager().findFragmentByTag("fragOne");

        // With this call, the FragmentOne will pop-up in the screen (it will call the onCreateView())
        //  with the new arguments.
        getSupportFragmentManager().popBackStack();

    }
}