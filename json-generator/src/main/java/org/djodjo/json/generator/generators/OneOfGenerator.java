package org.djodjo.json.generator.generators;


import org.djodjo.json.JsonElement;
import org.djodjo.json.generator.Generator;
import org.djodjo.json.generator.GeneratorConfig;
import org.djodjo.json.schema.Schema;
import org.djodjo.json.schema.SchemaList;

public class OneOfGenerator extends Generator {

    public OneOfGenerator(Schema schema, GeneratorConfig configuration) {
        super(schema, configuration);
    }

    public OneOfGenerator(Schema schema, GeneratorConfig configuration, String propertyName) {
        super(schema, configuration, propertyName);
    }

    @Override
    public JsonElement generate() {
        SchemaList list =  schema.getOneOf();
        Schema choice = list.get(rnd.nextInt(list.size()));
        return new Generator(choice, configuration).generate();
    }
}
