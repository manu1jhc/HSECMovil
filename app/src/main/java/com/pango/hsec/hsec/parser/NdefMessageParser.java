package com.pango.hsec.hsec.parser;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import com.pango.hsec.hsec.record.ParsedNdefRecord;
import com.pango.hsec.hsec.record.TextRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BOB on 24/06/2018.
 */

public class NdefMessageParser {

    private NdefMessageParser(){}

    public static List<ParsedNdefRecord> parse(NdefMessage message){
        return getRecords(message.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();
        for (final NdefRecord record:records){
            if(TextRecord.isText(record)) elements.add(TextRecord.parse(record));
            else elements.add(()->{ return new String(record.getPayload());});
        }
        return elements;
    }


}
