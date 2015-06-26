/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author Clara Currier
 */
public class Character {

    private String expression;
    private String location;
    private String facing;
    private String speech;
    private String name;

    public Character(String n) {
        name = n;
    }

    public void setExpr(String e) {
        expression = e;
    }

    public void setLoc(String e) {
        location = e;
    }

    public void setFacing(String e) {
        facing = e;
    }

    public void setSpeech(String e) {
        speech = e;
    }

    public String getExpr() {
        return expression;
    }

    public String getLoc() {
        return location;
    }

    public String getFacing() {
        return facing;
    }

    public String getSpeech() {
        return speech;
    }

    public String getName() {
        return name;
    }
}