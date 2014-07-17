package org.djodjo.json.schema;


import org.djodjo.json.JsonElement;
import org.djodjo.json.wrapper.TypedJsonArray;

public class SchemaList extends TypedJsonArray<Schema> {

    //blank schema used to obtain sub-schemas
    public Schema schema;

    public SchemaList(Schema schema) {
        this.schema = schema;
    }

    @Override
    protected Schema get(JsonElement jsonElement, int pos) {
        return (Schema) schema.getEmptySchema(String.valueOf(pos)).wrap(jsonElement);
    }

    @Override
    protected JsonElement to(Schema value) {
        return value.getJson();
    }


}
