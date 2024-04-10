package com.olioht.municipalityinfo.recyclerview.searched;

import java.util.ArrayList;

public class ListSearches {
    private static ListSearches storage = null;
    private ArrayList<Search> searches = new ArrayList<>();

    private ListSearches() {

    }

    public static ListSearches getInstance() {
        if (storage == null) {
            storage = new ListSearches();
        }
        return storage;
    }

    public void addSearch(Search search) {
        searches.add(search);
    }

    public ArrayList<Search> getSearches() {
        return searches;
    }
}
