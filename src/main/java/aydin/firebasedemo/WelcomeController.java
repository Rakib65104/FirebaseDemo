package aydin.firebasedemo;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

public class WelcomeController {

    @FXML
    private Button registerButton;

    @FXML
    private Button signInButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    // **REGISTER USER**
    @FXML
    void registerButtonClicked(ActionEvent event) {
        String email = emailTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Registration Failed", "Please enter both email and password.");
            return;
        }

        try {
            // Register a new user with Firebase Authentication
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            showAlert("Success", "User Registered", "User ID: " + userRecord.getUid());

        } catch (FirebaseAuthException e) {
            showAlert("Error", "Registration Failed", e.getMessage());
        }
    }

    // **SIGN IN USER**
    @FXML
    void signInButtonClicked(ActionEvent event) {
        String email = emailTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Sign-In Failed", "Please enter both email and password.");
            return;
        }

        try {
            // Retrieve user details from Firebase Authentication
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);

            if (userRecord != null) {
                showAlert("Success", "Sign-In Successful", "Welcome, " + userRecord.getEmail());

                // Switch scene to primary view
                DemoApp.setRoot("primary");
            } else {
                showAlert("Error", "Sign-In Failed", "User not found.");
            }
        } catch (FirebaseAuthException e) {
            showAlert("Error", "Sign-In Failed", "Invalid email or password.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
