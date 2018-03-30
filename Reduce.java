package FB_friend_recommender;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
 
import org.apache.commons.lang.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reduce extends Reducer<IntWritable, Text, IntWritable, Text> {
    public void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String[] value;
        HashMap<String, Integer> hash = new HashMap<String, Integer>();
        for (Text val : values) {
            value = (val.toString()).split(",");
            if (value[0].equals("1")) { // Paths of length 1.
                hash.put(value[1], -1);
            } else if (value[0].equals("2")) {  // Paths of length 2.
                if (hash.containsKey(value[1])) {
                    if (hash.get(value[1]) != -1) {
                        hash.put(value[1], hash.get(value[1]) + 1);
                    }
                } else {
                    hash.put(value[1], 1);
                }
            }
        }
        // Convert hash to list and remove paths of length 1.
        ArrayList<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
        for (Entry<String, Integer> entry : hash.entrySet()) {
            if (entry.getValue() != -1) {   // Exclude paths of length 1.
                list.add(entry);
            }
        }
        // Sort key-value pairs in the list by values (number of common friends).
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });
        int MAX_RECOMMENDATION_COUNT = 10;
        if (MAX_RECOMMENDATION_COUNT < 1) {
            // Output all key-value pairs in the list.
            context.write(key, new Text(StringUtils.join(list, ",")));
        } else {
            // Output at most MAX_RECOMMENDATION_COUNT keys with the highest values (number of common friends).
            ArrayList<String> top = new ArrayList<String>();
            for (int i = 0; i < Math.min(MAX_RECOMMENDATION_COUNT, list.size()); i++) {
                top.add(list.get(i).getKey());
            }
            context.write(key, new Text(StringUtils.join(top, ",")));
        }
    }
}
