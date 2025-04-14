import java.util.ArrayList;

public class IntervalPath {
		static class Interval {
			private int start;
			private int end;
			
			Interval() {
				this.start = 0;
				this.end = 0;
			}
			
			Interval(int start, int end) {
				this.start = start;
				this.end = end;
			}
			
			public int getStart() {
				return this.start;
			}
			
			public int getEnd() {
				return this.end;
			}
			
			public int size() {
				return this.end - this.start;
			}
			
			// Tries to extend an interval by another interval, otherwise returns null
			public Interval extend(Interval other) {
				if(other.start == this.end) {
					return new Interval(this.start, other.end);
				}
				return null;
			}
			
			@Override
			public String toString() {
				return "" + this.start + "->" + this.end;
			}
			
			// [0, 1] -> [0, 1]
			// [0, 1, 2, 4, 5] -> [0, 2, 4, 5] as 1 is between 0 and 2 and there is nothing between 4 and 5
			// 
			// NOTE: This assumes a correct interval graph
			public static ArrayList<Integer> getInsertionSpots(ArrayList<Integer> indices) {
				ArrayList<Integer> spots = new ArrayList<Integer>(indices.size());
				int prev = -1;
				for(int i = 0; i < indices.size(); i++) {
					int spot = indices.get(i);
					if(spots.isEmpty()) {
						spots.add(spot);
						prev = spot;
						continue;
					}
					
					if(spot - prev <= 1) {
						prev = spot; 
						continue;
					} else {
						// We've moved to another interval
						spots.add(prev);
						spots.add(spot);
						prev = spot;
						continue;
					}
				}
				
				spots.add(prev);
				
				return spots;
			}
			
			public static ArrayList<Interval> getInterval(ArrayList<Integer> indices) {
				ArrayList<Integer> inSpots = Interval.getInsertionSpots(indices);
				ArrayList<Interval> intervals = new ArrayList<>(inSpots.size());
				
				int i = 0;
				while(true) {
					if(i >= inSpots.size()) {
						break;
					}
					intervals.add(new Interval(inSpots.get(i), inSpots.get(i+1)));
					i+=2;
				}			
				return intervals;
			}
		}
		
		Interval interval;
		ArrayList<String> currentPath;
		
		public IntervalPath() {
			this.interval = new Interval();
			this.currentPath = new ArrayList<>();
		}
		
		public IntervalPath(Interval interval, ArrayList<String> currentPath) {
			this.interval = interval;
			this.currentPath = currentPath;
		}

		IntervalPath add(String node, Interval interval) {
			Interval newInterval = this.interval.extend(interval);
			if(newInterval == null) {
				return null;
			}
			
			ArrayList<String> currentPath = new ArrayList<>(this.currentPath);
			currentPath.add(node);
			return new IntervalPath(newInterval, currentPath);
		}
		
		@Override
		public String toString() {
			return "" + this.currentPath;
		}
	}
