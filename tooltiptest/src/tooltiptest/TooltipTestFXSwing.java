package tooltiptest;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;

/**
 * This application test the following JDK bug:
 * 
 * We open a frame with an JFXPanel that contains a WebView. The WebView shows a page with a button 
 * with a tooltip. Do the following steps:
 * - start the application with the VM-argument "-DUSE_CLEANUP_WORKAROUND=false"
 * - click on "Open new browser window"
 * - hover over the upcoming button "Show my tooltip" until the tooltip is shown
 * - close the web view window
 * 
 * If you use a profiler you'll see that the WebView is not garbage collected since it is still referenced by the 
 * tooltip component which is referenced by an com.sun.webkit.WebPage instance. 
 * 
 * If you start with "-DUSE_CLEANUP_WORKAROUND=true" the WebView, WebPage etc. is garbage collected.
 * The trick is to set an dummy scene at the JFXPanel and to call removeAll(9 for the frame.
 * 
 * @author Gerd Müller-Schramm, 2017
 * @version 1.0
 * @since 16.5.0.0
 */
public class TooltipTestFXSwing
{
	
	public static boolean USE_CLEANUP_WORKAROUND = Boolean.getBoolean("USE_CLEANUP_WORKAROUND"); 
	
	
	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	public void start()
	{
		JFrame frame = new JFrame("TooltipTestSwing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setLayout(new BorderLayout());
		
		JButton btn = new JButton("Open new browser window");
		btn.addActionListener(e -> reloadPage());
		panel.add(btn, BorderLayout.NORTH);
		
		frame.pack();
		frame.setVisible(true);
		
		PlatformImpl.startup(() -> System.out.println("init"));
	}
	
	public void reloadPage()
	{
		JFrame frame = new JFrame("TooltipTestSwing");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setLayout(new BorderLayout());

		JFXPanel jfxPanel = new JFXPanel();
		panel.add(jfxPanel, BorderLayout.CENTER);

		frame.setSize(800, 600);
		frame.setVisible(true);

		Platform.setImplicitExit(false);
		Platform.runLater(() ->
		{
			WebView webView = new WebView();
			try
			{
				webView.getEngine().load(TooltipTestFXSwing.class.getResource("tooltip.html").toURI().toString());
			}
			catch (URISyntaxException e1)
			{
				e1.printStackTrace();
			}

			Scene scene = new Scene(webView, 800, 600);
			jfxPanel.setScene(scene);
		});
		
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				if (USE_CLEANUP_WORKAROUND) 
				{
					Platform.runLater(() -> jfxPanel.setScene(new Scene(new Region())));
					frame.removeAll();
				}
				frame.dispose();
			}
		});
	}
	
	public static void main(String[] args)
	{
		new TooltipTestFXSwing().start();
	}
}
