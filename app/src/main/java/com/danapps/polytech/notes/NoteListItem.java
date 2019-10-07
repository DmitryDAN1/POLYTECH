package com.danapps.polytech.notes;

public class NoteListItem {

    private String title;
    private String subtitle;

    public NoteListItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}

