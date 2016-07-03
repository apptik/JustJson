package io.apptik.json.modelgen;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.*;
import io.apptik.json.JsonElement;
import io.apptik.json.JsonObject;
import io.apptik.json.modelgen.util.UriUtils;
import io.apptik.json.modelgen.util.WordUtils;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.fetch.SchemaUriFetcher;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtlGenerator implements Generator {

    private final Configuration cfg;

    public FtlGenerator() {
        cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setObjectWrapper(new MyAppObjectWrapper(cfg.getIncompatibleImprovements()));
    }

    private Template getJobTemplate() throws IOException {
        return new Template(Util.JOB_FTL_TEMPLATE, new InputStreamReader(Util.getJOBftl()), cfg);
    }

    private Writer getWrapperWriter(String wrapperName, File outDir) throws IOException {
        File file = new File(outDir, WordUtils.capitalize(wrapperName) + ".java");
        if (!outDir.exists()) {
            System.out.println("creating directory: " + outDir.getPath());
            outDir.mkdirs();
        }
        //        Writer out = new OutputStreamWriter(System.out);
        return new FileWriter(file);
    }

    private void process(Map<String, Object> data, Writer writer, Template template) throws IOException, TemplateException {
        template.process(data, writer);
        //System.out.println("Processed: " + file.getPath());
    }

    private void writeWrapper(Map<String, Object> data, Template template, String wrapperName, File outDir)
            throws IOException {
        Writer writer = getWrapperWriter(wrapperName, outDir);
        try {
            process(data, writer, template);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        writer.flush();
        writer.close();
    }

    @Override
    public void fromJson(JsonObject job, String wrapperName, File outDir) throws IOException {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("wrapperName", WordUtils.capitalize(wrapperName));
        List<Prop> props = new ArrayList<Prop>();
        for (Map.Entry<String, JsonElement> el : job) {
            Prop p = Util.getPropFromJsonEntry(el.getKey(), el.getValue());
            props.add(p);
            System.out.println("Adding: " + p);
        }
        data.put("properties", props);

        writeWrapper(data, getJobTemplate(), wrapperName, outDir);
    }

    @Override
    public void fromSchema(Schema schema, String wrapperName, File outDir) throws IOException {
        if (wrapperName == null) {
            wrapperName = schema.getTitle();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("wrapperName", WordUtils.capitalize(wrapperName));
        List<Prop> props = new ArrayList<Prop>();

        for (Map.Entry<String, Schema> el : schema.getProperties()) {
            Prop p = Util.getPropFromJsonSchema(el.getKey(), el.getValue());
            props.add(p);
            System.out.println("Adding: " + p);
        }
        data.put("properties", props);
        writeWrapper(data, getJobTemplate(), wrapperName, outDir);
    }


    public static class PropAdapter extends WrappingTemplateModel implements TemplateHashModel,
            AdapterTemplateModel {

        private final Prop prop;

        public PropAdapter(Prop prop, ObjectWrapper objectWrapper) {
            super(objectWrapper);
            this.prop = prop;
        }

        @Override
        public Object getAdaptedObject(Class hint) {
            return prop;
        }

        @Override
        public TemplateModel get(String key) throws TemplateModelException {
            if (key.equals("origKey")) return wrap(prop.origKey);
            if (key.equals("methodName")) return wrap(prop.methodName);
            if (key.equals("returnType")) return wrap(prop.returnType);
            if (key.equals("optMethod")) return wrap(prop.optMethod);
            else return null;
        }

        @Override
        public boolean isEmpty() throws TemplateModelException {
            return false;
        }
    }

    public static class MyAppObjectWrapper extends DefaultObjectWrapper {

        public MyAppObjectWrapper(Version incompatibleImprovements) {
            super(incompatibleImprovements);
        }

        @Override
        protected TemplateModel handleUnknownType(final Object obj) throws TemplateModelException {
            if (obj instanceof Prop) {
                return new PropAdapter((Prop) obj, this);
            }

            return super.handleUnknownType(obj);
        }

    }
}
