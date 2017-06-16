import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Hashtable;

public class LongestTrajectoryMapper
	extends Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException, RuntimeException {

				String line   = value.toString();
				String[] data = line.split(",");
				String trajID = data[3];

				DateFormat format = new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss\"", Locale.ENGLISH);
				long date;
				try {
					date = format.parse(data[4]).getTime();
				} catch (ParseException e) {
					throw new RuntimeException();
				}
				
				Hashtable<String, long[]> dates = new Hashtable<String, long[]>();

				long eDate[] = dates.get(trajID);

				if(eDate != null) {

					if (date < eDate[0]) {
						long tDate[] = new long[2];
						tDate[0] = date;
						tDate[1] = eDate[1];

						dates.remove(trajID);
						dates.put(trajID, tDate);
					} else if (date > eDate[1]) {
						long tDate[] = new long[2];
						tDate[0] = eDate[0];
						tDate[1] = date;

						dates.remove(trajID);
						dates.put(trajID, tDate);
					}

				} else {
					long aDate[] = new long[2];
					aDate[0] = date;
					aDate[1] = date;
					dates.put(trajID, aDate);
				}

				for(String aKey: dates.keySet()) {
					context.write(new Text(aKey), new LongWritable(dates.get(aKey)[0]));
					context.write(new Text(aKey), new LongWritable(dates.get(aKey)[1]));
				}	
			}
	}