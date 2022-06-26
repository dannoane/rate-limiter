package com.sideproj.ratelimiter.controller;

import lombok.val;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class RateLimiterController {
    private final OkHttpClient httpClient = new OkHttpClient();
    private static final Set<String> methodsWithoutBody = new HashSet<>(Arrays.asList("get", "head"));

    @RequestMapping(value = "/**")
    public void rateLimit(HttpServletRequest orgReq, HttpServletResponse orgRes) {
        System.out.println(orgReq.getServletPath());

        val path = orgReq.getServletPath();
        val targetUrl = "http://localhost:8000";

        try {
            val requestBuilder = new Request.Builder().url(targetUrl + path);

            if (acceptsBody(orgReq)) {
                requestBuilder.method(orgReq.getMethod(), buildRequestBody(orgReq));
            }

            getHeaders(orgReq).forEach((headerName, headerValues) -> {
                headerValues.forEach(headerValue -> requestBuilder.addHeader(headerName, headerValue));
            });

            val request = requestBuilder.build();
            val response = httpClient.newCall(request).execute();

            getHeaders(response).forEach((headerName, headerValues) -> {
                headerValues.forEach(headerValue -> orgRes.addHeader(headerName, headerValue));
            });

            orgRes.setStatus(response.code());

            orgRes.getWriter().write(response.body().string());
            orgRes.getWriter().flush();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }

    private static boolean acceptsBody(HttpServletRequest req) {
        return !methodsWithoutBody.contains(req.getMethod().toLowerCase());
    }

    private static RequestBody buildRequestBody(HttpServletRequest req) throws IOException {
        val body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return RequestBody.create(body.getBytes(StandardCharsets.UTF_8));
    }

    private static Map<String, ArrayList<String>> getHeaders(HttpServletRequest req) {
        return Collections.list(req.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(Function.identity(), header -> Collections.list(req.getHeaders(header))));
    }

    private static Map<String, ArrayList<String>> getHeaders(Response res) {
        Map<String, ArrayList<String>> headers = new HashMap<>();

        res.headers().forEach(header -> {
            val headerName = header.component1().lines().collect(Collectors.joining());
            val headerValue = header.component2().lines().collect(Collectors.joining());

            headers.compute(headerName, (key, value) -> {
                if (value == null) {
                    return new ArrayList<>(List.of(headerValue));
                } else {
                    value.add(headerValue);
                    return value;
                }
            });
        });

        return headers;
    }
}
