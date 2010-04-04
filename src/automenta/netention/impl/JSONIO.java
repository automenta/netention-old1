/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.impl;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *
 * @author seh
 */
public class JSONIO {

//    public static void saveSelf(MemorySelf self, String path) throws Exception {
//        save(self, path, "patterns", "properties");
//    }
//
//    public static void saveSelf(MemorySelf self, String path) throws Exception {
//        save(self, path, "patterns", "properties");
//    }
//
//    public static void saveSchema(MemorySelf self, String path) throws Exception {
//        save(self, path, "id", "name", "details", "links");
//    }
    
    public static void save(MemorySelf self, String path, String... excludes) throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        //String output = serializer./*exclude(excludes)*/prettyPrint(self);
        String output = serializer.include("id", "name", "properties", "patterns", "details").deepSerialize(self);
        FileOutputStream fout = new FileOutputStream(path);
        DataOutputStream dout = new DataOutputStream(fout);
        dout.writeUTF(output);
        fout.close();
    }

    public static MemorySelf load(String path) throws Exception {
        FileInputStream fout = new FileInputStream(path);
        DataInputStream oos = new DataInputStream(fout);
        String input = oos.readUTF();

        MemorySelf self = new JSONDeserializer<MemorySelf>().deserialize(input);

        oos.close();
        return self;
    }
}
