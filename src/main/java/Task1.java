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
import java.util.*;

import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Task1 {

    Map<String, Double> map = new HashMap<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        File folder = new File("C:\\Users\\Дима\\Downloads\\task3\\violation");
        File[] listOfFiles = folder.listFiles();
        Task1 task1 = new Task1();
        assert listOfFiles != null;
        ExecutorService service = Executors.newFixedThreadPool(2);


        for (int i = 0; i < listOfFiles.length; i++) {
            int finalI = i;
            CompletableFuture<Future<Map<String, Double>>> future =
                    CompletableFuture.supplyAsync(() ->

                            service.submit(() -> task1.readFile(new File(listOfFiles[finalI].getName()))), service);

            System.out.println(future.get().get().entrySet() + Thread.currentThread().getName());
        }
        service.shutdown();
        task1.createXml();
    }

    private Map<String, Double> readFile(File newFile) {
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(new FileReader("C:\\Users\\Дима\\Downloads\\task3\\violation\\" + newFile));
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

    private void sortMap() {
        map = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
