package com.pango.hsec.hsec;

public interface IActivity {

    void success(String data, String Tipo) throws CloneNotSupportedException;
    void successpost(String data, String Tipo) throws CloneNotSupportedException;
    void error(String mensaje, String Tipo);
}
