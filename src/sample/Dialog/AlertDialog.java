package sample.Dialog;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import java.awt.*;

public class AlertDialog {
    public static void display(String title, String message){
        Stage window = new Stage();
        window.setTitle(title);
        window.setMinWidth(500);
        window.setMinHeight(200);

        Label label = new Label();
        label.setText(message);
        Button button = new Button("OK");
        button.setOnAction(e-> window.close());

        VBox layout = new VBox(5);
        layout.getChildren().addAll(label,button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
