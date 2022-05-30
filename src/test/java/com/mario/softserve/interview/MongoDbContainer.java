package com.mario.softserve.interview;

import org.testcontainers.containers.GenericContainer;

public class MongoDbContainer extends GenericContainer<MongoDbContainer> {

    private static final int PORT = 27017;
    private static final String IMAGE = "mongo:5.0.8";
    public MongoDbContainer() {
        this(IMAGE);
    }
    public MongoDbContainer(String image) {
        super(image);
        addExposedPort(PORT);
    }

    public Integer getPort() {
        return getMappedPort(PORT);
    }
}
