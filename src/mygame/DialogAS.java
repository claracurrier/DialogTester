/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.Screen;

/**
 *
 * @author Clara Currier
 */
public class DialogAS extends AbstractAppState implements ActionListener {
    
    private SimpleApplication appl;
    private InputManager inputManager;
    private AssetManager assetManager;
    private Dialog dialog;
    private AppSettings settings;
    private Screen screen;
    private Window win;
    private Label txtbox;
    private HashMap<String, Node> charNodes = new HashMap<>();
    
    public DialogAS(SimpleApplication app, String dialogSource, AppSettings set, ElementManager s) throws Exception {
        appl = app;
        settings = set;
        screen = (Screen) s;
        
        DialogReader reader = new DialogReader();
        InputStream is = new BufferedInputStream(new FileInputStream("assets/Scenes/" + dialogSource + ".txt"));
        dialog = reader.readFile(is);
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        inputManager = appl.getInputManager();
        assetManager = appl.getAssetManager();
        
        win = new Window(screen, "dialogWin",
                new Vector2f(50, settings.getHeight() - 160),
                new Vector2f(450, 150));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setWindowIsMovable(false);
        win.setIgnoreMouse(true);
        
        txtbox = new Label(screen, "dialogText", new Vector2f(15, 15),
                new Vector2f(385, 125));
        txtbox.setFont("Interface/Fonts/Arial.fnt");
        txtbox.setTextAlign(BitmapFont.Align.Left);
        txtbox.setTextWrap(LineWrapMode.Word);
        txtbox.setFontSize(18f);
        win.addChild(txtbox);
        
        for (String chars : dialog.getUniqueChars()) {
            Node portrait = new Node(chars);
            Geometry geom = new Geometry("Quad", new Quad(200f, 400f));
            Texture tex = assetManager.loadTexture("Textures/" + chars + "_neutral.png");
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setTexture("ColorMap", tex);
            geom.setMaterial(mat);
            portrait.attachChild(geom);
            appl.getGuiNode().attachChild(portrait);
            charNodes.put(chars, portrait);
        }
        setEnabled(true);
        nextLine();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            if (!inputManager.hasMapping("continue")) {
                inputManager.addMapping("continue", new KeyTrigger(KeyInput.KEY_RETURN));
                inputManager.addListener(this, "continue");
            }
        } else {
            if (inputManager.hasMapping("continue")) {
                inputManager.deleteMapping("continue");
                inputManager.removeListener(this);
            }
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        setEnabled(false);
        appl.getGuiNode().detachAllChildren();
        screen.removeElement(win);
        screen.removeElement(txtbox);
    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("continue") && !isPressed) {
            nextLine();
        }
    }
    
    private void nextLine() {
        if (dialog.hasNext()) {
            Node curCharNode = charNodes.get(dialog.curChar());

            //update the box's nameplate

            if (!dialog.curExpr().equals("")) {
                //update the portrait
                Texture tex = assetManager.loadTexture("Textures/"
                        + dialog.curChar() + "_" + dialog.curExpr() + ".png");
                ((Geometry) curCharNode.getChild("Quad"))
                        .getMaterial().setTexture("ColorMap", tex);
            }
            if (!dialog.curFace().equals("")) {
                //update direction of the portrait
                if (dialog.curFace().equals("right")) {
                    //curCharNode.setLocalScale(-1f, 1f, 1f);
                } else { //left, default drawing
                    curCharNode.setLocalScale(1f, 1f, 1f);
                }
            }
            if (!dialog.curLoc().equals("")) {
                //update the location of the portrait
                if (dialog.curLoc().equals("right")) {
                    curCharNode.setLocalTranslation(settings.getWidth() - 200f, 0f, -1f);
                } else { //left, may need to add more for positions
                    curCharNode.setLocalTranslation(0f, 0f, -1f);
                }
            }
            //update the box's speech text
            txtbox.setText(dialog.curSpeech());
            dialog.next();
            
        } else {
            //end the dialog
        }
    }
}