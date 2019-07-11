package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class Controller {
    @FXML
    TextArea FirstPoem,SecondPoem;
    public void Translate(ActionEvent event){
        String a=FirstPoem.getText();
        String clientId = "feQnLzO_tkDPT75OTukf";
        String clientSecret = "ueJnIOL1K1";
        try {
            String text = URLEncoder.encode(a, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "source=en&target=ko&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            parseJson(response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private void parseJson(String json) throws Exception {
        Map<String, Object> map = new ObjectMapper().readValue(json, Map.class);
        Map<String, Object> message = (Map<String, Object>) map.get("message");
        Map<String, String> result = (Map<String, String>) message.get("result");
        SecondPoem.setText(result.get("translatedText"));
        System.out.println(result.get("translatedText"));
    }
}
