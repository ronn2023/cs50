package org.headroyce.ronn2023;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The entire workspace of the application.
 *
 * @author Brian Sea
 */
public class MainWorkspace extends BorderPane {

    private DrawingWorkspace drawingArea;
    private VBox toolPalette;

    // All the registered tools
    // (Tool_Name -> Constructor)
    private HashMap<String, Constructor<? extends Tool>> registeredTools;

    public MainWorkspace(){
        ArrayList<Class<? extends Tool>> tools = new ArrayList<>();
        tools.add(LineTool.class);

        registeredTools = new HashMap<>();
        drawingArea = new DrawingWorkspace();
        toolPalette = new VBox();

        // Register all the currently supported tools
        for( Class<? extends Tool> tool : tools ){
            try {

                // Grab the tool's name
                Method method = tool.getMethod("toolName");
                String toolname = (String)method.invoke(null);
                Constructor<? extends Tool> con = tool.getConstructor(Canvas.class);
                registeredTools.put(toolname, con);

                // Grab the GUI for the tool's selection area and attach it to the main GUI
                method = tool.getMethod("renderTool");
                Node toolGUI = (Node)method.invoke(null);

                // Handle pressing delete key to remove selectedShape
                toolGUI.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.BACK_SPACE){
                            drawingArea.deleteSelectedShape();
                        }
                    }
                });
                // Handle switching to a tool and changing the status to indicate which tools we're on
                toolGUI.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        if( drawingArea.getActiveTool() == con ){
                            drawingArea.setActiveTool(null);
                            toolGUI.getStyleClass().remove("active");
                            drawingArea.setModify();

                        }
                        else {
                            drawingArea.setActiveTool(con);
                            toolGUI.getStyleClass().add("active");
                        }
                    }
                });


                toolPalette.getChildren().add(toolGUI);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            toolPalette.setAlignment(Pos.CENTER);

            this.setLeft(toolPalette);

            //sets clearAll button to the right of the pane for aesthetics
            this.setRight(drawingArea.clearAll);

            this.setCenter(drawingArea);
        }

    }
}
