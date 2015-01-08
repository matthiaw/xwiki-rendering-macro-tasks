package org.xwiki.rendering.tasks;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Util {

	public static String fromDateOnly(Date date) {

		if (date == null) {
			return null;
		}

		return fromDate(date).substring(0, 10);
	}

	public static String fromDate(Date d) {
		try {

			if (d == null) {
				return null;
			}

			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(d);

			// Create a data type factory
			DatatypeFactory df = DatatypeFactory.newInstance();

			// Create a purchase date of a product (xs:dateTime)
			XMLGregorianCalendar calendar = df.newXMLGregorianCalendar(gregorianCalendar);

			return calendar.toXMLFormat();

		} catch (DatatypeConfigurationException e) {
			return null;
		}

	}

	public static Date toDate(String s) {

		if (s == null) {
			return null;
		}

		try {

			// Create a data type factory
			DatatypeFactory df = DatatypeFactory.newInstance();

			// Create a date of xs:dateTime
			XMLGregorianCalendar calendar = df.newXMLGregorianCalendar(s);

			return calendar.toGregorianCalendar().getTime();

		} catch (DatatypeConfigurationException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}

	}

}
