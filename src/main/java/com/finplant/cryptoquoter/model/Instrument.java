package com.finplant.cryptoquoter.model;

import java.util.Collection;
import java.util.LinkedList;

public class Instrument {
    private String name;
    private Currency base;
    private Currency counter;
    private Collection<Instrument> depends;

    public Instrument(String name, String stringView){
        this.name = name;
        parseCurrencies(stringView);
    }

    public void readDepents(Collection<Instrument> depends) {
        if (this.depends == null)
            this.depends = new LinkedList<>();
        this.depends.addAll(depends);
    }

    private void parseCurrencies(String stringView) {
        String[] arr = stringView.split("/");
        if (arr.length == 2) {
            base = new Currency(arr[0]);
            counter = new Currency(arr[1]);
        }
    }

    @Override
    public String toString() {
        return base.getCode() + "/" + counter.getCode();
    }
}
