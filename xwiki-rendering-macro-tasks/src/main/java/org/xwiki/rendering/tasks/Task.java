package org.xwiki.rendering.tasks;

import java.util.Date;

import org.xwiki.component.wiki.WikiComponent;

import com.xpn.xwiki.doc.XWikiDocument;

public interface Task extends WikiComponent {

	public static final String CLASS = "Task";
	public static final String MACRONAME = "task";
	public static final String SPACE = "XWiki";
	public static final String GUID = "guid";
	public static final String LABEL = "label";
	public static final String PN_LABEL = "Label";
	public static final String STATUS = "status";
	public static final String PN_STATUS = "Status";
	public static final String PRIORITY = "priority";
	public static final String PN_PRIORITY = "Priority";
	public static final String ASSIGNEE = "assignee";
	public static final String PN_ASSIGNEE = "Assignee";
	public static final String CREATED = "createDate";
	public static final String PN_CREATED = "Date of Creation";
	public static final String CHANGED = "changeDate";
	public static final String PN_CHANGED = "Date of Change";
	public static final String DUEDATE = "duedate";
	public static final String PN_DUEDATE = "DueDate";
	
	public float getPriority();
	
	public String getAssignee();
	
	public String getUser();
	
	public String getChangeDate(String format);
	
	public Date getChangeDate();
	
	public String getDueDate(String format);
	
	public Date getDueDate();
	
	public String getCreateDate(String format);
	
	public Date getCreateDate();
	
	public String getLabel();
	
	public String getStatus();

	public XWikiDocument getDocument();
	
}
