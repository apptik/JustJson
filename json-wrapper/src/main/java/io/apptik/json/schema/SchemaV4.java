package io.apptik.json.schema;


import io.apptik.json.Validator;
import io.apptik.json.schema.validation.SchemaV4Validator;

import java.net.URI;
import java.net.URISyntaxException;

public class SchemaV4 extends Schema {

    public SchemaV4() {
        super();
        setSchema("http://json-schema.org/draft-04/schema#");
    }

    @Override
    public Schema getEmptySchema(String path) {
        SchemaV4 res=  new SchemaV4().setSchemaFetcher(schemaFetcher);
        URI newScr = null;
        if(this.origSrc!=null && path!=null) {
            try {
                if(origSrc.getFragment() == null) {

                    newScr = new URI(getOrigSrc().getScheme(), origSrc.getAuthority(),origSrc.getPath(), origSrc.getQuery(), path);

                } else {
                    newScr = new URI(getOrigSrc().getScheme(),origSrc.getAuthority(),origSrc.getPath(), origSrc.getQuery(), origSrc.getFragment() + "/" + path);
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        res.setOrigSrc(newScr);
        return res;
    }

    @Override
    public Validator getDefaultValidator() {
        return new SchemaV4Validator(this);
    }
}
