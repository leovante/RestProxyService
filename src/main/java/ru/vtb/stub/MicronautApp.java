package ru.vtb.stub;

import io.micronaut.runtime.Micronaut;

public class MicronautApp {
    public static void main(String[] args) {
        Micronaut.build(args)
                .eagerInitSingletons(true)
                .mainClass(MicronautApp.class)
                .start();
    }
}
