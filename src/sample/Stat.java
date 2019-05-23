package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Natalie Le (nl121)
 */
public class Stat {
    private final double SPACING_OFFSET = 15;
    private boolean isRule; // false for cellState
    private Text title;
    private double value;
    private Text description;
    private Button increaseButton;
    private Button decreaseButton;

    public Stat(String title, double value, double xCoord, double yCoord, boolean isRule){
        this.isRule = isRule;
        this.title = new Text(xCoord, yCoord, title);
        this.title.setUnderline(true);
        this.value = value;
        this.description = new Text();
        this.description.setX(xCoord + this.title.getLayoutBounds().getWidth() + 3 * SPACING_OFFSET);
        this.description.setY(yCoord);
        updateDescription();
        increaseButton = new Button("+");
        decreaseButton = new Button("-");
        setButtonLocation(increaseButton, description.getX() + description.getLayoutBounds().getWidth() + SPACING_OFFSET, yCoord - SPACING_OFFSET);
        setButtonLocation(decreaseButton, description.getX() - SPACING_OFFSET * 2, yCoord - SPACING_OFFSET);
        increaseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                increaseValue();
                updateDescription();
            }
        });
        decreaseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                decreaseValue();
                updateDescription();
            }
        });
    }

    private void increaseValue(){
        if (this.value > 1){
            this.value++;
        } else {
            this.value += 0.01;
        }
        updateDescription();
    }

    private void decreaseValue(){
        if (this.value > 1) {
            if (this.value - 1 >= 0) {
                this.value--;
            }
        } else {
            if (this.value - 0.01 >= 0){
                this.value -= 0.01;
            }
        }
        updateDescription();
    }

    private void updateDescription(){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        description.setText(decimalFormat.format(this.value));
    }

    private void setButtonLocation(Button b, double xCoord, double yCoord){
        b.setLayoutX(xCoord);
        b.setLayoutY(yCoord);
    }

    public Text getTitle() { return this.title; }

    public Text getDescription() { return this.description; }

    public Button getIncreaseButton() { return this.increaseButton; }

    public Button getDecreaseButton() { return this.decreaseButton; }

    public double getValue() { return this.value; }

    public boolean getIsRule() { return this.isRule; }

}
