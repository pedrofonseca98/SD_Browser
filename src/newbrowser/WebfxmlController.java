package newbrowser;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JOptionPane;

public class WebfxmlController implements Initializable {

    @FXML
    private TextField txt;
    @FXML
    private Button bt;
    @FXML
    private AnchorPane anc;
    @FXML
    private WebView webView;
    @FXML
    private Button plus;
    @FXML
    private Button retroceder;
    @FXML
    private TabPane t;
    @FXML
    private WebEngine we;
    HashMap<String, runnable> threadList = new HashMap<String, runnable>();
    static ArrayList<String> historico = new ArrayList<String>();

    public void Historico() {
        String aux = "";
        for (int i = 0; i < historico.size(); i++) {
            if (historico.get(i) != " ") {
                aux += historico.get(i) + "\n";
            }
        }

        JOptionPane.showMessageDialog(null, aux, "Historico", JOptionPane.INFORMATION_MESSAGE);
    }

    @FXML
    public void start() {
        String ur;
        WebView wv = new WebView();
        we = wv.getEngine();
        Tab tab = new Tab("Nova aba");
        t.getTabs().add(tab);
        int numTabs = t.getTabs().size();
        t.getSelectionModel().select(tab);
        webView.setContextMenuEnabled(false);
        threadList.put(t.getSelectionModel().getSelectedItem().toString(), new runnable(numTabs, 10));

        tab.setContent(wv);

        try {
            ur = txt.getText();
            Tab name = t.getSelectionModel().getSelectedItem();
            WebView w = (WebView) name.getContent();
            we = wv.getEngine();
            w.setContextMenuEnabled(false);
            historico.add(ur);
            String html = threadList.get(name.toString()).socket(ur);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @FXML
    public void close() {
        Tab tab = t.getSelectionModel().getSelectedItem();
        tab.getTabPane().getTabs().remove(tab);
        threadList.remove(t.getSelectionModel().getSelectedItem());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        we = webView.getEngine();
        we.setJavaScriptEnabled(true);

        EventHandler<ActionEvent> enter = new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                we.load(txt.getText().startsWith("http://") ? txt.getText() : "http://" + txt.getText());

            }
        };
        txt.setOnAction(enter);
        bt.setOnAction(enter);

        we.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txt.setText(newValue);
            }
        });
    }

    @FXML
    private void handle(ActionEvent event) {
    }
}
