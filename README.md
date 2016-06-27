# JsoupParserHelper
* Helper which shows result of Jsoup : Java HTML Parser 
* Program based on Java

# 실행
* Open Project with IntelliJ 
* Run "ApplicationLauncher.java"

# 제작
* 선린인터넷고등학교 웹운영과 20409 오준석

# 프로그램 구조
* 스피너로 HTML/URL 중 어느 형태로 파싱할지 결정합니다. 기본값은 URL입니다.
* CSS 쿼리와 Position을 입력합니다. Position은 선택 입력 사항이며, 입력하지 않을 시 전부 반환됩니다.
* URL 혹은 HTML 코드를 입력합니다. URL은 URL의 형태를 갖추지 않을 시 에러메세지를 띄웁니다.
* Execute를 누르면 오른쪽 JList에 결과값을 출력합니다.
 
# 코드  구조
 * ApplicationLauncher.java
 	* FrameManager를 호출시킵니다.
 * FrameManager.java
 	* JFrame을 상속
 	* 기본적인 레이아웃 컨트롤을 담당합니다.
 	* 메인 레이아웃은 BorderLayout으로 배치하였으며 왼쪽과 오른쪽으로 나누어 각각 GridLayout으로 배치하였습니다.
 	* 위젯 구성에는 JSpinner, JTextField, JList, JButton 클래스를 사용하였습니다.
 	* 기본 레이아웃 배치 -> setSpinner() -> setInput() -> setList() -> JFrame 표시순으로 이루어집니다.
 	* 텍스트 미입력시 기본으로 나타나는 hint를 표시해 주기 위해 JTextField를 상속한 HintEditText 클래스를 구현하였습니다.
 	
 	```java
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
 	```
	* 포커스가 Gain되었을때 힌트를 표시해줍니다.
* Helper.java
	* HelperClass.java를 구현하기 위한 abstract 클래스
	
	```java
	    abstract void log(String logMessage);
    	abstract void alertError(String alertMessage);
	```
	* 로그를 띄우거나 오류가 발생했을 시 알림창을 띄우기 위한 용도입니다.
* HelperClass.java
	* Helper.java를 구현한 클래스
	
	```java
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
	```
* NetworkInterface.java
	* NetworkParser.java를 구현하기 위한 interface
	
	```java
	public interface NetworkInterface {
	    void initialize(boolean isUrl, String UrlOrHtml);
	
	    void reset();
	
	    ArrayList<String> selectFromElement(String cssQuery);
	
	    String selectFromElement(String cssQuery, int position);
}
	```
* NetworkParser.java
	* NetworkInterface.java를 구현한 클래스
	* URL / HTML 두개의 타입으로 입력을 받아 HTML을 파싱하여 리턴하는 구조입니다.

	```java
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
			// Reset
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
	```
	