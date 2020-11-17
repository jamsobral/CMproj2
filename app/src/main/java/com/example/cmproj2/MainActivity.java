package com.example.cmproj2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements FirstFragment.FirstFragmentInteractionListener,
                                                                SecondFragment.SecondFragmentInteractionListener {

    private static final String ARG_PARAM1 = "key1";
    private static final String ARG_PARAM2 = "key2";
    private static final String ARG_PARAM3 = "key3";
    private static final String ARG_PARAM4 = "key4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sharedpreferences saves titles
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        //PREPARAR E LANÇAR O PRIMEIRO FRAGMENTO
        FirstFragment firstFragment = FirstFragment.newInstance(prefs, prefsEditor); //VALORES INICIAIS PARA NAO IR EM BRANCO
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_activity, firstFragment, "fragOne");
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void FirstFragmentInteraction(String id, String title, String note) {

        // With the information from the FragmentOne, we will create a new Fragment, called
        //  FragmentTwo.
        // We will also store this fragment in the memory, with the tag "fragTwo", but we will
        //  also store the transaction (addToBackStack() method) in the memory so we can use it later on.
        SecondFragment fragmentTwo = SecondFragment.newInstance(id, title, note);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_activity , fragmentTwo, "fragTwo");
        fragmentTransaction.addToBackStack("Top");
        fragmentTransaction.commit();
    }

    @Override
    public void SecondFragmentInteraction(String id, String note) {
        FirstFragment fragmentOne = (FirstFragment) getSupportFragmentManager().findFragmentByTag("fragOne");
        // With this call, the FragmentOne will pop-up in the screen (it will call the onCreateView())
        //  with the new arguments.
        fragmentOne.updateNota(id, note);
        getSupportFragmentManager().popBackStack();
    }
}