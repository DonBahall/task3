import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Task1 {

    Map<String, Double> map = new HashMap<>();

    public static void main(String[] args) {
        Task1 task1 = new Task1();
        task1.startingThreads();
    }

    /**
     * Method finding the folder and get all files in it
     * @return files in folders
     */
    private File[] openFile() {
        File folder = new File("src/main/resources/violation");
        return folder.listFiles();
    }

    /**
     * Method which starting threads
     */
    private void startingThreads() {
        ExecutorService service = Executors.newFixedThreadPool(2);
        if (openFile() != null) {
            for (int i = 0; i < openFile().length; i++) {
                int finalI = i;
                CompletableFuture<Future<Map<String, Double>>> future =
                        CompletableFuture.supplyAsync(() ->
                                service.submit(() -> readFile(new File(openFile()[finalI].getName()))), service);
                try {
                    System.out.println(future.get().get().entrySet());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            service.shutdown();
            createXml();
        }
    }

    /**
     * Method parsing the json file and write data in map
     *
     * @param newFile file to read
     * @return map with data
     */
    private Map<String, Double> readFile(File newFile) {

        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(new FileReader("src/main/resources/violation/" + newFile));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String name = (String) jsonObject.get("type");
                double sum = (double) jsonObject.get("fine_amount");
                if (map.containsKey(name)) {
                    map.put(name, map.get(name) + sum);
                } else {
                    map.put(name, sum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Creating Xml document using global map with data
     */
    private void createXml() {
        sortMap();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Violations");
            doc.appendChild(rootElement);
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                Element base = doc.createElement("Violation");
                base.appendChild(doc.createTextNode(entry.getKey()));
                rootElement.appendChild(base);
                Element price = doc.createElement("Price");
                price.appendChild(doc.createTextNode(String.valueOf(entry.getValue())));
                rootElement.appendChild(price);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("result.xml"));
                transformer.transform(source, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sorting map
     */
    private void sortMap() {
        map = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
