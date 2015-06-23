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
import java.io.InputStream;

/**
 *
 * @author Clara Currier
 */
public class DialogAS extends AbstractAppState implements ActionListener {

    private SimpleApplication appl;
    private InputManager inputManager;
    private AssetManager assetManager;
    private Dialog dialog;

    public DialogAS(SimpleApplication app, String dialogSource) throws Exception {
        appl = app;

        DialogReader reader = new DialogReader();
        InputStream is = new BufferedInputStream(new FileInputStream("assets/Scenes/" + dialogSource + ".txt"));
        dialog = reader.readMap(is);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        inputManager = appl.getInputManager();
        assetManager = appl.getAssetManager();

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