package ampify_player;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class stores collections of SRT objects.
 *
 * All the methods here perform low-level operations on SRTInfo object.
 *
 * @author Sayed Zameer Qasim
 */

public class SRTInfo {
    public ArrayList<SRT> info;

    /**
     * Creates a new instance of SRTInfo.
     */
    public SRTInfo() {

        info = new ArrayList<>();
    }

    /**
     * Creates a new instance of SRTInfo.
     * This constructor acts as a copy constructor.
     *
     * @param srtInfo the SRTInfo object
     */
    public SRTInfo(SRTInfo srtInfo) {
        info = new ArrayList<>(srtInfo.info);
    }

    /**
     * Adds SRT object into SRTInfo object. If SRT object already exists, the old
     * SRT object will be replaced with the new SRT object.
     *
     * @param srt the SRT object to be added
     */
    public void add(SRT srt) {
        remove(srt);
        info.add(srt);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<SRT> iterator() {

        return info.iterator();
    }

    /**
     * Gets the number of SRT objects stored in SRTInfo object.
     *
     * @return the number of SRT objects stored in SRTInfo object
     */
    public int size() {

        return info.size();
    }

    /**
     * Removes the SRT object from SRTInfo.
     *
     * @param srt the SRT object to be removed from SRTInfo
     */
    public void remove(SRT srt) {
        // Set.remove() will check if the object is present in the Set, so
        // there is no need to do another check if the object is present in
        // the set
        info.remove(srt);
    }

    /**
     * Check if the subtitle number is in the SRTInfo object.
     *
     * @param number the subtitle number
     * @return true if the subtitle number is in the SRTInfo; false otherwise
     */
    public boolean contains(int number) {

        return info.contains(new SRT(number, null, null, new String[]{}));
    }

    /**
     * Check if the SRT is in the SRTInfo object.
     *
     * @param srt the SRT object
     * @return true if the subtitle number is in the SRTInfo; false otherwise
     */
    public boolean contains(SRT srt) {

        return info.contains(srt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {

        return new SRTInfo(this);
    }
}
