import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Teast extends Application {

    private static final double LED_RADIUS = 20;
    private static final double SOURCE_WIDTH = 30;
    private static final double SOURCE_HEIGHT = 15;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dynamic LED Connection");
        // create the ordered led  then get the center x and center y from the LEDs who can light
        //create the sours circle , if the source value equals with one of LED can light using center x and center y, draw the line
        // Get input for the number of LEDs and their order
        int numberOfLEDs = 6;
        List<Integer> orderedLEDs = Arrays.asList(2, 6, 3, 5, 4, 1); // Example order
        int [] ledsCanLigth = {2,3,6};
        Group root = new Group();
        double [][] cordinateLEDCanLight = new double[numberOfLEDs+12][16];
     /*   for (int i = 0; i < cordinateLEDCanLight.length; i++) {
            for (int j = 0; j < 2; j++) {
                cordinateLEDCanLight[i][j] =0;
            }
        }*/
        // Create LEDs with labels
        for (int i = 0; i < numberOfLEDs; i++) {
            double x = 50 + i * 80;
            double y = 100;

            Circle led = createLED(x, y);
            root.getChildren().add(led);

          //  if ( i < ledsCanLigth.length ){
                for (int j = 0; j < ledsCanLigth.length; j++) {
                    if (ledsCanLigth[j] == orderedLEDs.get(i)) { // i+1 is the number of led can light
                        cordinateLEDCanLight[ledsCanLigth[j]][0]=x;// like [1][0] for x & [1][1] for y
                        cordinateLEDCanLight[ledsCanLigth[j]][1]=y;
                    }
                }
          //  }
            // Add label with LED index
            Label label = createLabel(Integer.toString(orderedLEDs.get(i)), x, y);
            root.getChildren().add(label);
        }

        // Determine which LEDs can be lit (for example, the first three)
        List<Integer> litLEDs = orderedLEDs.subList(0, 3);

        // Get the center coordinates of the lit LEDs
        List<double[]> litLEDsCenters = new ArrayList<>();
        for (int litLED : litLEDs) {
            double x = 50 + (litLED - 1) * 80 + LED_RADIUS;
            double y = 100 + LED_RADIUS;
            litLEDsCenters.add(new double[]{x, y});
        }

        // Create source rectangles and connect to lit LEDs
        for (int i = 0; i < numberOfLEDs; i++) {
            double x = 50 + i * 80;
            double y = 300;

            Rectangle source = createSource(x, y);
            root.getChildren().add(source);

            // Add label with source index
            Label label = createLabel("Source " + (i + 1), x + SOURCE_WIDTH / 2, y - 10);
            root.getChildren().add(label);

            // Check if the source corresponds to a lit LED
           // if ( i < ledsCanLigth.length  ) {
                double endX =0;
                double endY = 0;
                byte check = 0;
                for (int j = 0; j < ledsCanLigth.length; j++) {
                    if (ledsCanLigth[j] == i + 1) {
                        check = 1;
                        endX = cordinateLEDCanLight[ledsCanLigth[j]][0];
                        endY = cordinateLEDCanLight[ledsCanLigth[j]][1];
                        break;
                    }
                }
                if (check == 1) {
                    // Connect source to lit LED with an animated line
                    double startX = source.getX() + SOURCE_WIDTH / 2;
                    double startY = source.getY() + SOURCE_HEIGHT / 2;

                    //double[] litLEDCenter = litLEDsCenters.get(litLEDs.indexOf(i + 1));



                    Line line = new Line(startX, startY, startX, startY);
                    root.getChildren().add(line);

                    // Animate the line
                    animateLine(line, startX, startY, endX, endY);
                }

           // }
        }

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private Circle createLED(double x, double y) {
        Circle led = new Circle(x, y, LED_RADIUS, Color.MEDIUMAQUAMARINE);

        // Add event handling to allow dragging
        led.setOnMouseDragged(event -> {
            led.setCenterX(event.getX());
            led.setCenterY(event.getY());
        });

        return led;
    }

    private Rectangle createSource(double x, double y) {
        Rectangle source = new Rectangle(x, y, SOURCE_WIDTH, SOURCE_HEIGHT);
        source.setFill(Color.DARKGREY);
        return source;
    }

    private Label createLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setLayoutX(x - 10); // Adjust for label width
        label.setLayoutY(y - 10); // Adjust for label height
        return label;
    }

    private void animateLine(Line line, double startX, double startY, double endX, double endY) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(line.endXProperty(), startX), new KeyValue(line.endYProperty(), startY)),
                new KeyFrame(Duration.seconds(1), new KeyValue(line.endXProperty(), endX), new KeyValue(line.endYProperty(), endY))
        );
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
