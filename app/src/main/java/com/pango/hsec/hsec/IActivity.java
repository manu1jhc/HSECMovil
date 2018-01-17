package com.pango.hsec.hsec;

/**
 * Created by Andre on 11/12/2017.
 */

public interface IActivity {

    void success(String data, String Tipo);
    void successpost(String data, String Tipo);
    void error(String mensaje, String Tipo);
}
