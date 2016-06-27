import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by KOHA_CLOUD on 2016. 6. 26..
 */
class NetworkParser implements NetworkInterface {

    HelperClass helper;
    private Document doc = null;

    NetworkParser(){
        helper = new HelperClass();
    }
    @Override
    public void initialize(boolean isUrl, String UrlOrHtml) {
        if (isUrl) {
            ((Runnable) () -> {
                try {
                    doc = Jsoup.connect(UrlOrHtml).get();
                } catch (IllegalArgumentException e) {
                    helper.alertError("Malformed Url!\nPlease Check if URL is correct.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).run();
        } else {
            ((Runnable) () -> {
                doc = Jsoup.parse(UrlOrHtml);
            }).run();
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public ArrayList<String> selectFromElement(String cssQuery) {
        ArrayList<String> elementList = new ArrayList<>();
        Elements elements = doc.select(cssQuery);
        for (Element e : elements) {
            elementList.add(e.text());
        }
        return elementList;
    }

    @Override
    public String selectFromElement(String cssQuery, int position) {
        ArrayList<String> elementList = selectFromElement(cssQuery);
        if(position > elementList.size()) {
            helper.alertError("Out of Array!");
            return "";
        }
        else return selectFromElement(cssQuery).get(position);
    }
}
