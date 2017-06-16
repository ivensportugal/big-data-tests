import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.util.Locale;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Hashtable;

public class LongestTrajectoryReducer 
	extends Reducer<Text, LongWritable, Text, LongWritable> {

		@Override
		public void reduce(Text key, Iterable<LongWritable> values, Context context)
			throws IOException, InterruptedException {

				long minDate = 0;
				long maxDate = 0;

				for(LongWritable value: values) {

					long eDate = value.get();

					if (minDate == 0 && maxDate == 0) {
						minDate = eDate;
						maxDate = eDate;
					} else if (eDate < minDate)
						minDate = eDate;
					else if (eDate > maxDate)
						maxDate = eDate;
				}

				long diffDate = maxDate - minDate;
				//diffDate = diffDate / (1000 * 60 * 60 * 24);

				context.write(key, new LongWritable(diffDate));


			}
}