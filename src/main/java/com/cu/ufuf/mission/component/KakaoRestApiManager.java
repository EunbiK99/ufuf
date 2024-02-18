package com.cu.ufuf.mission.component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class KakaoRestApiManager {

    private String secretKey = "fe6556cbcccecbec99f52226077803d7";
    private ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> request(String strUrl, String method, Map<String, Object> body){

            String requestJson = mapToJson(body);
            // System.out.println("body : " + body);
            // System.out.println("request : "+ requestJson);
            String responseJson = request(strUrl, method, requestJson);

            return stringToMap(responseJson);    

    }


    private String mapToJson(Map<String, Object> map){
        String requestJson = null;
        try{
            requestJson = objectMapper.writeValueAsString(map);

        } catch(Exception e){
            e.printStackTrace();
        }
        return requestJson;
    }


    private Map<String, Object> stringToMap(String text){

        Map<String, Object> result = null;

        try{
            TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String,Object>>() {};
            result = objectMapper.readValue(text, typeReference);
        }catch(Exception e){
            e.printStackTrace();
        }
            
        return result;            


    }

    private String request(String strUrl, String method, String body){

        HttpURLConnection conn = createConnection(strUrl, method);
        send(conn, body);
        String json = receive(conn);

        return json;
    }

    private void initHeader(String method, HttpURLConnection conn){
        try {
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "SECRET_KEY " + secretKey);
            conn.setDoOutput(true); 
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void send(HttpURLConnection conn, String data){
        try {
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);         
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private HttpURLConnection createConnection(String strUrl, String method){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection)url.openConnection();
            initHeader(method, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    private String receive(HttpURLConnection conn){
            // 서버로부터 데이터 읽어오기
            String result = null;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                // System.out.println("br:"+br);
                StringBuilder sb = new StringBuilder();
                String line = null;
                
                while((line = br.readLine()) != null) { // 읽을 수 있을 때 까지 반복
                    // System.out.println("line : " + line);
                    sb.append(line);
                    // System.out.println("sb:"+sb);
                }
                result = sb.toString();
                // System.out.println("result :" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return result;
    }

}
