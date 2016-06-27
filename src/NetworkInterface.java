import java.util.ArrayList;

/**
 * Created by Chad on 6/27/16.
 */
public interface NetworkInterface {
    void initialize(boolean isUrl, String UrlOrHtml);

    void reset();

    ArrayList<String> selectFromElement(String cssQuery);

    String selectFromElement(String cssQuery, int position);

}
