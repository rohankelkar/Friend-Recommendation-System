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
 
public class FriendshipRecommender {
 
    public static class Map extends Mapper<LongWritable, Text, IntWritable, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] userAndFriends = line.split("\t");
            if (userAndFriends.length == 2) {
                String user = userAndFriends[0];
                IntWritable userKey = new IntWritable(Integer.parseInt(user));
                String[] friends = userAndFriends[1].split(",");
                String friend1;
                IntWritable friend1Key = new IntWritable();
                Text friend1Value = new Text();
                String friend2;
                IntWritable friend2Key = new IntWritable();
                Text friend2Value = new Text();
                for (int i = 0; i < friends.length; i++) {
                    friend1 = friends[i];
                    friend1Value.set("1," + friend1);
                    context.write(userKey, friend1Value);   // Paths of length 1.
                    friend1Key.set(Integer.parseInt(friend1));
                    friend1Value.set("2," + friend1);
                    for (int j = i+1; j < friends.length; j++) {
                        friend2 = friends[j];
                        friend2Key.set(Integer.parseInt(friend2));
                        friend2Value.set("2," + friend2);
                        context.write(friend1Key, friend2Value);   // Paths of length 2.
                        context.write(friend2Key, friend1Value);   // Paths of length 2.
                    }
                }
            }
        }
    } 
}
