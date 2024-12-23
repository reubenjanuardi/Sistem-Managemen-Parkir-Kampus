import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NavigationUtil {
    public static void goToMainMenu(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource("MainMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goToAdminMenu(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource("AdminMenu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}