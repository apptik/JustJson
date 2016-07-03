package io.apptik.json.generator.generators;


import io.apptik.json.JsonElement;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaList;

public class OneOfGenerator extends JsonGenerator {

    public OneOfGenerator(Schema schema, JsonGeneratorConfig configuration) {
        super(schema, configuration);
    }

    public OneOfGenerator(Schema schema, JsonGeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    @Override
    public JsonElement generate() {
        SchemaList list =  schema.getOneOf();
        Schema choice = list.get(rnd.nextInt(list.size()));
        return new JsonGenerator(choice, configuration).generate();
    }
}
