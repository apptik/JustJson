package io.apptik.json.wrapper;


import io.apptik.json.JsonElement;

public class JsonObjectMapWrapper<J extends JsonObjectWrapper>
        extends TypedJsonObject<J> {

    Class<J> jobClass;

    public JsonObjectMapWrapper(Class<J> jobClass) {
        this.jobClass = jobClass;
    }

    @Override
    protected J get(JsonElement jsonElement, String key) {
        try {
            return jobClass.newInstance().wrap(jsonElement);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected JsonElement to(J value) {
        return value.getJson();

    }
}
