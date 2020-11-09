package com.example.cmproj2;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FirstFragment.FirstFragmentInteractionListener, SecondFragment.SecondFragmentInteractionListener{

    private static final String ARG_PARAM1 = "key1";
    private static final String ARG_PARAM2 = "key2";
    private static final String ARG_PARAM3 = "key3";
    private static final String ARG_PARAM4 = "key4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //PREPARAR E LANÇAR O PRIMEIRO FRAGMENTO
        String[] list_notas = new String[]{"Nota1", "Nota2", "Nota3", "Nota4", "Nota5", "Nota6","Nota7", "Nota8", "Nota9", "Nota10", "Nota11", "Nota12","Nota13", "Nota14", "Nota15", "Nota16", "Nota17", "Nota18"};
        FirstFragment firstFragment = FirstFragment.newInstance(list_notas); //VALORES INICIAIS PARA NAO IR EM BRANCO
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_activity, firstFragment, "mainfrag");
        fragmentTransaction.commit();

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

        // TODO - you should ALWAYS check if this is null or not. It might return null.
        FirstFragment fragmentOne = (FirstFragment) getSupportFragmentManager().findFragmentByTag("fragOne");

        // With this call, the FragmentOne will pop-up in the screen (it will call the onCreateView())
        //  with the new arguments.
        getSupportFragmentManager().popBackStack();

    }
}