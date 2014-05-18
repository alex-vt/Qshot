package com.oleksiykovtun.qshot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * JavaFX 8 Window for taking a screen shot area with mouse cursor
 */
public class ScreenShotTakingWindow extends Application {
    private int screenWidth;
    private int screenHeight;
    private int selectionStartX;
    private int selectionStartY;

    private static String screenShotSavePathHead = "screenshot_";
    private static String screenShotSavePathTail = ".png";

    private static Stage primaryStage;


    public static void startHidden() {
        new Thread(() -> launch()).start();
    }

    public static void show() {
        Platform.setImplicitExit(false);
        Platform.runLater(() -> updateBackgroundAndShow());
    }


    private static void updateBackgroundAndShow() {
        if (! primaryStage.isShowing()) {
            try {
                getBackgroundCanvas().getGraphicsContext2D().drawImage(new ScreenShotImage().getWritableImage(), 0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.show();
        }
    }

    private static Canvas getBackgroundCanvas() {
        return (Canvas) primaryStage.getScene().getRoot().getChildrenUnmodifiable().get(0);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        screenWidth = (int)Screen.getPrimary().getBounds().getWidth();
        screenHeight = (int)Screen.getPrimary().getBounds().getHeight();

        Group canvasesGroup = new Group();

        Canvas backgroundCanvas = new Canvas(screenWidth, screenHeight);
        canvasesGroup.getChildren().add(backgroundCanvas);

        Canvas selectionCanvas = new Canvas(screenWidth, screenHeight);
        GraphicsContext selectionGraphicsContext = selectionCanvas.getGraphicsContext2D();
        selectionCanvas.setOnMouseMoved(MouseEvent -> drawSelectionCross(selectionGraphicsContext,
                (int) MouseEvent.getX(), (int) MouseEvent.getY()));
        selectionCanvas.setOnMousePressed(MouseEvent -> memorizeSelectionStartCoordinates(
                (int) MouseEvent.getX(), (int) MouseEvent.getY()));
        selectionCanvas.setOnMouseDragged(MouseEvent -> drawSelectionRectangle(selectionGraphicsContext,
                (int) MouseEvent.getX(), (int) MouseEvent.getY()));
        selectionCanvas.setOnMouseReleased(MouseEvent -> finishSelection(selectionGraphicsContext,
                (int) MouseEvent.getX(), (int) MouseEvent.getY()));
        canvasesGroup.getChildren().add(selectionCanvas);

        Scene scene = new Scene(canvasesGroup, Color.TRANSPARENT);
        scene.setOnKeyPressed(KeyEvent -> primaryStage.close());

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setScene(scene);

        this.primaryStage = primaryStage;
    }

    private void drawSelectionCross(GraphicsContext selection, int x, int y) {
        selection.clearRect(0, 0, screenWidth, screenHeight);
        selection.strokeLine(0, y, screenWidth, y);
        selection.strokeLine(x, 0, x, screenHeight);
    }

    private void memorizeSelectionStartCoordinates(int x, int y) {
        selectionStartX = x;
        selectionStartY = y;
    }

    private void drawSelectionRectangle(GraphicsContext selection, int x, int y) {
        selection.clearRect(0, 0, screenWidth, screenHeight);
        selection.strokeRect(Math.min(x, selectionStartX), Math.min(y, selectionStartY),
                Math.abs(x - selectionStartX), Math.abs(y - selectionStartY));
    }

    private void finishSelection(GraphicsContext selection, int x, int y) {
        primaryStage.hide();
        if (selectionStartX != x && selectionStartY != y) {
            takeScreenShot(x, y);
        }
        selection.clearRect(0, 0, screenWidth, screenHeight);
        System.gc();
    }

    private void takeScreenShot(int x, int y) {
        try {
            ScreenShotImage screenShot = new ScreenShotImage(getBackgroundCanvas().snapshot(null, null));
            screenShot = screenShot.crop(x, y, selectionStartX, selectionStartY);
            screenShot.copyToClipboard();
            screenShot.writeToFilePath(screenShotSavePathHead + System.currentTimeMillis() + screenShotSavePathTail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}