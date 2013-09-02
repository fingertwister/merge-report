/**
 *
 */
package au.net.thehardings.ims.mergereport;

import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class TestDateFactory {

    private static final TestDateFactory instance = new TestDateFactory();

    public static TestDateFactory getInstance() {
        return instance;
    }

    /**
     * Gets a date initialised to midnight on the given day
     *
     * @param year The year
     * @param month The month (0 is January)
     * @param day The day of the month
     * @return The correct object for the parameters provided.
     */
    public java.sql.Date getSqlDate(int year, int month, int day) {
        return new java.sql.Date(getDate(year, month, day).getTime());
    }

    /**
     * Gets a date initialised to the given date and time
     *
     * @param year The year
     * @param month The month (0 is January)
     * @param day The day of the month
     * @param hour The hour
     * @param minute The number of minutes
     * @param secs The number of seconds
     * @param millis The number of milliseconds
     * @return The correct object for the parameters provided.
     */
    public java.sql.Date getSqlDateTime(int year, int month, int day, int hour, int minute, int secs, int millis) {

        return new java.sql.Date(getCalendar(year, month, day, hour, minute, secs, millis).getTime().getTime());

    }

    /**
     * Gets a date initialised to midnight on the given day
     *
     * @param year The year
     * @param month The month (0 is January)
     * @param day The day of the month
     * @return The correct object for the parameters provided.
     */
    public Date getDate(int year, int month, int day) {
        return getDateTime(year, month, day, 0, 0, 0, 0);
    }

    /**
     * Gets a date initialised to the given date and time
     *
     * @param year The year
     * @param month The month (0 is January)
     * @param day The day of the month
     * @param hour The hour
     * @param minute The number of minutes
     * @param secs The number of seconds
     * @param millis The number of milliseconds
     * @return The correct object for the parameters provided.
     */
    public Date getDateTime(int year, int month, int day, int hour, int minute, int secs, int millis) {

        return getCalendar(year, month, day, hour, minute, secs, millis).getTime();

    }

    /**
     * Gets a Calendar initialised to midnight on the given day
     *
     * @param year The year
     * @param month The month (0 is January)
     * @param day The day of the month
     * @return The correct object for the parameters provided.
     */
    public Calendar getCalendar(int year, int month, int day) {
        return getCalendar(year, month, day, 0, 0, 0, 0);
    }

    /**
     * Gets a date initialised to the given date and time
     *
     * @param year The year
     * @param month The month (0 is January)
     * @param day The day of the month
     * @param hour The hour
     * @param minute The number of minutes
     * @param secs The number of seconds
     * @param millis The number of milliseconds
     * @return The correct object for the parameters provided.
     */
    public Calendar getCalendar(int year, int month, int day, int hour, int minute, int secs, int millis) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, secs);
        cal.set(Calendar.MILLISECOND, millis);
        return cal;

    }

    /**
     * Gets the current date / time
     *
     * @return Date
     */
    public Date now() {
        return nowCalendar().getTime();
    }

    /**
     * Gets the current date / time
     *
     * @return Date
     */
    public Calendar nowCalendar() {
        return Calendar.getInstance();
    }

    /**
     * Gets the current date
     *
     * @return Date
     */
    public Date today() {
        return todayCalendar().getTime();
    }

    /**
     * Gets the current date
     *
     * @return Date
     */
    public Calendar todayCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

}
