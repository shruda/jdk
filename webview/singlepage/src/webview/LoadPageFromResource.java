package webview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * @author Steve Hruda, 2016
 * @version 1.0
 */
public class LoadPageFromResource extends Application
{
	
	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception
	{
		WebView webView = new WebView();
		webView.getEngine().load(LoadPageFromDisk.class.getResource("html/index.html").toExternalForm());
		Scene scene = new Scene(webView, 800, 600);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
