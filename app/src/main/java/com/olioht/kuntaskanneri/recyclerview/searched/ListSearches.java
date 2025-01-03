package com.olioht.kuntaskanneri.recyclerview.searched;

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
        // If the search is already in the list, remove it
        // This is to ensure that the search is at the beginning of the list and considered the most recent
        searches.removeIf(s -> s.getMunicipalityName().equals(search.getMunicipalityName()));

        // Add the new search at the beginning
        searches.add(0, search);
    }

    public Search getSearch(int position) {
        return searches.get(position);
    }

    public int getSize() {
        return searches.size();
    }

}
