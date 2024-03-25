package com.wayfare.backend.request;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

public record TestReq (Point location, Distance distance){

}
