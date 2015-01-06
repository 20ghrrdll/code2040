import java.util.*;
import java.net.*;
import java.io.*;

public class code2040 {
    
    public static void main(String[] args) {
        Map<String, Object> requests = new HashMap<String, Object>();
        requests.put("email", "caraha@stanford.edu");
        requests.put("github", "https://github.com/20ghrrdll/code2040");
        JSONObject result = getToken("http://challenge.code2040.org/api/register",requests);
        Map<String, Object> receivedInfo = extractValue(result);
        String Identifier = (String)receivedInfo.get("result");
        Map<String, Object> postToken = new HashMap<String, Object>();
        postToken.put("token", Identifier);
        result = getToken("http://challenge.code2040.org/api/getstring", postToken);
        receivedInfo = extractValue(result);
        String toReverse = (String)receivedInfo.get("result");
        requests.clear();
        requests.put("token", Identifier);
        requests.put("string", flipString(toReverse));
        getToken("http://challenge.code2040.org/api/validatestring", requests);
        result = getToken("http://challenge.code2040.org/api/haystack", postToken);
        receivedInfo = extractValue(result);
        Map<String,Object> extracted = (Map<String,Object>)receivedInfo.get("result");
        String needle = (String)extracted.get("needle");
        ArrayList<String> haystack = (ArrayList<String>)extracted.get("haystack");
        requests.remove("string");
        requests.put("needle", findNeedle(needle, haystack));
        getToken("http://challenge.code2040.org/api/validateneedle", requests);
        result = getToken("http://challenge.code2040.org/api/prefix", postToken);
        receivedInfo = extractValue(result);
        extracted = (Map<String, Object>)receivedInfo.get("result");
        String prefix = (String)extracted.get("prefix");
        ArrayList<String> array = (ArrayList<String>)extracted.get("array");
        requests.remove("needle");
        requests.put("array", startsWith(prefix, array));
        getToken("http://challenge.code2040.org/api/validateprefix", requests);
    }
    
    private static String flipString(final String toReverse){
        String toReturn = "";
        char[] flipFlop = toReverse.toCharArray();
        for(int a = 0; a < toReverse.length(); a++){
            toReturn+=flipFlop[toReverse.length()-1-a];
        }
        return toReturn;
    }
    
    private static int findNeedle(final String needle, final ArrayList<String> haystack){
        int location = 0;
        for(String straw: haystack){
            if(straw.equals(needle)){
                return location;
            }
            else{
                location++;
            }
        }
        return -1;
    }
    
    private static String[] startsWith(final String prefix, final ArrayList<String> array){
        String[] results = new String[10];
        int size = 0;
        int capacity = 10;
        for(String potential: array){
            if(!potential.startsWith(prefix)){
                if(size < capacity){
                    results[size] = potential;
                    size++;
                }
                else{
                    String[] temp = new String[capacity*2];
                    for(int a = 0; a < capacity; a++){
                        temp[a] = results[a];
                    }
                    capacity*=2;
                    results = temp;
                }
            }
        }
        String[] toReturn = new String[size];
        for(int a = 0; a < size; a ++){
            toReturn[a] = results[a];
        }
        return toReturn;
    }
    
    //I had never heard of "JSON" before this exercise. This method was by far the most exhaustive learning experience.
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
     * Method extractValue returns the given JSONObject as a usable Map<String, Object> so the
     * information can be read and processed by other methods
     * @param json is the JSON object returned by the server
     * @return The Map<String, Object>
     */
    private static Map<String, Object> extractValue(JSONObject json){
        Map<String, Object> toReturn = new HashMap<String, Object>();
        Iterator<String> results = json.keys();
        while(results.hasNext()){
            String key = results.next();
            Object value = json.get(key);
            if(value instanceof JSONArray){
                value = extractArray((JSONArray)value);
                //System.out.println(value.toString());
            }
            else if(value instanceof JSONObject){
                value = extractValue((JSONObject)value);
            }
            toReturn.put(key,value);
        }
        return toReturn;
    }
    
}
