/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Clara Currier
 */
public class DialogReader {

    private DefaultHandler currentHandler = new TMXHandler();
    private Dialog dialog = null;
    private HashMap<String, Character> characters = new HashMap<>();
    private ArrayList<Character> currentFrame = new ArrayList<>();

    // SAX library don't have SAXParser.setHandler(DefaultHandler) method, 
    //thats why we will use this workaround stub to parse hierarchy 
    private class SAXStub extends DefaultHandler {

        @Override
        public void startElement(String uri, String name, String qName, Attributes attributes) throws SAXException {
            currentHandler.startElement(uri, name, qName, attributes);
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            currentHandler.endElement(uri, name, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            currentHandler.characters(ch, start, length);
        }
    }

    private class TMXHandler extends DefaultHandler {

        @Override
        public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
            if (qName.equalsIgnoreCase("dialog")) {
                dialog = new Dialog();
                for (int i = 1; i < 5; i++) {
                    if (!getString(atts, "char" + i).equals("")) {
                        String charName = getString(atts, "char" + i);
                        dialog.addUniqueChar(charName);
                        characters.put(charName, new Character(charName));
                    }
                }
                currentHandler = new DialogHandler(this);
            }
        }
    }

    private class DialogHandler extends DefaultHandler {

        private DefaultHandler previousHandler;

        public DialogHandler(DefaultHandler previousHandler) {
            this.previousHandler = previousHandler;
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {

            if (qName.equalsIgnoreCase("line")) {
                Character newChar = new Character(getString(atts, "char"));
                newChar.setExpr(getString(atts, "expr"));
                newChar.setFacing(getString(atts, "facing"));
                newChar.setLoc(getString(atts, "loc"));
                newChar.setSpeech(getString(atts, "speech"));
                currentFrame.add(newChar);
                //terminate frame
                dialog.addFrame(new ArrayList<>(currentFrame));
                currentFrame.removeAll(currentFrame);

            } else if (qName.equalsIgnoreCase("nonspeaker")) {
                Character newChar = new Character(getString(atts, "char"));
                newChar.setExpr(getString(atts, "expr"));
                newChar.setFacing(getString(atts, "facing"));
                newChar.setLoc(getString(atts, "loc"));
                currentFrame.add(newChar);

            } else {
                throw new SAXException("unknown tag:" + qName);
            }
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("dialog")) {
                currentHandler = previousHandler;
            }
        }
    }

    private int getInteger(Attributes atts, String name) {
        String value = atts.getValue(name);
        if (value != null) {
            return (Integer.valueOf(value));
        }
        return 0;
    }

    private String getString(Attributes atts, String name) {
        String value = atts.getValue(name);
        if (value == null) {
            return ("");
        }
        return value;
    }

    public Dialog readFile(InputStream IS) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(IS, new SAXStub());
        return (dialog);
    }
}