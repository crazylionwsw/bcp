package com.fuze.bcp.entity;

public class StringTemplateSource {
    private final String name;
    private final String source;
    private final long lastModified;

    public StringTemplateSource(String name, String source, long lastModified) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        }
        if (lastModified < -1L) {
            throw new IllegalArgumentException("lastModified < -1L");
        }
        this.name = name;
        this.source = source;
        this.lastModified = lastModified;
    }

    public boolean equals(Object obj) {
        if (obj instanceof StringTemplateSource) {
            return name.equals(((StringTemplateSource) obj).name);
        }
        return false;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public long getLastModified() {
        return lastModified;
    }
}