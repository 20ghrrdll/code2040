import java.util.*;
import java.net.*;
import java.io.*;
import java.text.*;

public class code2040 {
    
    public static void main(String[] args) {
        //send in initial info
        Map<String, Object> requests = new HashMap<String, Object>();
        requests.put("email", "caraha@stanford.edu");
        requests.put("github", "https://github.com/20ghrrdll/code2040");
        //get response and extract id
        JSONObject result = getToken("http://challenge.code2040.org/api/register",requests);
        Map<String, Object> receivedInfo = extractValue(result);
        //complete challenges
        String Identifier = (String)receivedInfo.get("result");
        flipString(Identifier);
        findNeedle(Identifier);
        startsWith(Identifier);
        secondDateStamp(Identifier);//computes correct date, does not send to server for validation
    }
    
    private static void flipString(String id){
        Map<String, Object> requests = new HashMap<String, Object>();
        requests.put("token", id);
        JSONObject result = getToken("http://challenge.code2040.org/api/getstring", requests);
        Map<String, Object> receivedInfo = extractValue(result);
        String toReverse = (String)receivedInfo.get("result");
        String toReturn = "";
        char[] flipFlop = toReverse.toCharArray();
        for(int a = 0; a < toReverse.length(); a++){
            toReturn+=flipFlop[toReverse.length()-1-a];
        }
        requests.put("string", toReturn);
        getToken("http://challenge.code2040.org/api/validatestring", requests);
    }
    
    private static void findNeedle(String id){
        Map<String, Object> requests = new HashMap<String, Object>();
        requests.put("token", id);
        JSONObject result = getToken("http://challenge.code2040.org/api/haystack", requests);
        Map<String, Object> receivedInfo = extractValue(result);
        Map<String,Object> extracted = (Map<String,Object>)receivedInfo.get("result");
        String needle = (String)extracted.get("needle");
        ArrayList<String> haystack = (ArrayList<String>)extracted.get("haystack");
        int location = 0;
        for(String straw: haystack){
            if(!straw.equals(needle)){
                location++;
                break;
            }
        }
        requests.put("needle", location);
        getToken("http://challenge.code2040.org/api/validateneedle", requests);
    }
    
    private static void startsWith(String id){
        Map<String, Object> requests = new HashMap<String, Object>();
        requests.put("token", id);
        JSONObject result = getToken("http://challenge.code2040.org/api/prefix", requests);
        Map<String, Object>receivedInfo = extractValue(result);
        Map<String, Object>extracted = (Map<String, Object>)receivedInfo.get("result");
        String prefix = (String)extracted.get("prefix");
        ArrayList<String> array = (ArrayList<String>)extracted.get("array");
        int originalSize = array.size();
        for(int a = 0; a < originalSize; a++){
            String temp = array.get(a);
            if(temp.startsWith(prefix)){
                array.remove(temp);
                originalSize--;
            }
        }
        String[] toReturn = new String[array.size()];
        for(int a = 0; a < array.size(); a ++){
            toReturn[a] = array.get(a);
        }
        requests.put("array", toReturn);
        getToken("http://challenge.code2040.org/api/validateprefix", requests);
    }
    
    private static void secondDateStamp(String id){
        Map<String, Object> requests = new HashMap<String, Object>();
        requests.put("token", id);
        JSONObject result = getToken("http://challenge.code2040.org/api/time", requests);
        Map<String, Object> receivedInfo = extractValue(result);
        Map<String, Object>extracted = (Map<String, Object>)receivedInfo.get("result");
        String datestamp = (String)extracted.get("datestamp");
        int interval = (int)extracted.get("interval");
        Calendar toAlter = dateParser(datestamp);
        toAlter.set(13, interval+toAlter.get(13));
        DateFormat process = DateFormat.getDateInstance(DateFormat.SHORT);
        String newDate = reassemble(toAlter);
        requests.put("datestamp", newDate);
        getToken("http://challenge.code2040.org/api/validatetime", requests);
    }
    
    private static Calendar dateParser(String date){
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(9,10));
        int hour = Integer.parseInt(date.substring(12,13));
        int min = Integer.parseInt(date.substring(15,16));
        int sec = Integer.parseInt(date.substring(18,19));
        Calendar result = Calendar.getInstance();
        result.set(year, month, day, hour, min, sec);
        result.setLenient(true);
        return result;
    }
    
    private static String reassemble(Calendar date){
        String toReturn =  date.get(1)+"-";
        if(date.get(2) < 10){
            toReturn+="0"+date.get(2)+"-";
        }else{
            toReturn+=date.get(2)+"-";
        }
        if(date.get(5) < 10){
            toReturn+="0"+date.get(5);
        }else{
            toReturn+=date.get(5);
        }
        toReturn +="T";
        if(date.get(10) < 10){
            toReturn+="0"+date.get(10)+":";
        }else{
            toReturn+=date.get(10)+":";
        }
        if(date.get(12) < 10){
            toReturn+="0"+date.get(12)+":";
        }else{
            toReturn+=date.get(12)+":";
        }
        if(date.get(13) < 10){
            toReturn+="0"+date.get(13)+".000Z";
        }else{
            toReturn+=date.get(13)+".000Z";
        }
        return toReturn;
    }
    
    private static JSONObject getToken(String target, Map<String, Object> dict){
        HttpURLConnection connection = null;
        String response = "";
        JSONObject toReturn = null;
        try{
            URL url = new URL(target);
            connection = (HttpURLConnection)url.openConnection();
            try{
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            catch(ProtocolException e){}
            JSONObject requests = new JSONObject(dict);
            String dictionary = requests.toString();
            OutputStream out = connection.getOutputStream();
            try{
                OutputStreamWriter wr = new OutputStreamWriter(out);
                wr.write(dictionary);
                wr.flush();
                wr.close();
            }
            catch(IOException e){
            }
            
            if(out !=null){
                out.close();
            }
            
            int HttpResult = connection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK){
                InputStream input = connection.getInputStream();
                try{
                    BufferedReader rd = new BufferedReader(new InputStreamReader(input));
                    String info = "";
                    while((info = rd.readLine()) != null){
                        response +=info;
                    }
                    rd.close();
                }
                catch(IOException e){}
                if(input!= null){
                    input.close();
                }
                toReturn = new JSONObject(response);
            }
            else{
                System.out.println(connection.getResponseMessage());
                
            }
            if(connection !=null){
                connection.disconnect();
            }
            return toReturn;
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return toReturn;
    }
    
    /**
     * Method extractArray takes the given key and checks to see if the corresponding value is an array
     * If it is not, extract Array returns an empty array. It is assumed that the array is an array of
     * strings.
     *
     * @param json the JSON object assumed to contain the array
     * @param key the assumed array label
     * @return the array in a form that can be worked with
     */
    private static ArrayList<String> extractArray(JSONArray array){
        ArrayList<String> extracted = new ArrayList();
        for(int a = 0; a < array.length(); a++){
            extracted.add((String)array.get(a));
        }
        return extracted;
    }
    
    /**
     * Method extractValue assumes that the value to be extracted is known to be a string.
     *
     * @param json A parameter
     * @return The return value
     */
    private static Map<String, Object> extractValue(JSONObject json){
        Map<String, Object> toReturn = new HashMap<String, Object>();
        Iterator<String> results = json.keys();
        while(results.hasNext()){
            String key = results.next();
            Object value = json.get(key);
            if(value instanceof JSONArray){
                value = extractArray((JSONArray)value);
            }
            else if(value instanceof JSONObject){
                value = extractValue((JSONObject)value);
            }
            toReturn.put(key,value);
        }
        return toReturn;
    }
    
}
