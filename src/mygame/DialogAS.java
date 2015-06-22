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
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Clara Currier
 */
public class DialogAS extends AbstractAppState implements ActionListener {

    private SimpleApplication appl;
    private InputManager inputManager;
    private AssetManager assetManager;
    private Dialog dialog;

    public DialogAS(SimpleApplication app, String dialogSource) {
        appl = app;
        inputManager = appl.getInputManager();
        assetManager = appl.getAssetManager();

        DialogReader reader = new DialogReader();
        InputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream("assets/Scenes/" + dialogSource + ".txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DialogAS.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dialog = reader.readMap(is);
        } catch (Exception ex) {
            Logger.getLogger(DialogAS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
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
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("continue")) {
            nextLine();
        }
    }

    private void nextLine() {
        //calls all the dialog stuff
    }
}