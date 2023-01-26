package com.example.app2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class SampleController {

    @GetMapping("/")
    public ResponseEntity<?> index(HttpSession session){
        UUID uid = Optional.ofNullable(UUID.class.cast(session.getAttribute("uid")))
                .orElse(UUID.randomUUID());
        session.setAttribute("uid", uid);

        Map result = new HashMap();
        result.put("SessionID", session.getId());
        result.put("UUID", session.getAttribute("uid"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/invalidate")
    public ResponseEntity<?> invalidate(HttpSession session){
        session.invalidate();
        return new ResponseEntity<>("Invalidated", HttpStatus.OK);
    }
}
