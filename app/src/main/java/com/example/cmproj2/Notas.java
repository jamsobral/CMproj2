package com.example.cmproj2;

import java.util.ArrayList;

public class Notas {


    ArrayList<String> ids, titles, display_titles;

    public Notas(ArrayList<String> ids, ArrayList<String> titles) {
        this.ids = ids;
        this.titles = titles;
        this.display_titles = new ArrayList<>();
        display_titles.addAll(titles);
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public ArrayList<String> getDisplay_titles() {
        return display_titles;
    }

    public String getTitleById(String id){
        int pos = ids.indexOf(id);
        return titles.get(pos);
    }

    public void newNote(String title){
        String newId;
        if (ids.size() == 0){
            newId = "1";
        } else {
            String lastId = ids.get(ids.size() - 1);
            newId = Integer.toString(Integer.parseInt(lastId) + 1);
        }
        ids.add(newId);
        titles.add(title);
        display_titles.add(title);
    }

    public String getIdByTitle(String title){
        int pos = titles.indexOf(title);
        return ids.get(pos);
    }

    public void removeNote(int position){
        ids.remove(position);
        titles.remove(position);
        display_titles.remove(position);
    }

    public void editNote(int position, String title){
        titles.set(position, title);
        display_titles.set(position, title);
    }
}
