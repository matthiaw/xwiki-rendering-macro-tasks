/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.rendering.tasks;

import java.util.Date;

import org.xwiki.properties.annotation.PropertyHidden;
import org.xwiki.properties.annotation.PropertyMandatory;
import org.xwiki.properties.annotation.PropertyDescription;

/**
 * Parameters for the
 * {@link org.xwiki.rendering.tasks.TaskMacro.internal.ExampleMacro} Macro.
 */
public class TaskMacroParameters {
	private String status;

	private String duedate;
	
	private String guid;
	
	private String priority;

	public String getDuedate() {
		return this.duedate;
	}
	
	public String getStatus() {
		return this.status;
	}

	public String getGuid() {
		return this.guid;
	}

	public String getPriority() {
		return this.priority;
	}
	
	@PropertyDescription("Task duedate in the format yyyy-mm-dd")
	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}
	
	@PropertyDescription("Task status")
	public void setStatus(String status) {
		this.status = status;
	}

	@PropertyHidden
	@PropertyDescription("Task guid")
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	@PropertyDescription("Task priority")
	public void setPriority(String priority) {
		this.priority = priority;
	}
}
