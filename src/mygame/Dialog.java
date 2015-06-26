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

    private int frameNum = 0;
    private ArrayList<String> uniqueChars = new ArrayList<>();
    private ArrayList<ArrayList<Character>> characters = new ArrayList<>();

    public Dialog() {
    }

    public ArrayList<Character> getCurFrame() {
        return characters.get(frameNum);
    }

    @Override
    public boolean hasNext() {
        return frameNum < characters.size();
    }

    @Override
    public Object next() {
        frameNum++;
        return null;
    }

    public void reset() {
        frameNum = 0;
    }

    public ArrayList<String> getUniqueChars() {
        return uniqueChars;
    }

    @Override
    public void remove() {
    }

    public void addFrame(ArrayList<Character> frame) {
        characters.add(frame);
    }

    public void addUniqueChar(String charname) {
        uniqueChars.add(charname);
    }
}