package com.dilanka456.myprojectsalonapp10.Validation;


import android.os.Build;
import android.view.KeyEvent;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Dilanka Sadaruwan
 */
public class Validation {

    final static String number_digit = "0125678";

    public static boolean validateNic(String nic) {
        boolean nic_state = true;
        if (nic.length() == 10) {
            if (nic.contains("v") || nic.contains("x") || nic.contains("V") || nic.contains("X")) {
                try {
                    String number = nic.substring(0, 9);
                    Integer.parseInt(number);
                    int days = Integer.parseInt(nic.substring(2, 5));
                    if (366 < days) {
                        nic_state = days > 500;
                    }
                    if (days > 866) {
                        nic_state = false;
                    }
                } catch (Exception e) {
                    nic_state = false;
                }

            } else {
                nic_state = false;
            }
        } else {
            if (nic.length() == 12) {
                try {
                    Long.parseLong(nic);
                    int days = Integer.parseInt(nic.substring(4, 7));
                    if (366 < days) {
                        nic_state = days > 500;
                    }
                    if (days > 866) {
                        nic_state = false;
                    }
                } catch (Exception e) {
                    nic_state = false;
                }

            } else {
                nic_state = false;
            }
        }

        return nic_state;

    }

    public static boolean validateMobile(String mobile) {
        boolean mobile_state = true;
        if (mobile.length() == 10) {
            try {
                Integer.parseInt(mobile);
                char[] chara = number_digit.toCharArray();
                mobile_state = false;
                for (int i = 0; i < chara.length; i++) {
                    if (mobile.charAt(2) == chara[i]) {
                        mobile_state = true;
                    }

                }

            } catch (Exception e) {
                mobile_state = false;
            }

        } else {
            mobile_state = false;
        }
        return mobile_state;
    }

    public static boolean validateEmail(String email) {

        Pattern ptn = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = ptn.matcher(email);

        return matcher.find();
    }

    public static boolean validateSalary(String salary) {
        boolean val_sal = true;
        try {
            Double.parseDouble(salary);

        } catch (Exception e) {
            val_sal = false;
        }
        return val_sal;
    }

    public static boolean dob_state = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean validateDobFromNic(String nic, Date date) {
        String year_nic;
        int days;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        if (nic.length() == 10) {
            //year
            year_nic = "19" + nic.substring(0, 2);
            days = Integer.parseInt(nic.substring(2, 5));
        } else {

            year_nic = nic.substring(0, 4);
            days = Integer.parseInt(nic.substring(4, 7));

        }
        String year_dob = sdf.format(date);

        //month
        if (!(Integer.parseInt(year_nic) % 4 == 0) && days > 59) {

            days = days - 1;
        }
        if (days >= 500) {
            days = days - 500;
        }
        LocalDate ldate = LocalDate.ofYearDay(Integer.parseInt(year_nic), days);
        SimpleDateFormat dob = new SimpleDateFormat("yyyy-MM-dd");

        String dob_from_input = dob.format(date);
        String dob_from_nic = ldate.toString();

        if (dob_from_input.equals(dob_from_nic)) {
            dob_state = true;
        }

        return dob_state;
    }

    public static boolean validateGenderFromNic(String nic, String gender) {
        boolean gender_state = false;
        String gender_nic;

        int days;

        if (nic.length() == 10) {
            //year

            days = Integer.parseInt(nic.substring(2, 5));
        } else {

            days = Integer.parseInt(nic.substring(4, 7));

        }

        if (days > 500) {
            gender_nic = "female";
        } else {
            gender_nic = "male";
        }

        if (gender.equalsIgnoreCase(gender_nic)) {
            gender_state = true;
        }

        return gender_state;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDOBFromNIC(String nic) {
        String year_nic;
        int days;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        if (nic.length() == 10) {
            //year
            year_nic = "19" + nic.substring(0, 2);
            days = Integer.parseInt(nic.substring(2, 5));
        } else {

            year_nic = nic.substring(0, 4);
            days = Integer.parseInt(nic.substring(4, 7));

        }

        //month
        if (!(Integer.parseInt(year_nic) % 4 == 0) && days > 59) {

            days = days - 1;
        }
        if (days >= 500) {
            days = days - 500;
        }
        LocalDate ldate = LocalDate.ofYearDay(Integer.parseInt(year_nic), days);
        return ldate.toString();

    }

    public static String convertNIC(String nic) {
        String convertedNIC;
        if (nic.length() == 10) {
            nic = nic.subSequence(0, 9).toString();
            String backPt = nic.substring(5);
            String frountPt = nic.subSequence(0, 5).toString();

            convertedNIC = "19" + frountPt + "0" + backPt;

        } else {
            if (nic.substring(0).equals("2")) {
                convertedNIC = "THIS VIC CANNOT BE CONVERTED TO OLD NIC";
            } else {
                nic = nic.substring(2);
                String frountPt = nic.substring(0, 5);
                String backPt = nic.substring(6);
                convertedNIC = frountPt + backPt + "v";
            }
        }
        return convertedNIC;

    }



}

