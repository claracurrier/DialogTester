/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.InputStream;

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
                currentHandler = new CharacterHandler(this);

            } else if (qName.equalsIgnoreCase("nonspeaker")) {
                currentHandler = new NonSpeakerHandler(this);

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

    private class CharacterHandler extends DefaultHandler {

        private DefaultHandler previousHandler;

        public CharacterHandler(DefaultHandler previousHandler) {
            this.previousHandler = previousHandler;
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
            if (qName.equalsIgnoreCase("char")) {
                dialog.addChar(getString(atts, "char"));
            } else if (qName.equalsIgnoreCase("expr")) {
                dialog.addExpr(getString(atts, "expr"));
            } else if (qName.equalsIgnoreCase("facing")) {
                dialog.addFacing(getString(atts, "facing"));
            } else if (qName.equalsIgnoreCase("loc")) {
                dialog.addLoc(getString(atts, "loc"));
            } else if (qName.equalsIgnoreCase("speech")) {
                dialog.addSpeech(getString(atts, "speech"));
            } else {
                throw new SAXException("unknown tag:" + qName);
            }
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("s")) {
                currentHandler = previousHandler;
            }
        }
    }

    //don't use right now, needs a way to time
    private class NonSpeakerHandler extends DefaultHandler {

        private DefaultHandler previousHandler;

        public NonSpeakerHandler(DefaultHandler previousHandler) {
            this.previousHandler = previousHandler;
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
            if (qName.equalsIgnoreCase("char")) {
                dialog.addChar(getString(atts, "char"));
            } else if (qName.equalsIgnoreCase("expr")) {
                dialog.addExpr(getString(atts, "expr"));
            } else if (qName.equalsIgnoreCase("facing")) {
                dialog.addFacing(getString(atts, "facing"));
            } else if (qName.equalsIgnoreCase("loc")) {
                dialog.addLoc(getString(atts, "loc"));
            } else {
                throw new SAXException("unknown tag:" + qName);
            }
        }

        @Override
        public void endElement(String uri, String name, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("ns")) {
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

    public Dialog readMap(InputStream IS) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(IS, new SAXStub());
        return (dialog);
    }
}