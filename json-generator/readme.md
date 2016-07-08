# Json Generator

Simple tool to generate json from Json-schema.
    

##Download

Maven:

    <dependency>
        <groupId>io.apptik.json</groupId>
        <artifactId>json-generator</artifactId>
        <version>1.0.4-SNAPSHOT</version>
    </dependency>


Gradle:

    'io.apptik.json:json-generator:1.0.4-SNAPSHOT'

##Customizable option
* set custom propeties on all values(globals) or specific to the element key
* set custom set of options to be chosen pseudo randomly for almost all Json Schema properties
* custom values which do fit the schema are ignored
* element specific values are chosen ovet the global ones


##Usage

examples are available at: https://github.com/apptik/JustJson/tree/master/examples/src/main/java/org.djodjo.json.examples/jsongenerator

create basic schema object:

     Schema schema = new SchemaV4().wrap(JsonElement.readFrom("{the schema}"));

### Simple/Default

    new Generator(schema, null).generate();

### Customized

    GeneratorConfig gConf = new GeneratorConfig();
    ArrayList<String> images =  new ArrayList<String>();
    images.add("/photos/image.jpg");
    images.add("/photos/image.jpg");
    gConf.uriPaths.put("seven", images);
    gConf.globalArrayItemsMax = 7;
    gConf.globalIntegerMin = 0;
    gConf.globalIntegerMax = 100;
    //can still limit numbers as long as its still valid according to the schema
    gConf.integerMin.put("five1", 300);
    gConf.integerMax.put("five1", 400);
    gConf.skipObjectProperties.add("two");
    return  new Generator(schema, gConf).generate();
    
