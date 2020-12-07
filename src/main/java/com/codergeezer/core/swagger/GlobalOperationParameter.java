package com.codergeezer.core.swagger;

/**
 * @author haidv
 * @version 1.0
 */
public class GlobalOperationParameter {

    private String name;

    private String description;

    private String modelRef;

    private String parameterType;

    private boolean required;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getModelRef() {
        return this.modelRef;
    }

    public void setModelRef(final String modelRef) {
        this.modelRef = modelRef;
    }

    public String getParameterType() {
        return this.parameterType;
    }

    public void setParameterType(final String parameterType) {
        this.parameterType = parameterType;
    }

    public boolean getRequired() {
        return this.required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }
}
