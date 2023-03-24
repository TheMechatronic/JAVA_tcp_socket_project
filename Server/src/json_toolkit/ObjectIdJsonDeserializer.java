package json_toolkit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdJsonDeserializer extends StdDeserializer<ObjectId> {
    public ObjectIdJsonDeserializer() {
        super(ObjectId.class);
    }

    @Override
    public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String hex = p.getValueAsString();
        return new ObjectId(hex);
    }
}
