package controller;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;
import main.Parser;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import scripts.Script;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptEditor {

    @FXML
    public AnchorPane anchorPane;

    private CodeArea codeArea;

    public void init(){
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                codeArea.replaceSelection("    ");
            }
        });
        File file = new File(Main.rootPath + "/resources/scripts/DEFAULT_SCRIPT/");
        if (!file.exists())file.mkdir();
        if (!new File(file + "/main.js").exists()) {
            codeArea.replaceText(0,0,sampleCode);
            saveScript();
        }else {
            codeArea.replaceText(0,0,Parser.readFile(new File(file + "/main.js")));
        }
        anchorPane.setTopAnchor(codeArea, 0.0);
        anchorPane.setBottomAnchor(codeArea, 0.0);
        anchorPane.setLeftAnchor(codeArea, 0.0);
        anchorPane.setRightAnchor(codeArea, 0.0);
        anchorPane.getChildren().add(codeArea);
    }

    public void saveScript(){
        File file = new File(Main.rootPath + "/resources/scripts/DEFAULT_SCRIPT/");
        if (!file.exists())file.mkdir();
        Parser.writeFile(new File(file + "/main.js"), codeArea.getText());
        if (Main.scriptManager.getScript(new File(Main.rootPath + "/resources/scripts/DEFAULT_SCRIPT/main.js")) != null) Main.scriptManager.getScript(new File(Main.rootPath + "/resources/scripts/DEFAULT_SCRIPT/main.js")).reloadScript();
        else new Script(new File(Main.rootPath + "/resources/scripts/DEFAULT_SCRIPT/main.js"));
    }

    private static final String[] KEYWORDS = new String[] {
            "$", "$api", "$script", "abstract", "arguments", "Array", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue","debugger", "Date", "default", "do", "delete", "double", "else",
            "enum", "eval", "export", "extends", "false", "final", "finally", "float",
            "for", "function", "goto", "if", "implements", "import",
            "in", "Infinity", "instanceof", "int", "interface", "isFinite", "isNan", "isPrototypeOf", "length", "let", "long", "Math", "name", "NaN", "native",
            "new", "null", "Number", "Object", "package", "private", "protected", "prototype", "public",
            "return", "short", "static", "super", "String", "toString", "undefined", "valueOf",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "true", "try", "typeof", "var", "void", "volatile", "while", "with", "yield"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private static final String sampleCode = String.join("\n", new String[] {
            "function onInfoMsg(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onStreamUpdate(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onHostUpdate(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onMotdUpdate(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onText(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onWhisper(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onSpecialChat(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onMediaLog(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onJoin(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onPart(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onUserList(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onUserInfo(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onNewSubscriber(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onGiveawayStared(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onGiveawayEnded(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onPollStared(message){",
            "   //THIS IS A TEST",
            "}",
            "",
            "function onPollEnded(message){",
            "   //THIS IS A TEST",
            "}"
    });

}
