package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class OnlyEditor extends Application 
{
	protected BDEditorView editorView;
	
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	private String curPath = this.getClass().getResource("/").getPath();
	private String editorUrl = "file://" + curPath + "resources/ace-builds-master/editor.html";
	
	@Override
	public void start(Stage stage) 
	{
		BorderPane root  = new BorderPane();

		editorView = new BDEditorView(stage, editorUrl);
		
		new BDEditorCtrl(editorView);

		root.setCenter(editorView);
	    
	    Scene scene = new Scene(root);
		
		stage.setTitle("ACE4JavaFX");
		stage.setScene(scene);
		stage.show();
	}


}
