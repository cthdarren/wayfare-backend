package com.wayfare.backend.request;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

public record LocationRequest(double longitude, double latitude, double kmdistance){

}
