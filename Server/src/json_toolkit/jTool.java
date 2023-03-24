package json_toolkit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class jTool<T> {

    public String objectToJSON(T objs) throws Exception {
        // create an ObjectMapper instance to convert the Book objects to JSON
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdJsonSerializer());
        module.addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
        objectMapper.registerModule(module);

        // convert the list of Book objects to a JSON string
        return objectMapper.writeValueAsString(objs);
    }

    public String objectListToJSON(List<T> objs) throws Exception {
        // create an ObjectMapper instance to convert the Book objects to JSON
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdJsonSerializer());
        module.addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
        objectMapper.registerModule(module);

        // convert the list of Book objects to a JSON string
        return objectMapper.writeValueAsString(objs);
    }

    public List<T> JSONtoObjectList(String json) throws Exception {
        // create an ObjectMapper instance to convert the JSON to Book objects
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdJsonSerializer());
        module.addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
        objectMapper.registerModule(module);

        // use the ObjectMapper to deserialize the JSON string into a List of Book objects
        return objectMapper.readValue(json, new TypeReference<List<T>>() {});
    }

    public static <T> T JSONtoObject(String json, Class<T> objectType) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // This is only for the ObjectId class parsing ...
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdJsonSerializer());
        module.addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
        objectMapper.registerModule(module);

        return objectMapper.readValue(json, objectType);
    }

    public static <T> ArrayList<T> convertArrayList(List<?> objectList, Class<T> clazz) {
        ArrayList<T> resultList = new ArrayList<T>();

        ObjectMapper objectMapper = new ObjectMapper();

        // This is only for the ObjectId class parsing ...
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdJsonSerializer());
        module.addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
        objectMapper.registerModule(module);

        // Convert ArrayList item to object ...

        for (Object obj : objectList) {
            // System.out.println("Try conversion to: " + clazz.toString() + "From: " + obj.getClass());
            objectMapper.convertValue(obj, clazz);
            if (clazz.isInstance(objectMapper.convertValue(obj, clazz))) {
                resultList.add(objectMapper.convertValue(obj, clazz));
            }
        }
        return resultList;
    }

}
