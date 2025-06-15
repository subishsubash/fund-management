package com.subash.fund.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Version Controller have API to fetch the current version of APIs.
 */
@RestController
public class VersionController {

    /**
     * Helps to fetch API version
     *
     * @return
     */
    @GetMapping("/version")
    public String getVersion() {
        return "v1";
    }
}
