package com.aioveu.utils;

import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.entity.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * 日期util
 */
public class DataUtil {
	/**
	 * 获取今天
	 * @return String
	 * */
	public static String getToday(){
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 获取今天
	 * @return String
	 * */
	public static String getToday(LocalDateTime time){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return dtf.format(time);
	}

	/**

	 * 获取指定日期下个月的第一天
	 * @param dateStr
	 * @return
	 */
	public static String getFirstDayOfNextMonth(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH,1);
			calendar.add(Calendar.MONTH, 1);
			return sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 时间转换
	 * @return String
	 * */
	public static String getDayByData(Date date){
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	/**
	 * 时间转换
	 * @return String
	 * */
	public static Date getDayByString(String date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	}

	/**
	 * 时间转换
	 * @return String
	 * */
	public static String getDayByString(Date date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}


	/**
	 * 时间转换
	 * @return String
	 * */
	public static String getDayByDataHHMM(Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}


	public static Date getDayDataByDataHHMM(String  date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
	}

	/**
	 * 获取今天
	 * @return String
	 * */
	public static String getTodayHms(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	/**
	 * 获取昨天
	 * @return String
	 * */
	public static String getYestoday(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}





	/**
	 * 获取明天
	 * @return String
	 * */
	public static String getTomorrowDay(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,1);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}


	/**
	 * 获取明天
	 * @return String
	 * */
	public static String getTomorrowDay(String data) throws ParseException {
		SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		Date parse = simpleDateFormat.parse(data);
		cal.setTime(parse);
		cal.add(Calendar.DATE,1);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}




	/**
	 * 时间相加
	 * @return String
	 * */
	public static Date getDataAdd(Date times,Integer addTime){
		Calendar cal=Calendar.getInstance();
		cal.setTime(times);
		cal.add(Calendar.DATE,addTime);
		Date time=cal.getTime();
		return time;
	}


	/**
	 * 获取两个日期之间的所有日期
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return 所有日期
	 */
	public static List<String> splitDateList(String startDate, String endDate) {
		List<String> listDate = new ArrayList<>();
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getDayByString(startDate));
			while (calendar.getTime().before(getDayByString(endDate)) || calendar.getTime().equals(getDayByString(endDate))) {
				listDate.add(getDayByData(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			return listDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listDate;
	}





	/**
	 * 获取n天
	 * @return String
	 * */
	public static Date getTomorrowDayByNumber(Integer number){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,number);
		Date time=cal.getTime();
		return time;
	}


	/**
	 * 获取过去7天
	 * @return String
	 * */
	public static List<String> getTomorrowDayGq() throws Throwable {
		List<String> list = new ArrayList<>();
		Date time = new Date();
		int index = 7;
		for (int i =0; i<index ;i++){
			Calendar cal=Calendar.getInstance();
			cal.setTime(time);
			String day=	new SimpleDateFormat("yyyy-MM-dd").format(time);
			cal.add(Calendar.DATE,-1);
			time=cal.getTime();
			list.add(day);
		}
		return list;
	}


	/**
	 * 获取过后7天
	 * @return String
	 * */
	public static List<Map<String,Object>> getTomorrowDaySeven() throws Throwable {
		List<Map<String,Object>> list = new ArrayList<>();
		 Date time = new Date();
		 int index = 7;
		for (int i =0; i<index ;i++){
			Calendar cal=Calendar.getInstance();
			cal.setTime(time);
			String day=	new SimpleDateFormat("yyyy-MM-dd").format(time);
			String week = dayForWeek(day);
			Map<String,Object> map = new HashMap<>();
			cal.add(Calendar.DATE,1);
			time=cal.getTime();
			if (week.equals("3")){
				index = index+1;
				continue;
			}

			map.put("day",day);
			map.put("week",getWeekBySize(Integer.parseInt(week)));

			list.add(map);
		}
		return list;
	}

	/**
	 * 获取过后7天
	 * @return String
	 * */
	public static List<Map<String,Object>> getTomorrowDaySevenByqb() throws Throwable {
		List<Map<String,Object>> list = new ArrayList<>();
		Date time = new Date();
		int index = 7;
		for (int i =0; i<index ;i++){
			Calendar cal=Calendar.getInstance();
			cal.setTime(time);
			String day=	new SimpleDateFormat("yyyy-MM-dd").format(time);
			String week = dayForWeek(day);
			Map<String,Object> map = new HashMap<>();
			cal.add(Calendar.DATE,1);
			time=cal.getTime();

			map.put("day",day);
			map.put("week",getWeekBySize(Integer.parseInt(week)));
			list.add(map);
		}
		return list;
	}


	/**
	 * 获取今天- n
	 * @return String
	 * */
	public static String getYestodayByN(Integer n) throws ParseException {
		SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		Date parse = new Date();
		long time1 = parse.getTime();
		cal.setTime(parse);
		cal.add(Calendar.DATE,-n);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}




	/**
	 * 获取今天-1
	 * @return String
	 * */
	public static String getYestoday(String data) throws ParseException {
		SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		Date parse = simpleDateFormat.parse(data);
		long time1 = parse.getTime();
		cal.setTime(parse);
		cal.add(Calendar.DATE,-1);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}

	/**
	 * 获取本月开始日期
	 * @return String
	 * **/
	public static String getMonthStart(String dataTime) throws ParseException {
		Calendar cal=Calendar.getInstance();
		cal.setTime(getDayByString(dataTime));
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	/**
	 * 获取本月最后一天
	 * @return String
	 * **/
	public static String getMonthEnd(String dataTime) throws ParseException {
		Calendar cal=Calendar.getInstance();
		cal.setTime(getDayByString(dataTime));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}

	/**
	 * 获取连个日期之间相差的月份
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getMonth(String startDate, String endDate) throws ParseException {
		List list = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(sdf.parse(startDate));
		c2.setTime(sdf.parse(endDate));
		int  year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		int month = c2.get(Calendar.MONTH)+year*12 - c1.get(Calendar.MONTH);
		for(int i = 0;i<month;i++){
			c1.setTime(sdf.parse(startDate));
			c1.add(c1.MONTH, i);
			list.add(sdf.format(c1.getTime()));
		}
		return list;
	}

	/**
	 * 获取上个月
	 * @return
	 */
	public static String getLastMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); // 设置为当前时间
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
		date = calendar.getTime();
		String accDate = format.format(date);
		return accDate;
	}

	/**
	 * 获取本月开始日期
	 * @return String
	 * **/
	public static String getMonthStart(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	/**
	 * 获取本月最后一天
	 * @return String
	 * **/
	public static String getMonthEnd(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	/**
	 * 获取本周的第一天
	 * @return String
	 * **/
	public static String getWeekStart(){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_MONTH, 0);
		cal.set(Calendar.DAY_OF_WEEK, 2);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	/**
	 * 获取本周的最后一天
	 * @return String
	 * **/
	public static String getWeekEnd(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
		cal.add(Calendar.DAY_OF_WEEK, 1);
		Date time=cal.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	/**
	 * 获取本年的第一天
	 * @return String
	 * **/
	public static String getYearStart(){
		return new SimpleDateFormat("yyyy").format(new Date())+"-01-01";
	}

	/**
	 * 获取本年的最后一天
	 * @return String
	 * **/
	public static String getYearEnd(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH,calendar.getActualMaximum(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date currYearLast = calendar.getTime();
		return new SimpleDateFormat("yyyy-MM-dd").format(currYearLast);
	}

	/**
	 * 转换大小写
	 * @week 小写星期
	 * @return
	 * @throws Throwable
	 */
	public static String getWeekBySize(Integer week) throws Throwable {
		String[] weekDays = { "一", "二", "三", "四", "五", "六","日" };

		return weekDays[week-1];
	}

	/**
	 * 根据日期判断今天是周几
	 * @param pTime 日期
	 * @return 周几
	 * @throws Throwable
	 */
	public static String dayForWeek(String pTime) throws Throwable {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date tmpDate = format.parse(pTime);

		Calendar cal = Calendar.getInstance();

		String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };

		try {

			cal.setTime(tmpDate);

		} catch (Exception e) {

			e.printStackTrace();

		}

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。

		if (w < 0)

			w = 0;
		return weekDays[w];

	}

	/**
	 * 判断两个时间谁大
	 * @param data  大的一方
	 * @param endData  小的一方
	 * @return 大的一方赢 则为true  小的为false
	 */
	public static  boolean getDataDx(String data,String endData){
		// 比较 年 月 日
		SimpleDateFormat sdf  =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //创建日期转换对象：年 月 日

		try {
			Date dateD = sdf.parse(data); //将字符串转换为 date 类型  Debug：Sun Nov 11 00:00:00 CST 2018
			Date endDate = sdf.parse(endData); //将字符串转换为 date 类型  Debug：Sun Nov 11 00:00:00 CST 2018
			boolean flag = dateD.getTime() < endDate.getTime();
			System.err.println("flag = "+flag);  // flag = false
			return flag;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return  false;
	}



	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * @param starttime 时间参数 1 格式：1990-01-01 12:00:00
	 * @param endtime 时间参数 2 格式：2009-01-01 12:00:00
	 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static long[] getDistanceTimes(String starttime, String endtime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			one = df.parse(starttime);
			two = df.parse(endtime);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff ;
			if(time1<time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long[] times = {day, hour, min, sec};
		return times;
	}

	/**
	 * user对象转LoginVal
	 * @param user
	 * @return
	 */
	public static LoginVal getByUser(User user, List<String> roles) {
		LoginVal currentUser = new LoginVal();
		currentUser.setNickname(user.getName());
		currentUser.setUserId(user.getId());
		currentUser.setGender(user.getGender());
		currentUser.setUsername(user.getUsername());
		currentUser.setHead(user.getHead());
		currentUser.setPhone(user.getPhone());
		currentUser.setEmail(user.getMail());
		currentUser.setAuthorities(roles.toArray(new String[0]));
		return currentUser;
	}


	public static void main(String[] args) {
		List<String> roleList = Arrays.asList("12admin", "user");
		if (roleList.stream().noneMatch(s -> s.startsWith("admin"))) {
			System.out.println("非管理员");
		} else {
			System.out.println("管理员");
		}


	}


}
