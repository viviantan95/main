package seedu.address.ui;

import java.util.HashMap;
import java.util.Random;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import seedu.address.model.person.ReadOnlyPerson;


/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final double GAP = 8;

    private static String[] colors = { "red", "blue", "orange", "brown", "green", "black", "grey", "yellow", "pink" };

    private static HashMap<String, String> tagColors = new HashMap<String, String>();
    private static Random random = new Random();
    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyPerson person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private FlowPane phones;
    @FXML
    private Label birthday;
    @FXML
    private Label address;
    @FXML
    private FlowPane emails;
    @FXML
    private ImageView imageView;
    @FXML
    private FlowPane tags;

    public PersonCard(ReadOnlyPerson person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        initPhones(person);
        initEmails(person);
        initTags(person);
        initPhoto(person);
        bindListeners(person);
    }

    private static int getRandom() {
        int randNum = random.nextInt(colors.length);
        return randNum;
    }

    private static String getColorForTag(String tagValue, int randNum) {
        if (!tagColors.containsKey(tagValue)) {
            tagColors.put(tagValue, colors[randNum]);

        }

        return tagColors.get(tagValue);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyPerson person) {
        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        person.phoneProperty().addListener((observable, oldValue, newValue) -> {
            phones.getChildren().clear();
            initPhones(person);
        });
        birthday.textProperty().bind(Bindings.convert(person.birthdayProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        person.emailProperty().addListener((observable, oldValue, newValue) -> {
            emails.getChildren().clear();
            initEmails(person);
        });
        person.photoProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setImage(new Image(person.getPhoto().toString(), 120, 120,
                    true, false));
        });
        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            tags.getChildren().clear();
            initTags(person);
        });
    }

    /**
     * Initialise the phones for each person
     */
    private void initPhones(ReadOnlyPerson person) {
        person.getPhones().forEach(phone -> {
            Label phoneLabel = new Label(phone.value);
            phones.getChildren().add(phoneLabel);
            phones.setHgap(GAP);
        });
    }

    /**
     * Initialise the emails for each person
     */
    private void initEmails(ReadOnlyPerson person) {
        person.getEmails().forEach(email -> {
            Label emailLabel = new Label(email.value);
            emails.getChildren().add(emailLabel);
            emails.setHgap(GAP);
        });
    }

    /**
     * Initialise the tags for each person
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            int randNum = getRandom();
            tagLabel.setStyle("-fx-background-color: " + getColorForTag(tag.tagName, randNum));
            if (randNum > 6) {
                tagLabel.setStyle("-fx-text-fill: black");
            }

            tags.getChildren().add(tagLabel);
        });
    }

    /**
     *
     * Initialise the photo for each person
     */
    private void initPhoto(ReadOnlyPerson person) {
        imageView.setImage(new Image(person.getPhoto().toString(), 126, 126, true,
                false));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }


}
