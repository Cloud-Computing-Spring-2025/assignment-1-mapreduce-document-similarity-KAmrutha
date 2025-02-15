package com.example;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.hadoop.mapreduce.lib.input.FileSplit; 
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DocumentSimilarityMapper extends Mapper<Object, Text, Text, Text> {

    private StringBuilder docContent = new StringBuilder();
    private String fileName = "";
    private final static Text constantKey = new Text("doc");
    private final static Text outValue = new Text();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        fileName = fileSplit.getPath().getName();
    }

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
       
        docContent.append(value.toString()).append(" ");
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        
        String content = docContent.toString().trim();
        if (content.isEmpty()) {
            return;
        }
        String[] tokens = content.split("\\s+");
        Set<String> words = new HashSet<>();
        for (String token : tokens) {
            
            String word = token.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
            if (!word.isEmpty()) {
                words.add(word);
            }
        }
       
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            sb.append(w).append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1); 
        }
        outValue.set(fileName + "\t" + sb.toString());
        context.write(constantKey, outValue);
    }
}
