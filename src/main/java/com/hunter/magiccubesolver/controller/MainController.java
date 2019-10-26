package com.hunter.magiccubesolver.controller;

import com.hunter.magic_cube.cube_controller.PocketCubeMatrixController;
import com.hunter.magic_cube.model.Cube;
import com.hunter.magic_cube.model_factory.PocketCubeFactory;
import com.hunter.magic_cube.solver.PocketCubeSolver;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    @FXML
    private ChoiceBox<String> colorSelector;

    @FXML
    private GridPane tilePane;

    private List<Rectangle> tileComponents;

    private String[] colorListKeys;
    private HashMap<String, Paint> colorList;

    private Cube currentState;


    public MainController() {
        colorListKeys = new String[]{"RED", "YELLOW", "GREEN", "WHITE", "BLUE", "ORANGE"};

        colorList = new HashMap<>();
        colorList.put(colorListKeys[0], Paint.valueOf("#ff0000"));
        colorList.put(colorListKeys[1], Paint.valueOf("#ffff00"));
        colorList.put(colorListKeys[2], Paint.valueOf("#00ff00"));
        colorList.put(colorListKeys[3], Paint.valueOf("#ffffff"));
        colorList.put(colorListKeys[4], Paint.valueOf("#0000ff"));
        colorList.put(colorListKeys[5], Paint.valueOf("#ffa500"));

        tileComponents = new ArrayList<>();

        currentState = PocketCubeFactory.createSolvedPocketCube();
    }

    private void updateCubeView() {
        double[] tiles = currentState.getTiles();
        for (int i = 0; i < tiles.length && i < tileComponents.size(); i++) {
            tileComponents.get(i).setFill(colorList.get(colorListKeys[(int) tiles[i]]));
        }
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set default selected items
        colorSelector.setItems(FXCollections.observableArrayList(colorList.keySet()));
        colorSelector.setValue("RED");

        // store tile components
        tileComponents = tilePane.getChildren().stream()
                .filter(n -> n instanceof Rectangle)
                .map(n -> (Rectangle) n)
                .collect(Collectors.toList());

        // reset cube view
        updateCubeView();
    }

    @FXML
    public void tileClicked(Event e) {
        String selectedColorString = colorSelector.getValue();
        Rectangle targetTile = (Rectangle) e.getTarget();
        targetTile.setFill(colorList.get(selectedColorString)); // update color

        int colorIndex = Arrays.asList(colorListKeys).indexOf(selectedColorString);
        for (int i = 0; i < tileComponents.size(); i++) {
            if (tileComponents.get(i) == targetTile) {
                currentState.setTileValueByIndex(i, colorIndex);
            }
        }
    }

    @FXML
    public void solveClicked(Event e) {
        try {
            // standardize colors of currentState
            Cube c = PocketCubeFactory.standardizePocketCube(currentState);
            PocketCubeSolver solver = new PocketCubeSolver();

            var solution = solver.solve(c);

            if (solution.size() == 0) {
                displayPopup("Already done!");
            } else {
                List<String> solutionInString = new ArrayList<>();
                for (var step : solution) {
                    solutionInString.add(step.getHow());
                }
                displayPopup(String.join(" ", solutionInString));
            }
        } catch (RuntimeException ex) {
            displayPopup(ex.getMessage());
        }
    }

    @FXML
    private void reset(Event e){
        Cube solvedState = PocketCubeFactory.createSolvedPocketCube();
        currentState = solvedState;
        updateCubeView();
    }

    @FXML
    private void rotateClicked(Event e) {
        // if(!(e.getTarget() instanceof Button)) return;

        var rotationController = PocketCubeMatrixController.getController();
        Button target = (Button) e.getTarget();

        double[] state = currentState.getTiles();
        double[][] currentStates = new double[][]{state};

        if (target.getText().equals(rotationController.FRONT_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateFrontClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.FRONT_COUNTER_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateFrontCounterClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.FRONT_CLOCKWISE_2X_NOTATION)) {
            state = rotationController.rotateFrontClockwise2x(currentStates)[0];
        } else if (target.getText().equals(rotationController.RIGHT_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateRightClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.RIGHT_COUNTER_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateRightCounterClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.RIGHT_CLOCKWISE_2X_NOTATION)) {
            state = rotationController.rotateRightClockwise2x(currentStates)[0];
        } else if (target.getText().equals(rotationController.UP_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateUpClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.UP_COUNTER_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateUpCounterClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.UP_CLOCKWISE_2X_NOTATION)) {
            state = rotationController.rotateUpClockwise2x(currentStates)[0];
        }else if (target.getText().equals(rotationController.BACK_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateBackClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.BACK_COUNTER_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateBackCounterClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.BACK_CLOCKWISE_2X_NOTATION)) {
            state = rotationController.rotateBackClockwise2x(currentStates)[0];
        }else if (target.getText().equals(rotationController.LEFT_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateLeftClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.LEFT_COUNTER_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateLeftCounterClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.LEFT_CLOCKWISE_2X_NOTATION)) {
            state = rotationController.rotateLeftClockwise2x(currentStates)[0];
        }else if (target.getText().equals(rotationController.DOWN_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateDownClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.DOWN_COUNTER_CLOCKWISE_NOTATION)) {
            state = rotationController.rotateDownCounterClockwise(currentStates)[0];
        } else if (target.getText().equals(rotationController.DOWN_CLOCKWISE_2X_NOTATION)) {
            state = rotationController.rotateDownClockwise2x(currentStates)[0];
        } else {
            System.out.println("Unknown rotation operation -- " + target.getText());
            return;
        }

        currentState = new Cube(state, 0);
        updateCubeView();
    }

    private void displayPopup(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/Popup.fxml")
            );
            Parent root = loader.load();

            // get and update controller
            PopupController controller = loader.<PopupController>getController();
            controller.changeLabelText(message);

            Stage stage = new Stage();
            stage.setTitle("Solution");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
