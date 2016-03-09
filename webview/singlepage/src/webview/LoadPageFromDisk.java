package webview;

import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class LoadPageFromDisk extends Application
{
	
	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception
	{
		WebView webView = new WebView();
		webView.getEngine().load(Paths.get("dependency/topack/webview/html/index.html").toUri().toString());
		Scene scene = new Scene(webView, 800, 600);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
