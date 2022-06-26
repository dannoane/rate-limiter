package com.sideproj.ratelimiter.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Endpoint {
    public enum Strategy {
        TOKEN_BUCKET,
        LEAKING_BUCKET,
        FIXED_WINDOW_COUNTER,
        SLIDING_WINDOW_LOG,
        SLIDING_WINDOW_COUNTER
    }

    public enum LimitBy {
        ALL_REQUESTS,
        IP,
    }

    private String path;
    private int numberOfRequests;
    private int window;
    private Strategy strategy;
    private LimitBy limitBy;
    private ArrayList<Endpoint> endpoints;
}
