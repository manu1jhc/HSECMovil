package com.pango.hsec.hsec.record;


import android.annotation.SuppressLint;
import android.nfc.NdefRecord;
import androidx.core.util.Preconditions;

import java.util.Arrays;

/**
 * Created by BOB on 25/06/2018.
 */

public class TextRecord implements ParsedNdefRecord {

    private final String mLanguajeCode;
    private final String mText;

    @SuppressLint("RestrictedApi")
    public TextRecord(String languajeCode, String text){
        mLanguajeCode = Preconditions.checkNotNull(languajeCode);
        mText=  Preconditions.checkNotNull(text);
    }
    public String str() {
        return mText;
    }

    public String getLanguajeCode() {
        return mLanguajeCode;
    }

    public String getText() {
        return mText;
    }

    @SuppressLint("RestrictedApi")
    public static TextRecord parse(NdefRecord record){
        Preconditions.checkArgument(record.getTnf()==NdefRecord.TNF_WELL_KNOWN);
        Preconditions.checkArgument(Arrays.equals(record.getType(),NdefRecord.RTD_TEXT));
        try {
            byte[] payload = record.getPayload();
            String textEncoding =((payload[0]&0200)==0)?"UTF-8":"UTF-16";
            int languajeCodeLength = payload[0]&0077;
            String languajeCode= new String(payload,1,languajeCodeLength,"US-ASCII");
            String text= new String(payload,languajeCodeLength+1,payload.length-languajeCodeLength-1,textEncoding);
            return new TextRecord(languajeCode,text);
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public static boolean isText(NdefRecord record){
        try{
                parse(record);
                return true;
        }
        catch (IllegalArgumentException e){
            return false;
        }
    }
}
