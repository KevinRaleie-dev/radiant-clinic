package com.example.radiant.repo;

public interface AsyncTaskCallback {
    void handleResponse(String object);
    void handleFault(Exception e);
}
