import controllers.ControllerLogin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.ServiceConcert;
import services.ServiceUser;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/loginView.fxml"));
        AnchorPane root = loader.load();

        ControllerLogin ctrl = loader.getController();
        ctrl.setServices(getServiceUser(), getServiceConcert());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("");
        primaryStage.show();
    }

    private static ServiceUser getServiceUser(){
        ApplicationContext context = new ClassPathXmlApplicationContext("FestivalApp.xml");
        return context.getBean(ServiceUser.class);
    }

    private static ServiceConcert getServiceConcert(){
        ApplicationContext context = new ClassPathXmlApplicationContext("FestivalApp.xml");
        return context.getBean(ServiceConcert.class);
    }
}
