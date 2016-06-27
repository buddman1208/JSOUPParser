import javax.swing.*;

/**
 * Created by Chad on 6/27/16.
 */
public class HelperClass extends Helper{
    HelperClass(){}
    @Override
    void log(String logMessage) {
        System.out.println(logMessage);
    }

    @Override
    void alertError(String alertMessage) {
        JOptionPane.showMessageDialog(new JFrame(), alertMessage);
    }
}
