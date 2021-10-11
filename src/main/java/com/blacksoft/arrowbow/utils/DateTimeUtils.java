package com.blacksoft.arrowbow.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author AbdelWadoud Rasmi
 * <p>
 * The goal of this class is to hold basic date formatting methods
 */
public class DateTimeUtils {

    /**
     * private constructor to prevent users from creating an instance from this class
     */
    private DateTimeUtils() {
    }

    /**
     * @return current System date formatted like this: "day-month-year"
     */
    public static final String getCurrentDateStartWithDay() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    /**
     * @return current System date formatted like this: "year-month-day"
     */
    public static final String getCurrentDateStartWithYear() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * @param languageCode: 2 chars code for example(Arabic:ar, English: en)
     * @return current System date formatted using formatDate method
     */
    public static final String getCurrentFormattedDate(@NonNull String languageCode) {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    /**
     * @return current System time formatted like this "HH:mm:ss"
     */
    public static final String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    /**
     * @return current System time formatted like this "dd-MM-yyyy HH:mm:ss"
     */
    public static final String getCurrentDateTimeStartWithDay() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    /**
     * @return current System time formatted like this "yyyy-MM-dd HH:mm:ss"
     */
    public static final String getCurrentDateTimeStartWithYear() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }


    /**
     * formats date (only date) in Arabic,English and French for example:
     * "2022-01-22" or "22-01-2022" gives:
     * {
     * en: 22 January 2022
     * fr: 22 Janvier 2022
     * }
     *
     * @param languageCode: 2 chars code for example(Arabic:ar, English: en)
     * @param date:         example: "2002-10-12" or "12-10-2002"
     */
    public static final String formatDate(@NonNull String languageCode,
                                          @Nullable String date) {

        String appLang = languageCode.toLowerCase();
        if (date == null || date.isEmpty()) return null;
        String formattedDate = date;
        String[] dateArr = date.split("-");

        if (appLang.equals("ar")) {

            switch (Integer.valueOf(dateArr[1])) {
                case 1:
                    dateArr[1] = " جانفي ";
                    break;

                case 2:
                    dateArr[1] = " فيفري ";
                    break;

                case 3:
                    dateArr[1] = " مارس ";
                    break;

                case 4:
                    dateArr[1] = " أبريل ";
                    break;

                case 5:
                    dateArr[1] = " ماي ";
                    break;

                case 6:
                    dateArr[1] = " جوان ";
                    break;

                case 7:
                    dateArr[1] = " جويلية ";
                    break;

                case 8:
                    dateArr[1] = " أوت ";
                    break;

                case 9:
                    dateArr[1] = " سبتمبر ";
                    break;

                case 10:
                    dateArr[1] = " أكتوبر ";
                    break;

                case 11:
                    dateArr[1] = " نوفمبر ";
                    break;

                case 12:
                    dateArr[1] = " ديسمبر ";
                    break;

            }
            return (dateArr[0].length() == 4 ? dateArr[2] : dateArr[0]) + dateArr[1] + (dateArr[0].length() == 4 ? dateArr[0] : dateArr[2]);

        } else if (appLang.equals("fr")) {

            switch (Integer.valueOf(dateArr[1])) {
                case 1:
                    dateArr[1] = " Janvier ";
                    break;

                case 2:
                    dateArr[1] = " Février ";
                    break;

                case 3:
                    dateArr[1] = " Mars ";
                    break;

                case 4:
                    dateArr[1] = " Avril ";
                    break;

                case 5:
                    dateArr[1] = " Mai ";
                    break;

                case 6:
                    dateArr[1] = " Juin ";
                    break;

                case 7:
                    dateArr[1] = " Juillet ";
                    break;

                case 8:
                    dateArr[1] = " Aout ";
                    break;

                case 9:
                    dateArr[1] = " Septembre ";
                    break;

                case 10:
                    dateArr[1] = " Octobre ";
                    break;

                case 11:
                    dateArr[1] = " Novembre ";
                    break;

                case 12:
                    dateArr[1] = " Décembre ";
                    break;
            }
        } else {

            switch (Integer.valueOf(dateArr[1])) {
                case 1:
                    dateArr[1] = " January ";
                    break;

                case 2:
                    dateArr[1] = " February ";
                    break;

                case 3:
                    dateArr[1] = " March ";
                    break;

                case 4:
                    dateArr[1] = " April ";
                    break;

                case 5:
                    dateArr[1] = " May ";
                    break;

                case 6:
                    dateArr[1] = " June ";
                    break;

                case 7:
                    dateArr[1] = " July ";
                    break;

                case 8:
                    dateArr[1] = " August ";
                    break;

                case 9:
                    dateArr[1] = " September ";
                    break;

                case 10:
                    dateArr[1] = " October ";
                    break;

                case 11:
                    dateArr[1] = " November ";
                    break;

                case 12:
                    dateArr[1] = " December ";
                    break;

            }

        }
        return (dateArr[0].length() == 4 ? dateArr[2] : dateArr[0]) + dateArr[1] + (dateArr[0].length() == 4 ? dateArr[0] : dateArr[2]);
    }

}
