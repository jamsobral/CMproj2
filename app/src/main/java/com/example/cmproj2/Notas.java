package com.example.cmproj2;

import java.util.ArrayList;

public class Notas {


    ArrayList<String> ids, titles, notes, display_titles;

    public Notas(ArrayList<String> ids, ArrayList<String> titles) {
        this.ids = ids;
        this.titles = titles;
        this.display_titles = new ArrayList<>();
        display_titles.addAll(titles);
        this.notes = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++){
            notes.add("");
        }
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

    public void updateNotes(ArrayList<String> notes){
        this.notes.clear();
        this.notes.addAll(notes);
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
        notes.add("");
    }

    public String[] getNoteByTitle(String title){
        int pos = titles.indexOf(title);
        return new String[]{ids.get(pos), titles.get(pos), notes.get(pos)};
    }

    public void updateNote(String id, String note){
        int pos = ids.indexOf(id);
        notes.set(pos, note);
    }

    public void removeNote(int position){
        ids.remove(position);
        titles.remove(position);
        display_titles.remove(position);
        notes.remove(position);
    }

    public void editNote(int position, String title){
        titles.set(position, title);
        display_titles.set(position, title);
    }
}
