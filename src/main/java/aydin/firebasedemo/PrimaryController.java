package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PrimaryController {
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField phoneTextField;  // New phone number field

    @FXML
    private TextArea outputTextArea;

    @FXML
    private Button readButton;

    @FXML
    private Button writeButton;

    @FXML
    private Button switchSecondaryViewButton;

    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();

    @FXML
    void readButtonClicked(ActionEvent event) {
        readFirebase();
    }

    @FXML
    void writeButtonClicked(ActionEvent event) {
        addData();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        DemoApp.setRoot("secondary");
    }

    private boolean readFirebase() {
        boolean key = false;
        ApiFuture<QuerySnapshot> future = DemoApp.fstore.collection("Persons").get();

        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            outputTextArea.clear();
            for (QueryDocumentSnapshot document : documents) {
                outputTextArea.appendText(document.getData().get("Name") + " , Age: " +
                        document.getData().get("Age") + " , Phone: " + document.getData().get("Phone") + "\n");
                listOfUsers.add(new Person(
                        String.valueOf(document.getData().get("Name")),
                        Integer.parseInt(document.getData().get("Age").toString()),
                        String.valueOf(document.getData().get("Phone"))));
            }
            key = true;
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    private void addData() {
        DocumentReference docRef = DemoApp.fstore.collection("Persons").document(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameTextField.getText());
        data.put("Age", Integer.parseInt(ageTextField.getText()));
        data.put("Phone", phoneTextField.getText());

        ApiFuture<WriteResult> result = docRef.set(data);
    }
}
