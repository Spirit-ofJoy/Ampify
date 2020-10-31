package ampify_player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* A class to store SRT information.
*
* @author Sayed Zameer Qasim
*/

public class SRT implements Comparable<SRT>{
public final int num;
public final Date startTime;
public final Date endTime;
public final List<String> text;

/**
 * Creates a new instance of SRT.
 *
 * @param number the subtitle number
 * @param startTime the start time
 * @param endTime the end time
 * @param text the subtitle text
 */

public SRT(int number, Date startTime, Date endTime, String[] text) {
    this.num = number;
    this.startTime = startTime;
    this.endTime = endTime;
    this.text = new ArrayList<>(Arrays.asList(text));
}
    /**
     * Creates a new instance of SRT.
     *
     * @param numnber the subtitle number
     * @param startTime the start time
     * @param endTime the end time
     * @param text the subtitle text
     */

public SRT(int numnber, Date startTime, Date endTime, List<String> text) {
    this.num = numnber;
    this.startTime = startTime;
    this.endTime = endTime;
    this.text = new ArrayList<>(text);
}

    /**
     * {@inheritDoc}
     */
@Override
public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (num ^ (num >>> 32));
    return result;
}

    /**
     * {@inheritDoc}
     */
@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    SRT other = (SRT) obj;
    if (num != other.num)
        return false;
    return true;
}

    /**
     * {@inheritDoc}
     */
@Override
public int compareTo(SRT o) {

    return new Integer(num).compareTo(o.num);
}

@Override
public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("SRT [number=").append(num).append(", startTime=")
            .append(startTime).append(", endTime=").append(endTime).append(", text=")
            .append(text).append("]");
    return builder.toString();
}
}
