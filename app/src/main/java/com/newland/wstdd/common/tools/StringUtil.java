package com.newland.wstdd.common.tools;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.newland.wstdd.common.common.AppContext;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.widget.EditText;

public class StringUtil {

    public static String DAY_TIME = "yyyy-MM-dd";
    public static String MINUTE_TIME = "yyyy-MM-dd HH:mm";// 分钟
    public static String MONTH_TIME = "yyyy-MM";
    public static String YEAR_TIME = "yyyy";
    public static String SECOND_TIME = "yyyy-MM-dd HH:mm:ss";

    // public static int WEEK_TIME=java.text.DateFormat.WEEK_OF_MONTH_FIELD;

    /**
     * MD5加密验证
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static String noNull(Object str) {
        if (str == null || str.equals("") || "null".equals(str))
            return "";
        else
            return str.toString();
    }

    /**
     * 如果源串为空或NULL，返回默认值
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static String noNull(Object str, String defaultValue) {
        if (str == null || str.equals("") || "null".equals(str))
            return defaultValue;
        else
            return str.toString();
    }

    public static boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0) || str.equals("");
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 身份证号验证
     *
     * @param sfzh
     * @return true验证通过 false不通过
     */
    public static boolean isChinaIDCard(String sfzh) {
        try {
            int flag = 1;
            // 如果长度不是15和18，则返回错误
            if (sfzh.length() != 18 && sfzh.length() != 15) {
                flag = flag * 0;
            }
            if (sfzh.length() == 18) {
                String start = sfzh.substring(0, 6);
                int year = Integer.parseInt(sfzh.substring(6, 10));
                int month = Integer.parseInt(sfzh.substring(10, 12));
                int day = Integer.parseInt(sfzh.substring(12, 14));
                int nowYear = (new Date()).getYear() + 1900;
                Log.v("sfzh year is ", year + "");
                Log.v("sfzh month is ", month + "");
                Log.v("sfzh day is ", day + "");
                Log.v("now year is ", nowYear + "");
                if (year < 1900 || year > nowYear) {
                    flag = flag * 0;
                }
                if (month < 1 || month > 12) {
                    flag = flag * 0;
                }
                if (day < 1 || day > 31) {
                    flag = flag * 0;
                }
                // 判断验证位
                String check = "";
                int a = Integer.parseInt(sfzh.substring(0, 1)) * 7 + Integer.parseInt(sfzh.substring(1, 2)) * 9 + Integer.parseInt(sfzh.substring(2, 3)) * 10 + Integer.parseInt(sfzh.substring(3, 4))
                        * 5 + Integer.parseInt(sfzh.substring(4, 5)) * 8 + Integer.parseInt(sfzh.substring(5, 6)) * 4 + Integer.parseInt(sfzh.substring(6, 7)) * 2
                        + Integer.parseInt(sfzh.substring(7, 8)) * 1 + Integer.parseInt(sfzh.substring(8, 9)) * 6 + Integer.parseInt(sfzh.substring(9, 10)) * 3
                        + Integer.parseInt(sfzh.substring(10, 11)) * 7 + Integer.parseInt(sfzh.substring(11, 12)) * 9 + Integer.parseInt(sfzh.substring(12, 13)) * 10
                        + Integer.parseInt(sfzh.substring(13, 14)) * 5 + Integer.parseInt(sfzh.substring(14, 15)) * 8 + Integer.parseInt(sfzh.substring(15, 16)) * 4
                        + Integer.parseInt(sfzh.substring(16, 17)) * 2;
                int b = a % 11;
                switch (b) {
                    case 0:
                        check = "1";
                        break;
                    case 1:
                        check = "0";
                        break;
                    case 2:
                        check = "X";
                        break;
                    case 3:
                        check = "9";
                        break;
                    case 4:
                        check = "8";
                        break;
                    case 5:
                        check = "7";
                        break;
                    case 6:
                        check = "6";
                        break;
                    case 7:
                        check = "5";
                        break;
                    case 8:
                        check = "4";
                        break;
                    case 9:
                        check = "3";
                        break;
                    case 10:
                        check = "2";
                        break;
                    default:
                        break;
                }
                Log.v("check num is ", check);
                if (!check.equals(sfzh.subSequence(17, 18))) {
                    flag = flag * 0;
                }
            }
            Log.v("flag", flag + "");
            if (flag == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 获取相对URL地址
     *
     * @param split 切割符
     * @param url
     * @return
     */
    public static final String splitSubUrl(String split, String url) {
        try {
            if (StringUtil.isNotEmpty(url)) {
                int pos = url.indexOf(split);
                if (pos <= -1) {
                    return "";
                } else {
                    String temp = url.substring(pos);
                    return temp;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * @param str
     * @return 设定文件
     * @Title: clearSpaces
     * @Description: 清除字符串中的空格字符
     */
    public static final String clearSpaces(String str) {
        StringTokenizer st = new StringTokenizer(str, " ", false);
        String t = "";
        while (st.hasMoreElements()) {
            t += st.nextElement();
        }
        return t;
    }

    /**
     * @param str
     * @return 设定文件
     * @Title: formatStr
     * @Description:如果为null,返回"" , 如果不是却掉前后空格
     */
    public static final String formatStr(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    /**
     * 取得指定参数的值
     *
     * @param key
     * @param Keys
     * @param Values
     * @return
     */
    public static String getKeyValue(String key, ArrayList<String> Keys, ArrayList<String> Values) {
        int len = Keys.size();
        for (int i = 0; i < len; i++) {
            if (Keys.get(i).equalsIgnoreCase(key))
                return Values.get(i);
        }
        return "";
    }

    /**
     * 去掉空格，回车符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {

        String dest = "";

        if (str != null) {

            Pattern p = Pattern.compile("\\s*|\t|\r|\n");

            Matcher m = p.matcher(str);

            dest = m.replaceAll("");

        }

        return dest;

    }

    /**
     * 判断是否是身份证
     */
    public static boolean isPid(String str) {

        if (isEmpty(str)) {
            return false;
        }
        String[] arg = {str};

        // 17位加权因子，与身份证号前17位依次相乘。

        int w[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        int sum = 0;// 保存级数和

        try {
            for (int i = 0; i < arg[0].length() - 1; i++) {
                sum += new Integer(arg[0].substring(i, i + 1)) * w[i];
            }
        } catch (Exception e) {
            return false;

        }

        /**
         * 校验结果，上一步计算得出的结果与11取模，得到的结果相对应的字符就是身份证最后一位 ，也就是校验位。例如：0对应下面数组第一个元素，
         * 以此类推。
         */

        String sums[] = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        if (sums[(sum % 11)].equals(arg[0].substring(arg[0].length() - 1, arg[0].length()))) {// 与身份证最后一位比较
            return true;

        } else {
            return false;
        }

    }

    /**
     * 检查Edit是否是空的 isEditTextIsEmpty(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
     *
     * @param editText
     * @return boolean
     * @throws
     * @since 1.0.0
     */
    public static boolean isEditTextIsEmpty(EditText editText) {
        String str = editText.getText().toString().trim();
        if ("".equals(str)) {
            return true;
        }
        return false;

    }

    /**
     * 获取当前时间
     *
     * @param type
     * @return
     */
    public static String getNowTime(String type) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(type);// 可以方便地修改日期格式
        return dateFormat.format(now);
    }

    /**
     * 传入时间跟时间格式返回long型时间
     *
     * @param time
     * @param type
     * @return
     */
    public static long getLongTime(String time, String type) {
        long timeL = 0;
        try {
            timeL = new SimpleDateFormat(type).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeL;
    }

    /**
     * 传入long型返回对应格式的时间
     */
    public static String long2FormatTime(long timeL, String type) {
        String formatTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date dt = new Date(timeL);
        formatTime = sdf.format(dt);
        return formatTime;
    }

    /**
     * 传入时间（格式为HH:mm）返回long型时间
     *
     * @param time
     * @return
     */
    public static long getLongTime(String time) {
        long longTime = 0;
        String nowDate = new SimpleDateFormat(SECOND_TIME).format(new Date());
        nowDate = nowDate.substring(0, 10);
        time = nowDate + " " + time + ":00";
        try {
            longTime = new SimpleDateFormat(SECOND_TIME).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return longTime;
    }

    /**
     * 获取昨天时间
     *
     * @return
     */
    public static String getLastTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat(DAY_TIME).format(cal.getTime());
        return yesterday;
    }

    /**
     * 获取上月
     *
     * @return
     */
    public static String getLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        String day = new SimpleDateFormat(MONTH_TIME).format(cal.getTime());
        return day;
    }

    /**
     * 获取当年
     *
     * @return
     */
    public static String getLastYear() {
        Calendar cal = Calendar.getInstance();
        // cal.add(Calendar.YEAR, 0);//获取上一年
        cal.add(Calendar.YEAR, 0);
        String day = new SimpleDateFormat(YEAR_TIME).format(cal.getTime());
        return day;
    }

    public static String formateNum(String date) {
        if ("1".equals(date)) {
            date = "01";
            return date;
        } else if ("2".equals(date)) {
            date = "02";
            return date;
        } else if ("3".equals(date)) {
            date = "03";
            return date;
        } else if ("4".equals(date)) {
            date = "04";
            return date;
        } else if ("5".equals(date)) {
            date = "05";
            return date;
        } else if ("6".equals(date)) {
            date = "06";
            return date;
        } else if ("7".equals(date)) {
            date = "07";
            return date;
        } else if ("8".equals(date)) {
            date = "08";
            return date;
        } else if ("9".equals(date)) {
            date = "09";
            return date;
        } else {
            return date;
        }
    }

    /**
     * 活动分类 数字转为相应汉字:0.高级 1.讲座 2.通知 3.会议 4.运动 5.招聘 6.旅游 7.众筹 8.赛事 9.聚会 10.投票
     * 31.高级快捷活动 32.一句话 33.一张图片 34.一段语音 50.团购 99.微店',
     *
     * @param str
     * @return
     */
    public static String intType2Str(int value) {
        String finalStr = "讲座";
        switch (value) {
            case 0:
                finalStr = "高级";
                break;
            case 1:
                finalStr = "讲座";
                break;
            case 2:
                finalStr = "通知";
                break;
            case 3:
                finalStr = "会议";
                break;
            case 4:
                finalStr = "运动";
                break;
            case 5:
                finalStr = "招聘";
                break;
            case 6:
                finalStr = "旅游";
                break;
            case 7:
                finalStr = "众筹";
                break;
            case 8:
                finalStr = "赛事";
                break;
            case 9:
                finalStr = "聚会";
                break;
            case 10:
                finalStr = "投票";
                break;
            case 31:
                finalStr = "高级快捷活动";
                break;
            case 32:
                finalStr = "一句话";
                break;
            case 33:
                finalStr = "一张图片";
                break;
            case 34:
                finalStr = "一段语音";
                break;
            case 50:
                finalStr = "团购";
                break;
            case 99:
                finalStr = "微店";
                break;
            default:
                break;
        }
        return finalStr;
    }

    /**
     * 汉字转对应英文
     *
     * @param mess
     * @return
     */
    public static String mess2Str(String mess) {
        String finalStr = "";
        if ("讲座".equals(mess)) {
            finalStr = "chair";
        } else if ("团购".equals(mess)) {
            finalStr = "groupbuying";
        } else if ("招聘".equals(mess)) {
            finalStr = "invite";
        } else if ("聚会".equals(mess)) {
            finalStr = "meeting";
        } else if ("旅行".equals(mess)) {
            finalStr = "travel";
        } else if ("众筹".equals(mess)) {
            finalStr = "crowdfunding";
        } else if ("投票".equals(mess)) {
            finalStr = "vote";
        } else if ("全部".equals(mess)) {
            finalStr = "all";
        }
        return finalStr;
    }

    /**
     * 根据uri得到图片的路径
     */
    // 根据Uri获取到图片的路径
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 去除List<String> 中重复的值
     */
    public static List<String> getNewList(List<String> li) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < li.size(); i++) {
            String str = li.get(i); // 获取传入集合对象的每一个元素
            if (!list.contains(str)) { // 查看新集合中是否有指定的元素，如果没有则加入
                list.add(str);
            }
        }
        return list; // 返回集合
    }

    /**
     * int转性别 Int（1是男性，2是女性，0是未知）',
     */
    public static String getSex(int sexInt) {
        String sexString = null;
        if (sexInt == 1) {
            sexString = "男";
        } else if (sexInt == 2) {
            sexString = "女";
        } else {
            sexString = "未知";
        }
        return sexString;
    }

    /**
     * 性别转int Int（1是男性，2是女性，0是未知）',
     */
    public static int getIntSex(String string) {
        int sexInt = 0;
        if ("男".equals(string)) {
            sexInt = 1;
        } else if ("女".equals(string)) {
            sexInt = 2;
        } else if ("未知".equals(string)) {
            sexInt = 0;
        }
        return sexInt;
    }

    /**
     * 限制手机的格式
     */

    public static void limitPatternPhone(EditText editText) {
        editText.setKeyListener(new NumberKeyListener() {
            protected char[] getAcceptedChars() {
                return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
            }

            public int getInputType() {
                return android.text.InputType.TYPE_CLASS_PHONE;
            }
        });


    }

    //保存我的标签   将一个list集合编程以逗号隔开的字符串  保存到sharepreference
    public static void appContextTagsListToString(List<String> list) {
        String myTags = "";
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.size() - 1 == i) {
                    myTags += list.get(i);
                } else {
                    myTags += list.get(i) + ",";
                }
            }
        }
        AppContext.getAppContext().setMyTags(myTags);
    }

    //将字符串    转换     成list
    public static List<String> appContextTagsStringToList(String myTags) {
        String[] arr = myTags.split(",");
        List<String> list = Arrays.asList(arr);
        return list;
    }
}
