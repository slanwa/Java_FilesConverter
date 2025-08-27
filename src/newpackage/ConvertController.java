/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package newpackage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import javafx.scene.control.ButtonType;
import javax.imageio.ImageIO;


/**
 * FXML Controller class
 *
 * @author Yunotoshi Kirma
 */
public class ConvertController implements Initializable {

    @FXML
    private AnchorPane background;
    @FXML
    private TabPane backgroundPane;
    @FXML
    private Button BrowseJPG;
    @FXML
    private Button BrowsePNG;
    @FXML
    private Button ConvertImages;

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lblPathToJpg;
    @FXML
    private Label lblPathToPng;
    @FXML
    private Label LabelJPG;
    @FXML
    private Label LabelPNG;


//    @FXML
//    private void hoverLabels(MouseDragEvent event) {
//        // Tooltip for JPG conversion
//        Tooltip tooltipJPG = new Tooltip(
//            "Supported formats for JPG conversion:\n" +
//            "PNG, GIF, BMP, TIFF, WEBP\n" +
//            "(Any image that is NOT already JPG/JPEG)"
//        );
//        tooltipJPG.setStyle("-fx-font-size: 12px; -fx-background-color: rgba(230,230,250,0.9); -fx-text-fill: black;");
//
//        // Tooltip for PNG conversion
//        Tooltip tooltipPNG = new Tooltip(
//            "Supported formats for PNG conversion:\n" +
//            "JPG, JPEG, GIF, BMP, TIFF, WEBP\n" +
//            "(Any image that is NOT already PNG)"
//        );
//        tooltipPNG.setStyle("-fx-font-size: 12px; -fx-background-color: rgba(230,230,250,0.9); -fx-text-fill: black;");
//
//        // Link tooltips to labels
//        LabelJPG.setTooltip(tooltipJPG);
//        LabelPNG.setTooltip(tooltipPNG);
//    }

    @FXML
    private void hoverLabels(MouseEvent event) {
        // Tooltip for JPG conversion
        Tooltip tooltipJPG = new Tooltip(
            "Supported formats for JPG conversion:\n" +
            "PNG, GIF, BMP, TIFF, WEBP\n" +
            "(Any image that is NOT already JPG/JPEG)"
        );
        tooltipJPG.setStyle("-fx-font-size: 12px; -fx-background-color: rgba(230,230,250,0.9); -fx-text-fill: black;");

        // Tooltip for PNG conversion
        Tooltip tooltipPNG = new Tooltip(
            "Supported formats for PNG conversion:\n" +
            "JPG, JPEG, GIF, BMP, TIFF, WEBP\n" +
            "(Any image that is NOT already PNG)"
        );
        tooltipPNG.setStyle("-fx-font-size: 12px; -fx-background-color: rgba(230,230,250,0.9); -fx-text-fill: black;");
        
        tooltipJPG.setShowDelay(Duration.ZERO);
//        tooltipJPG.setHideDelay(Duration.INDEFINITE);
        tooltipJPG.setShowDuration(Duration.INDEFINITE);
        
        tooltipPNG.setShowDelay(Duration.ZERO);
//        tooltipPNG.setHideDelay(Duration.INDEFINITE);
        tooltipPNG.setShowDuration(Duration.INDEFINITE);

        // Link tooltips to labels
        LabelJPG.setTooltip(tooltipJPG);
        LabelPNG.setTooltip(tooltipPNG);
    }

    @FXML
    private void ConvertImages(ActionEvent event) {
        if (selectedInput == null || target == null) {
            showError("Please select an input image and a target format.");
            return;
        }

        try {
            Path outputDir = selectedInput.getParent();
            String baseName = stripExtension(selectedInput.getFileName().toString());
            String outFileName;

            if (target == Target.JPG) {
                outFileName = baseName + "_converted.jpg";
            } else if (target == Target.PNG) {
                outFileName = baseName + "_converted.png";
            } else {
                showError("Unsupported target format: " + target);
                return;
            }

            File outFile = outputDir.resolve(outFileName).toFile();

            // Build ImageMagick command
            ProcessBuilder pb = new ProcessBuilder(
            "C:\\Program Files\\ImageMagick-7.1.2-Q16\\magick.exe",
            selectedInput.toString(),
            outFile.getAbsolutePath()
            );


            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                showInfo("Conversion successful:\n" + outFile.getAbsolutePath());
            } else {
                showError("ImageMagick failed. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            showError("Conversion failed:\n" + e.getMessage());
        }
    }


    // Helper methods for cleaner code
    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }

    private String stripExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return (i > 0) ? fileName.substring(0, i) : fileName;
    }

    private Window getWindow() {
        // Use any control to fetch a window; adapt if you store a reference differently
        return (ConvertImages != null) ? ConvertImages.getScene().getWindow() : null;
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }




    private enum Target { JPG, PNG }
    private Target target;                 // remembers what we’re converting to
    private Path selectedInput;   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void browseJPG(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select an image to convert to JPG");
        // Allow only NON-JPG formats:
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Images (not JPG)", "*.png", "*.bmp", "*.gif", "*.tif", "*.tiff", "*.webp", "*.svg", "*.ico", "*.heif"));
        File f = fc.showOpenDialog(BrowseJPG.getScene().getWindow());

        if (f != null) {
            // extra safety: reject if already jpg/jpeg
            String name = f.getName().toLowerCase();
            if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            new Alert(Alert.AlertType.ERROR, "Some message").showAndWait();
                return;
            }
            selectedInput = f.toPath();
            target = Target.JPG;
            if (lblPathToJpg != null) lblPathToJpg.setText(selectedInput.toString());
            if (lblPathToPng != null) lblPathToPng.setText("");
        }
    }

    @FXML
    private void browsePNG(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select an image to convert to PNG");
        // Allow only NON-PNG formats:
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Images (not PNG)", "*.jpg", "*.jpeg", "*.bmp", "*.gif", "*.tif", "*.tiff", "*.webp", "*.svg", "*.ico", "*.heif"));
        File f = fc.showOpenDialog(BrowseJPG.getScene().getWindow());
        if (f != null) {
            // extra safety: reject if already png
            String name = f.getName().toLowerCase();
            if (name.endsWith(".png")) {
            new Alert(Alert.AlertType.ERROR, "Some message").showAndWait();
                return;
            }
            selectedInput = f.toPath();
            target = Target.PNG;
            if (lblPathToPng != null) lblPathToPng.setText(selectedInput.toString());
            if (lblPathToJpg != null) lblPathToJpg.setText("");
        }
    }
    
}
