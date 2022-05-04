package com.example.demo;

import com.example.demo.readClasses.ConnectionsDetailsReader;
import com.example.demo.readClasses.ConnectionsReader;
import com.example.demo.readClasses.DeparturesReader;
import com.example.demo.readClasses.LineCPReader;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("cp")
public class Controller {

    @GetMapping("/dep")
    public DeparturesReader getDepartures(@RequestParam(value = "vehicle", defaultValue = "null") String vehicle,
                                          @RequestParam(value = "from", defaultValue = "null") String from,
                                          @RequestParam(value = "time", defaultValue = "null") String time,
                                          @RequestParam(value = "date", defaultValue = "null") String date,
                                          @RequestParam(value = "arr", defaultValue = "null") String arrivals) {
        return new DeparturesReader(vehicle, from, time, date, arrivals);
    }

    @GetMapping("/zcp")
    public LineCPReader getStationCP(@RequestParam(value = "city", defaultValue = "null") String city,
                                     @RequestParam(value = "line", defaultValue = "null") String line,
                                     @RequestParam(value = "from", defaultValue = "null") String from,
                                     @RequestParam(value = "to", defaultValue = "null") String to,
                                     @RequestParam(value = "date", defaultValue = "null") String date) {
        return new LineCPReader(city, line, from, to, date);
    }

    @GetMapping("/con")
    public ConnectionsReader getConnections(@RequestParam(value = "vehicle", defaultValue = "null") String vehicle,
                                            @RequestParam(value = "from", defaultValue = "null") String from,
                                            @RequestParam(value = "to", defaultValue = "null") String to,
                                            @RequestParam(value = "via", defaultValue = "null") String via,
                                            @RequestParam(value = "time", defaultValue = "null") String time,
                                            @RequestParam(value = "date", defaultValue = "null") String date,
                                            @RequestParam(value = "dir", defaultValue = "null") String direct,
                                            @RequestParam(value = "maxTran", defaultValue = "null") String maxTransports) {
        return new ConnectionsReader(vehicle, from, to, via, time, date, direct, maxTransports);
    }

    @GetMapping("/cond")
    public ConnectionsDetailsReader getConnectionsDetails(@RequestParam(value = "url", defaultValue = "null") String url) {
        return new ConnectionsDetailsReader(url);
    }
}

