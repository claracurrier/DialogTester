/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author Clara Currier
 */
public class PauseMenu {

    private Screen screen;
    private final AppStateManager asmr;
    private final MainMenu mm;

    public PauseMenu(Screen screen, AppStateManager asm, Application app) {
        this.screen = screen;
        this.asmr = asm;
        mm = (MainMenu) app;
    }

    public void makePauseMenu() {
        final Window win = new Window(screen, "pausewin", new Vector2f(15, 15),
                new Vector2f(100, 300));
        screen.addElement(win);
        win.setIsResizable(false);
        win.setWindowIsMovable(false);
        win.setIgnoreMouse(true);

        //resume button
        MyButton resumeGame = new MyButton(screen, "PauseResume",
                new Vector2f(15, 55), new Vector2f(100, 35)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                resume();
            }
        };
        resumeGame.setFont("Interface/Fonts/Arial.fnt");
        resumeGame.setText("Resume");
        resumeGame.setTextAlign(BitmapFont.Align.Center);
        win.addChild(resumeGame);

        //options
        MyButton optionMenuBtn = new MyButton(screen, "PauseOptions",
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

        //exit button
        MyButton exitToMenu = new MyButton(screen, "PauseExitToMenu",
                new Vector2f(15, 205), new Vector2f(200, 35)) {
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
                screen.removeElement(win);
                goToMM();
            }
        };
        exitToMenu.setFont("Interface/Fonts/Arial.fnt");
        exitToMenu.setText("Return to Main Menu");
        exitToMenu.setTextAlign(BitmapFont.Align.Center);
        win.addChild(exitToMenu);
    }

    public void goToMM() {
        asmr.detach(asmr.getState(DialogAS.class));
        mm.makeStartMenu();
    }

    private void goToOptions() {
        mm.setPaused(true);
        mm.getOM().makeOptionsMenu();
    }

    public void resume() {
        screen.removeElement(screen.getElementById("pausewin"));
        asmr.getState(DialogAS.class).setEnabled(true);
        mm.setPaused(false);
    }
}