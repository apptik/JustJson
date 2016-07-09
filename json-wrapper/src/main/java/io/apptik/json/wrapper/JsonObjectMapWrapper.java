package io.apptik.json.wrapper;


import io.apptik.json.JsonElement;
import io.apptik.json.JsonObject;

public class JsonObjectMapWrapper<J extends JsonObjectWrapper>
        extends TypedJsonObject<J> {

    Class<J> jobClass;

    public JsonObjectMapWrapper(Class<J> jobClass) {
        this.jobClass = jobClass;
    }

    @Override
    protected J get(JsonElement jsonObject, String key) {
        try {
            return jobClass.newInstance().wrap(jsonObject.asJsonObject());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected JsonObject to(J value) {
        return value.getJson();

    }
}
