package org.xwiki.rendering.tasks;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xwiki.component.wiki.WikiComponentScope;
import org.xwiki.model.reference.DocumentReference;

import com.xpn.xwiki.doc.XWikiDocument;

public class DefaultTask implements Task {

	private String roleHint;
	private XWikiDocument xwikiDocument;
	private DocumentReference documentReference;
	private DocumentReference authorReference;

	public XWikiDocument getDocument() {
		return xwikiDocument;
	}

	public void setDocument(XWikiDocument xwikiDocument) {
		this.xwikiDocument = xwikiDocument;
	}

	public DocumentReference getDocumentReference() {
		return documentReference;
	}

	public DocumentReference getAuthorReference() {
		return authorReference;
	}

	public void setRoleHint(String roleHint) {
		this.roleHint = roleHint;
	}

	public void setDocumentReference(DocumentReference documentReference) {
		this.documentReference = documentReference;
	}

	public void setAuthorReference(DocumentReference authorReference) {
		this.authorReference = authorReference;
	}

	public String getRoleHint() {
		return roleHint;
	}

	public WikiComponentScope getScope() {
		return WikiComponentScope.WIKI;
	}

	private String label;

	private String status;

	private float priority;

	private Date created;

	private Date dueDate;

	private Date changed;

	private String assignee;

	public DefaultTask() {
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getDueDate(String format) {
		if (dueDate == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat(format);
		return df.format(dueDate);
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getChangeDate(String format) {
		if (changed == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat(format);
		return df.format(changed);
	}

	public Date getChangeDate() {
		return changed;
	}

	public String getUser() {

		if (this.assignee != null) {
			if (!this.assignee.equals("")) {
				if (this.assignee.startsWith("XWiki.")) {
					return "[["+this.assignee+"]]";
				} else {
					return "[[XWiki."+this.assignee+"]]";
				}
			}
		}

		return "";
	}

	public void setChangeDate(Date changed) {
		this.changed = changed;
	}

	public String getCreateDate(String format) {
		if (created == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat(format);
		return df.format(created);
	}

	public Date getCreateDate() {
		return created;
	}

	public void setCreateDate(Date created) {
		this.created = created;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public float getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		try {
			this.priority = Float.parseFloat(priority.replace(",", "."));
		} catch (Exception e) {

		}
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public Type getRoleType() {
		return Task.class;
	}

}
