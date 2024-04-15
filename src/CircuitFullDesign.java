import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;



public class CircuitFullDesign {
    private static final double LED_RADIUS = 14;
    private   int numberOfLEDs ;
    private   int[] ledsOrder;
    private int [] ledsCanLight; // it got it's values from invoc the obj

    public CircuitFullDesign() {
      numberOfLEDs=0;
    }

    public int getNumberOfLEDs() {
        return numberOfLEDs;
    }

    public void setNumberOfLEDs(int numberOfLEDs) {
        this.numberOfLEDs = numberOfLEDs;
    }

    public int[] getLedsCanLight() {
        return ledsCanLight;
    }

    public void setLedsCanLight(int[] ledsCanLight) {
        this.ledsCanLight = ledsCanLight;
    }

    public int[] getLedsOrder() {
        return ledsOrder;
    }

    public void setLedsOrder(int[] ledsOrder) {
        this.ledsOrder = ledsOrder;
    }
//createLEDsAndSources
    private static final double SOURCE_WIDTH = 30;
    private static final double SOURCE_HEIGHT = 15;


    public Scene createLEDsAndSources() {
        // create the ordered led  then get the center x and center y from the LEDs who can light
        //create the sours circle , if the source value equals with one of LED can light using center x and center y, draw the line


        Group root = new Group();
        double [][] coordinateLEDCanLight = new double[numberOfLEDs+1][2];// 2 because I want to use 0 & 1
                                                                        // [for value of LED lights ][for x,y coordinate]
        // Create LEDs with labels
        for (int i = 0; i < numberOfLEDs; i++) {
            double x = 50 + i * 80;
            double y = 100;

            Circle led = createLED(x, y);
            root.getChildren().add(led);

            for (int j = 0; j < ledsCanLight.length; j++) { // To  get the center x and center y from the LEDs who can light
                if (ledsCanLight[j] == ledsOrder[i]) {
                    coordinateLEDCanLight[ledsCanLight[j]][0]=x;// like [1][0] for x & [1][1] for y
                    coordinateLEDCanLight[ledsCanLight[j]][1]=y;
                }
            }
            // Add label with every LED
            Label label = createLabel(Integer.toString(ledsOrder[i]), x, y);
            root.getChildren().add(label);
        }

        // Create source (rectangle) then connect to LEDs can light
        for (int i = 0; i < numberOfLEDs; i++) {
            double x = 50 + i * 80;
            double y = 300;

            Rectangle source = createSource(x, y);
            root.getChildren().add(source);

            // Add label with source rec
            Label label = createLabel("Source " + (i + 1), x + SOURCE_WIDTH / 2, y - 10);
            root.getChildren().add(label);

            // Check if the source equals to a LEDs can light
            // Initial values
            double endX =0;
            double endY = 0;
            for (int j = 0; j < ledsCanLight.length; j++) {
                if (ledsCanLight[j] == i + 1) {
                    endX = coordinateLEDCanLight[ledsCanLight[j]][0];
                    endY = coordinateLEDCanLight[ledsCanLight[j]][1];
                    break;
                }
            }
            if (endX != 0) { // that means  it's equals
                // Connect source to lit LED with  line
                double startX = source.getX()+ SOURCE_WIDTH/2;
                double startY = source.getY()+ SOURCE_HEIGHT/2; //to get the center

                Line line = new Line(startX, startY, startX, startY);
                root.getChildren().add(line);
                // Animate the line
                animateLine(line, startX, startY, endX, endY);
            }


        }
        ScrollPane scrollPane = new ScrollPane(root);
        return new Scene(scrollPane, 600, 400);
    }

    // Create circle for LEDs
    private Circle createLED(double x, double y) {
        Circle led = new Circle(x, y, LED_RADIUS, Color.MEDIUMAQUAMARINE);
        // Add event handling to allow dragging
        led.setOnMouseDragged(event -> {
            led.setCenterX(event.getX());
            led.setCenterY(event.getY());
        });

        return led;
    }
    // Create rectangle for LEDs
    private Rectangle createSource(double x, double y) {
        Rectangle source = new Rectangle(x, y, SOURCE_WIDTH, SOURCE_HEIGHT);
        source.setFill(Color.DARKGREY);
        return source;
    }
    // To add label to the shapes
    private Label createLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setLayoutX(x-8); //  for label width
        label.setLayoutY(y-8); //  for label height
        return label;
    }
    //  Animate the line
    private void animateLine(Line line, double startX, double startY, double endX, double endY) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(line.endXProperty(), startX), new KeyValue(line.endYProperty(), startY)),
                new KeyFrame(Duration.seconds(1), new KeyValue(line.endXProperty(), endX), new KeyValue(line.endYProperty(), endY))
        );
        timeline.play();
    }


}
