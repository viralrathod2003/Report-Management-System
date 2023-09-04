package com.iris.springboot.pojo;

import com.iris.springboot.entity.Users;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
public class RequestDetails {
    private Request request;
    private Users employee;
    private Report report;
    private List<Action> actions = new ArrayList<>();
    private Map<Integer, List<Users>> approversByLevel = new HashMap<>();
}
