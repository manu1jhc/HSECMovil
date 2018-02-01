package com.pango.hsec.hsec;

public interface IActivity {

    void success(String data, String Tipo);
    void successpost(String data, String Tipo);
    void error(String mensaje, String Tipo);
}
