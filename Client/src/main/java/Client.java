import controllers.ControllerLogin;
import controllers.ControllerMainApp;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import persistance.PersistanceException;
import service.IService;


public class Client extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        FXMLLoader loaderPagPrinc = new FXMLLoader();

        loaderPagPrinc.setLocation(getClass().getResource("/view/mainView.fxml"));
        AnchorPane rootPagPrinc = loaderPagPrinc.load();
        ControllerMainApp controllerPagPrinc = loaderPagPrinc.getController();
        Scene scenePagPrinc = new Scene(rootPagPrinc);
        Stage stagePagPrinc = new Stage();
        stagePagPrinc.setTitle("Music Festivals");
        stagePagPrinc.setScene(scenePagPrinc);

        IService server = (IService) factory.getBean("Service");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Parent root = loader.load();
        ControllerLogin ctrl = loader.getController();
        ctrl.setAppService(primaryStage, stagePagPrinc, server, controllerPagPrinc);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    server.logout("username");
                } catch (PersistanceException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        primaryStage.setTitle("Login");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
