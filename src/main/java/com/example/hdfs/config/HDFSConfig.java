package com.example.hdfs.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import java.net.URI;

public class HDFSConfig {
    public static FileSystem getHDFS() throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        return FileSystem.get(new URI("hdfs://localhost:9000"), conf);
    }
}
