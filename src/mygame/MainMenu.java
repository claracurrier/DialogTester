/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author Clara Currier
 */
public class MainMenu extends SimpleApplication implements ActionListener {

    public static void main(String[] args) {
        MainMenu app = new MainMenu();
        AppSettings newSettings = new AppSettings(true);
        newSettings.setFrameRate(60);
        app.setSettings(newSettings);
        app.start();
    }
    private static Screen screen;
    private Node tgGuiNode;
    private static boolean ispaused = false;
    private PauseMenu pauseMenu;
    private OptionsMenu optionsMenu;

    @Override
    public void simpleInitApp() {
        getFlyByCamera().setEnabled(false); //enables mouse

        tgGuiNode = new Node("tgGuiNode");
        screen = new Screen(this);
        guiNode.attachChild(tgGuiNode);
        tgGuiNode.addControl(screen);


        pauseMenu = new PauseMenu(screen, stateManager, this);
        optionsMenu = new OptionsMenu(screen, this, settings);
        makeStartMenu();
    }

    public PauseMenu getPM() {
        return pauseMenu;
    }

    public OptionsMenu getOM() {
        return optionsMenu;
    }

    public static Screen getScreen() {
        return screen;
    }

    public void makeStartMenu() {
        if (!inputManager.hasMapping("pause")) {
            inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
            inputManager.addListener(this, "pause");
        }

        final Window win = new Window(screen, "win", new Vector2f(15, 15),
                new Vector2f(settings.getWidth() - 30, settings.getHeight() - 30));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setWindowIsMovable(false);
        win.setIgnoreMouse(true);

        //start
        MyButton startDialog = new MyButton(screen, "Start", new Vector2f(15, 55)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                DialogAS dialogAS = new DialogAS();
                stateManager.attach(dialogAS);
                screen.removeElement(win);

            }
        };
        startDialog.setFont("Interface/Fonts/Arial.fnt");
        startDialog.setText("Load Dialog");
        startDialog.setTextAlign(BitmapFont.Align.Center);
        win.addChild(startDialog);
        
        //Validate
        MyButton validateBtn = new MyButton(screen, "Validate",
                new Vector2f(15, 105)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                requestDialog(true);
            }
        };
        validateBtn.setFont("Interface/Fonts/Arial.fnt");
        validateBtn.setText("Options");
        validateBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(validateBtn);
        
        //Options
        MyButton optionMenuBtn = new MyButton(screen, "Options",
                new Vector2f(15, 155)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                goToOptions();
            }
        };
        optionMenuBtn.setFont("Interface/Fonts/Arial.fnt");
        optionMenuBtn.setText("Options");
        optionMenuBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(optionMenuBtn);

        //Exit
        MyButton exitBtn = new MyButton(screen, "Exit",
                new Vector2f(15, 255)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                app.stop();
            }
        };
        exitBtn.setFont("Interface/Fonts/Arial.fnt");
        exitBtn.setText("Exit");
        exitBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(exitBtn);
        ispaused = false;
    }
    
    private void requestDialog(boolean debug){
        if(debug){
            //do extra debugging stuff
        }
        else{
            //read xml
        }
    }

    private void pause() {
        DialogAS bM = stateManager.getState(DialogAS.class);
        if (bM != null) {
            bM.setEnabled(false);
            ispaused = true;
            pauseMenu.makePauseMenu();
        }
    }

    public static boolean isPaused() {
        return ispaused;
    }

    public void setPaused(boolean p) {
        ispaused = p;
    }
    
    private void goToOptions() {
        optionsMenu.makeOptionsMenu();
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("pause") && !isPressed && !ispaused) {
            pause();
        }
    }
}