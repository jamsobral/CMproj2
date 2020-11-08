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
        String[] list_notas = new String[]{"Nota1", "Nota1", "Nota1", "Nota1", "Nota1", "Nota1","Nota1", "Nota1", "Nota1", "Nota1", "Nota1", "Nota1","Nota1", "Nota1", "Nota1", "Nota1", "Nota1", "Nota1"};
        FirstFragment firstFragment = FirstFragment.newInstance(list_notas); //VALORES INICIAIS PARA NAO IR EM BRANCO
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_activity, firstFragment, "mainfrag");
        fragmentTransaction.commit();

    }

    @Override
    public void FirstFragmentInteraction(int spinner) {

    }

    @Override
    public void SecondFragmentInteraction(int spinner) {

    }
}