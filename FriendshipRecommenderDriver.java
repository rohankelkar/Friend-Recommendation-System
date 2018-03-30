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
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FriendshipRecommenderDriver {

	public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
 
        Job job = new Job(conf, "FriendshipRecommender");
        job.setJarByClass(FriendshipRecommender.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
 
        job.setMapperClass(FriendshipRecommender.Map.class);
        job.setReducerClass(Reduce.class);
 
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
        FileInputFormat.setInputPaths(job, new Path("inputf"));
		FileOutputFormat.setOutputPath(job, new Path("output"));
 
       /* FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));*/
 
        job.waitForCompletion(true);
    }
}
