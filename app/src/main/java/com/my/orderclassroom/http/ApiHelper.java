package com.my.orderclassroom.http;


public class ApiHelper {

    public  static String baseUrl = "http://piscn.vicp.cc:8084/pub.Web/";

    public static String userLogin() {

        return baseUrl + "student/login.action";
    }

    public static String addReserveClass() {
        return baseUrl + "reserveClass/addOrUpdReserveClass.action";
    }

    public static String getClassroomPage() {
        return baseUrl + "Classroom/getClassroomPage.action";
    }
    public static String getMyReserveClassPage() {
        return baseUrl + "student/getMyReserveClassPage.action";
    }
    public static String cancelReserveClassById() {
        return baseUrl + "reserveClass/cancelReserveClassById.action";
    }

}
