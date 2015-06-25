/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import java.util.logging.Level;
import java.util.logging.Logger;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
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
        cam.setParallelProjection(true);

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

        final Window win = new Window(screen, "MainWin", new Vector2f(15, 15),
                new Vector2f(settings.getWidth() - 30, settings.getHeight() - 30));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setWindowIsMovable(false);
        win.setIgnoreMouse(true);

        //start
        MyButton startDialog = new MyButton(screen, "Start", new Vector2f(15, 55)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                makeTextField(false);
            }
        };
        startDialog.setFont("Interface/Fonts/Arial.fnt");
        startDialog.setText("Load");
        startDialog.setTextAlign(BitmapFont.Align.Center);
        win.addChild(startDialog);

        //Validate
        MyButton validateBtn = new MyButton(screen, "Validate",
                new Vector2f(15, 105)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                makeTextField(true);
            }
        };
        validateBtn.setFont("Interface/Fonts/Arial.fnt");
        validateBtn.setText("Validate");
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

    private void makeTextField(final boolean debug) {
        int w = settings.getWidth();
        int h = settings.getHeight();
        final Window win = new Window(screen, "textFieldWin", new Vector2f(w / 2 - 100, h / 2 - 100),
                new Vector2f(250, 200));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setWindowIsMovable(false);
        win.setIgnoreMouse(true);

        //Textfield
        final TextField textfield = new TextField(screen, "textField", new Vector2f(15, 15),
                new Vector2f(200, 30));
        win.addChild(textfield);
        textfield.onGetFocus(null);

        //Back
        MyButton backBtn = new MyButton(screen, "textFieldBack",
                new Vector2f(15, 140)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
            }
        };
        backBtn.setFont("Interface/Fonts/Arial.fnt");
        backBtn.setText("Cancel");
        backBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(backBtn);

        //Load
        MyButton loadBtn = new MyButton(screen, "textFieldLoad",
                new Vector2f(120, 140)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                DialogAS dialogAS;
                try {
                    dialogAS = new DialogAS((SimpleApplication) app, textfield.getText(),
                            settings, screen);
                    if (!debug) {
                        screen.removeElement(win);
                        screen.removeElement(screen.getElementById("MainWin"));
                        stateManager.attach(dialogAS);
                    } else {
                        makeErrorWindow(null);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    makeErrorWindow(ex);
                }

            }
        };
        loadBtn.setFont("Interface/Fonts/Arial.fnt");
        loadBtn.setText("Load");
        loadBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(loadBtn);
    }

    private void makeErrorWindow(Exception e) {
        final Window win = new Window(screen, "controlwin", new Vector2f(15, 15),
                new Vector2f(350, 200));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setIgnoreMouse(true);
        win.setWindowIsMovable(false);

        Label txtbox = new Label(screen, "controltext", new Vector2f(15, 15),
                new Vector2f(350, 110));
        txtbox.setFont("Interface/Fonts/Arial.fnt");
        txtbox.setTextAlign(BitmapFont.Align.Left);
        txtbox.setTextWrap(LineWrapMode.Word);
        txtbox.setFontSize(18f);
        if (e != null) {
            txtbox.setText("There was an error!"
                    + "\n" + e.getMessage());
        } else {
            txtbox.setText("No errors while reading");
        }
        win.addChild(txtbox);

        //go back button
        MyButton goBackBtn = new MyButton(screen, "ControlGoBack",
                new Vector2f(15, 120)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
            }
        };
        goBackBtn.setFont("Interface/Fonts/Arial.fnt");
        goBackBtn.setText("Back");
        goBackBtn.setTextAlign(BitmapFont.Align.Center);
        win.addChild(goBackBtn);
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