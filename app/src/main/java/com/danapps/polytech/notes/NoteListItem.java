package com.danapps.polytech.notes;

public class NoteListItem {

    private String title;
    private String subtitle;

    public NoteListItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    String getTitle() {
        return title;
    }

    String getSubtitle() {
        return subtitle;
    }
}

