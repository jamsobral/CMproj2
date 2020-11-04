package com.example.cmproj2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

public class FirstFragment extends Fragment {
    private ListView Notas;
    private String[] list_notas;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Notas = (ListView) view.findViewById(R.id.notes_list);
        list_notas = new String[]{"Nota1", "Nota1", "Nota1", "Nota1", "Nota1", "Nota1","Nota1", "Nota1", "Nota1", "Nota1", "Nota1", "Nota1","Nota1", "Nota1", "Nota1", "Nota1", "Nota1", "Nota1"};
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.note_list_item, list_notas);
        Notas.setAdapter(itemsAdapter);

        Notas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),"position="+position+"!!!id="+id, Toast.LENGTH_LONG).show();
            }
        });

        /*view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }
}