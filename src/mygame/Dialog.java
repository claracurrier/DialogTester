/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Clara Currier
 */
public class Dialog implements Iterator {

    private int linenum = 0;
    private ArrayList<String> character = new ArrayList<>();
    private ArrayList<String> expression = new ArrayList<>();
    private ArrayList<String> location = new ArrayList<>();
    private ArrayList<String> facing = new ArrayList<>();
    private ArrayList<String> speech = new ArrayList<>();

    public Dialog() {
    }

    @Override
    public boolean hasNext() {
        return linenum < character.size();
    }

    @Override
    public Object next() {
        linenum++;
        return null;
    }

    public String nextChar() {
        return character.get(linenum);
    }

    public String nextExpr() {
        return expression.get(linenum);
    }

    public String nextFace() {
        return facing.get(linenum);
    }

    public String nextLoc() {
        return location.get(linenum);
    }

    @Override
    public void remove() {
    }

    public void addChar(String charname) {
        character.add(charname);
    }

    public void addExpr(String expr) {
        expression.add(expr);
    }

    public void addLoc(String loc) {
        location.add(loc);
    }

    public void addFacing(String face) {
        facing.add(face);
    }

    public void addSpeech(String s) {
        speech.add(s);
    }
}
