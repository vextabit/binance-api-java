package org.asynchttpclient;

public class Header {

  public static String get(RequestBuilder builder, String header) {
    return builder.headers.get(header);
  }

  public static void remove(RequestBuilder request, String header) {
    request.headers.remove(header);
  }

}
