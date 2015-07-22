package controller.settings;

import config.IniConfig;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import main.Main;

import java.util.Arrays;
import java.util.List;

public class ChatController {

    @FXML
    public ColorPicker backgroundColour, fontColour;

    @FXML
    public ComboBox fontFamily;

    @FXML
    public TextField fontSize;

    @FXML
    public WebView webView;

    @FXML
    public CheckBox timestamp;

    public String background_colour, font_size, font_colour, font_family;

    public void init(){
        fillChoiceBox();

        backgroundColour.setValue(Color.valueOf(IniConfig.getProperty("chat", "background-color")));
        fontFamily.getSelectionModel().select(fontFamily.getItems().indexOf(IniConfig.getProperty("chat", "font-family")));
        fontColour.setValue(Color.valueOf(IniConfig.getProperty("chat", "font-color")));
        fontSize.setText(IniConfig.getProperty("chat", "font-size"));
        timestamp.setSelected(Boolean.parseBoolean(IniConfig.getProperty("chat", "timestamp-enabled")));

        refreshWebView(null);
    }

    public void refreshWebView(ActionEvent event){
        background_colour = getColour(backgroundColour.getValue().getRed(), backgroundColour.getValue().getGreen(), backgroundColour.getValue().getBlue());
        if (fontFamily.getSelectionModel().getSelectedIndex() == -1) fontFamily.getSelectionModel().select(0);
        font_family = fontFamily.getItems().get(fontFamily.getSelectionModel().getSelectedIndex()).toString();
        font_colour = getColour(fontColour.getValue().getRed(), fontColour.getValue().getGreen(), fontColour.getValue().getBlue());
        font_size = fontSize.getText();
        webView.getEngine().loadContent("<html><head><style>body{background-color:" + background_colour + ";font-family:" + font_family + ";color:" + font_colour + ";font-size:" + font_size + ";></style></head><body><p>This is a test message!</p></body></html>");
    }

    public void saveSettings(){
        IniConfig.setProperty("chat", "background-color", background_colour);
        IniConfig.setProperty("chat", "font-family", font_family);
        IniConfig.setProperty("chat", "font-color", font_colour);
        IniConfig.setProperty("chat", "font-size", font_size);
        IniConfig.setProperty("chat", "timestamp-enabled", Boolean.toString(timestamp.isSelected()));
    }



    private void fillChoiceBox(){
        try {
            List<String> fonts = Arrays.asList(
                    "Arial,'Helvetica Neue',Helvetica,sans-serif",
                    "'Arial Black','Arial Bold',Gadget,sans-serif",
                    "'Arial Narrow',Arial,sans-serif",
                    "'Arial Rounded MT Bold','Helvetica Rounded',Arial,sans-serif",
                    "'Avant Garde',Avantgarde,'Century Gothic',CenturyGothic,AppleGothic,sans-serif",
                    "Calibri,Candara,Segoe,'Segoe UI',Optima,Arial,sans-serif",
                    "Candara,Calibri,Segoe,'Segoe UI',Optima,Arial,sans-serif",
                    "'Century Gothic',CenturyGothic,AppleGothic,sans-serif",
                    "'Franklin Gothic Medium','Franklin Gothic','ITC Franklin Gothic',Arial,sans-serif",
                    "Futura,'Trebuchet MS',Arial,sans-serif",
                    "Geneva,Tahoma,Verdana,sans-serif",
                    "'Gill Sans','Gill Sans MT',Calibri,sans-serif",
                    "'Helvetica Neue',Helvetica,Arial,sans-serif",
                    "Impact,Haettenschweiler,'Franklin Gothic Bold',Charcoal,'Helvetica Inserat','Bitstream Vera Sans Bold','Arial Black','sans serif'",
                    "'Lucida Grande','Lucida Sans Unicode','Lucida Sans',Geneva,Verdana,sans-serif",
                    "Optima,Segoe,'Segoe UI',Candara,Calibri,Arial,sans-serif",
                    "'Segoe UI',Frutiger,'Frutiger Linotype','Dejavu Sans','Helvetica Neue',Arial,sans-serif",
                    "Tahoma,Verdana,Segoe,sans-serif",
                    "'Trebuchet MS','Lucida Grande','Lucida Sans Unicode','Lucida Sans',Tahoma,sans-serif",
                    "Verdana,Geneva,sans-serif",
                    "'Big Caslon','Book Antiqua','Palatino Linotype',Georgia,serif",
                    "'Bodoni MT',Didot,'Didot LT STD','Hoefler Text',Garamond,'Times New Roman',serif",
                    "'Book Antiqua',Palatino,'Palatino Linotype','Palatino LT STD',Georgia,serif",
                    "'Calisto MT','Bookman Old Style',Bookman,'Goudy Old Style',Garamond,'Hoefler Text','Bitstream Charter',Georgia,serif",
                    "Cambria,Georgia,serif",
                    "Didot,'Didot LT STD','Hoefler Text',Garamond,'Times New Roman',serif",
                    "Garamond,Baskerville,'Baskerville Old Face','Hoefler Text','Times New Roman',serif",
                    "Georgia,Times,'Times New Roman',serif",
                    "'Goudy Old Style',Garamond,'Big Caslon','Times New Roman',serif",
                    "'Hoefler Text','Baskerville Old Face',Garamond,'Times New Roman',serif",
                    "'Lucida Bright',Georgia,serif",
                    "Palatino,'Palatino Linotype','Palatino LT STD','Book Antiqua',Georgia,serif",
                    "Perpetua,Baskerville,'Big Caslon','Palatino Linotype',Palatino,'URW Palladio L','Nimbus Roman No9 L',serif",
                    "Rockwell,'Courier Bold',Courier,Georgia,Times,'Times New Roman',serif",
                    "'Rockwell Extra Bold','Rockwell Bold',monospace",
                    "Baskerville,'Baskerville Old Face','Hoefler Text',Garamond,'Times New Roman',serif",
                    "TimesNewRoman,'Times New Roman',Times,Baskerville,Georgia,serif",
                    "Consolas,monaco,monospace",
                    "'Courier New',Courier,'Lucida Sans Typewriter','Lucida Typewriter',monospace",
                    "'Lucida Console','Lucida Sans Typewriter',monaco,'Bitstream Vera Sans Mono',monospace",
                    "'Lucida Sans Typewriter','Lucida Console',monaco,'Bitstream Vera Sans Mono',monospace",
                    "monaco,Consolas,'Lucida Console',monospace",
                    "'Andale Mono',AndaleMono,monospace",
                    "Copperplate,'Copperplate Gothic Light',fantasy",
                    "Papyrus,fantasy",
                    "'Brush Script MT',cursive"
            );
            for (String s : fonts) {
                fontFamily.getItems().add(s);
            }

        }catch (Exception e){
            Main.consoleController.eout(e);
            e.printStackTrace();
        }
    }

    private String getColour(double red, double green, double blue){
        String redString = Integer.toHexString((int)(red*255));
        if (redString.equals("0")) redString = "00";
        String greenString = Integer.toHexString((int)(green*255));
        if (greenString.equals("0")) greenString = "00";
        String blueString = Integer.toHexString((int)(blue*255));
        if (blueString.equals("0")) blueString = "00";
        String hexColour = "#"+redString+greenString+blueString;
        return hexColour;
    }

}
