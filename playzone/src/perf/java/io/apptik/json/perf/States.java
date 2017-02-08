package io.apptik.json.perf;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

public class States {

    private States() {
    }

    @State(Scope.Thread)
    public static class GeneralParams {

        @Param({
                "1",
//                "1000",
//                "1000000"
        })
        public int elements;

        @Param
        public JsonInput jsonInput;

        String jsonString;
        public Gson gson = new Gson();
        public ObjectMapper jackson = new ObjectMapper();
        public ObjectMapper jacksonAfterburner = new ObjectMapper();


        @Setup(Level.Iteration)
        public void setup() {
            jsonString = jsonInput.create();

        }

        @TearDown(Level.Iteration)
        public void tearDown() {
        }
    }



}
