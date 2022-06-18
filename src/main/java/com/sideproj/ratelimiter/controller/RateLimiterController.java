package com.sideproj.ratelimiter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class RateLimiterController {
    @RequestMapping(value = "/**")
    public void rateLimit(HttpServletRequest req, HttpServletResponse res) {
        System.out.println(req.getServletPath());
    }
}
