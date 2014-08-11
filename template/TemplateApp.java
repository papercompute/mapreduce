import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

import java.io.IOException;
import java.util.*;

public final class TemplateApp {

  public static class Map extends Mapper<Text, Text, Text, Text> {
    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
      System.out.println("key: "+key.toString().trim());
      System.out.println("val: "+value.toString().trim());
      // just translate key/value
      context.write(key,value);
    }
  }

  public static class Reduce extends Reducer<Text, Text, Text, Text> {  
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
       String result=new String();
       System.out.println("reduce:"+key.toString());
       // collect by key
       for (Text value : values) {
        result+=value.toString();
       }
       context.write(key, new Text(result));
    }
  }

  public static void main(String... args) throws Exception {
    runJob(args[0], args[1]);
  }

  public static void runJob(String input, String output) throws Exception {  

    Configuration conf = new Configuration();
    conf.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", ",");
    Job job = new Job(conf);
    job.setJarByClass(TemplateApp.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    job.setInputFormatClass(KeyValueTextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);

    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.setInputPaths(job, new Path(input));
    Path outPath = new Path(output);
    FileOutputFormat.setOutputPath(job, outPath);

    outPath.getFileSystem(conf).delete(outPath, true);

    job.waitForCompletion(true);
  }
}
