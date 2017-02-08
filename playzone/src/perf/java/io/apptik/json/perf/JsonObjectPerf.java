package io.apptik.json.perf;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.apptik.json.JsonElement;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class JsonObjectPerf {

    @Benchmark
    public Object jjsonRead(States.GeneralParams p) throws InterruptedException, IOException {
        return JsonElement.readFrom(p.jsonString);
    }
    //@Benchmark
    public Object gsonRead(States.GeneralParams p) throws InterruptedException, IOException {
        return new com.google.gson.JsonParser().parse(p.jsonString);

    }
    //@Benchmark
    public Object jacksonRead(States.GeneralParams p) throws InterruptedException, IOException {
        return p.jackson.readTree(p.jsonString);
    }

    //@Benchmark
    public Object jacksonAfterBurnerRead(States.GeneralParams p) throws InterruptedException, IOException {
        return p.jacksonAfterburner.readTree(p.jsonString);
    }


}
