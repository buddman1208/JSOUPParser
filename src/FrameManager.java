import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.html.CSS;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by KOHA_CLOUD on 2016. 6. 26..
 */
class FrameManager extends JFrame {
    final String cssDefaultQuery = "Input CSS Query";
    final String positionDefaultQuery = "Input Position";
    final String urlOrHtmlDefaultQuery = "Input Url or Html";

    int selectedPosition = -1;
    private JPanel panel = new JPanel();
    private JPanel panel2 = new JPanel();
    private JPanel panel3 = new JPanel();
    private JPanel panel4 = new JPanel();
    private JButton execute = new JButton("Execute");
    private JList list = new JList();
    private JScrollPane listScroller;
    private String parseType[] = new String[]{"URL", "HTML"};
    private JSpinner typeSelecter = new JSpinner();
    HelperClass helper = new HelperClass();
    NetworkParser parser = new NetworkParser();

    FrameManager(String frameName) {
        super(frameName);
        setDefault();
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(6, 1));
        rightPanel.setLayout(new GridLayout(1, 1));
        setSpinner();
        setInput();
        setList();
        leftPanel.add(panel);
        leftPanel.add(panel2);
        leftPanel.add(panel3);
        leftPanel.add(panel4);
        rightPanel.add(listScroller);
        rightPanel.setPreferredSize(new Dimension(230, 280));
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setList() {
        DefaultListModel model = new DefaultListModel();
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        listScroller = new JScrollPane(list);
        list.setModel(model);
    }

    private void setList(boolean isUrl, String cssQuery, int selectedPos, String urlorhtml) {
        ArrayList<String> result;
        DefaultListModel model = new DefaultListModel();
        if (isUrl) parser.initialize(true, urlorhtml);
        else parser.initialize(false, urlorhtml);

        if(selectedPos == -1){
            result = parser.selectFromElement(cssQuery);
            for(String s : result) {
                model.addElement(s);
            }
            list.setModel(model);
        } else {
            String s = parser.selectFromElement(cssQuery, selectedPos);
            model.addElement(s);
            list.setModel(model);
        }
    }

    private void setSpinner() {
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        SpinnerListModel spinAdapter = new SpinnerListModel(parseType);
        typeSelecter.setModel(spinAdapter);
        panel.add(typeSelecter);
        Dimension selectorSize = new Dimension(120, 30);
        typeSelecter.setPreferredSize(selectorSize);
        typeSelecter.setMinimumSize(selectorSize);
    }

    private void setInput() {
        JTextField CSSField = new HintEditText(cssDefaultQuery);
        JTextField selectNum = new HintEditText(positionDefaultQuery);
        JTextField URLHtml = new HintEditText(urlOrHtmlDefaultQuery);
        CSSField.setPreferredSize(new Dimension(120, 50));
        selectNum.setPreferredSize(new Dimension(120, 50));
        URLHtml.setPreferredSize(new Dimension(240, 50));
        execute.setPreferredSize(new Dimension(240, 30));
        execute.addActionListener(e -> {
            String cssQuery = CSSField.getText().trim();
            String urlorhtml = URLHtml.getText().trim();
            if (selectNum.getText().trim().equals(positionDefaultQuery)) selectedPosition = -1;
            else {
                try {
                    selectedPosition = Integer.parseInt(selectNum.getText());
                } catch (NumberFormatException err) {
                    helper.alertError(err.getMessage());
                    return;
                }
            }
            if (cssQuery.equals(cssDefaultQuery) || urlorhtml.equals(urlOrHtmlDefaultQuery)) {
                helper.alertError("Please Input CSS Query and URL or HTML");
            } else setList((typeSelecter.getValue().equals("URL")), cssQuery, selectedPosition, urlorhtml);
        });
        panel2.add(CSSField);
        panel2.add(selectNum);
        panel3.add(URLHtml);
        panel4.add(execute);

    }


    private void setDefault() {
        setSize(500, 300);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}

class HintEditText extends JTextField implements FocusListener {

    private final String hint;
    boolean showingHint;

    HintEditText(final String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().equals(hint)) {
            super.setText("");
            showingHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText(hint);
            showingHint = true;
        }
    }
}


