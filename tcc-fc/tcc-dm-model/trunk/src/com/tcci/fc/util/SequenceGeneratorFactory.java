package com.tcci.fc.util;

public class SequenceGeneratorFactory {

    private Object object;
    private SequenceGenerator generator;

    public SequenceGeneratorFactory(Object object) {
        this.object = object;
    }

    public SequenceGenerator createSequenceGenerator() {
        if (this.generator == null) {
            this.generator = createSequenceGenerator(this.object);
        }
        return this.generator;
    }

    public SequenceGenerator createSequenceGenerator(Object object) {
        if (this.object == null) {
            this.generator = new DefaultSequenceGenerator();
        } else {
            // find related configurtions of object from properties and 
            // create corresponding SequenceGenerator here.
            this.generator = new DefaultSequenceGenerator();
        }
        
        return this.generator;
    }
}