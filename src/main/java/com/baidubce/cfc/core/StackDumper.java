package com.baidubce.cfc.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

class StackDumper {
    static void dumpStackTrace(Throwable t, OutputStream out) {
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = null;
        try {
            generator = factory.createGenerator(out);
            generator.writeStartObject();
            generator.writeStringField("errorMessage", t.getMessage());
            generator.writeStringField("errorType", t.getClass().getName());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(stream);
            StackTraceElement[] trace = t.getStackTrace();
            for (StackTraceElement traceElement : trace) {
                if (!traceElement.getClassName().startsWith("org.java_websocket")) {
                    printStream.println(traceElement);
                }
            }
            generator.writeStringField("stackTrace", stream.toString());
            generator.writeEndObject();
            generator.close();
            stream.close();
            printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
