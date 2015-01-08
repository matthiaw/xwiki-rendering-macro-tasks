package org.xwiki.rendering.tasks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.xwiki.model.reference.EntityReference;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.MacroBlock;
import org.xwiki.rendering.block.XDOM;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;

public class TaskContainer {

	private XWikiDocument doc;
	private XWikiContext context;
	private XDOM xdom;
	private EntityReference entityReference;
	private boolean needsUpdate = false;

	public TaskContainer(XWikiDocument doc, XWikiContext context) {
		this.doc = doc;
		xdom = doc.getXDOM();
		xdom = (xdom == null) ? null : xdom.clone();
		this.context = context;
		entityReference = doc.resolveClassReference(Task.SPACE + "." + Task.CLASS + "Class");
	}

	public boolean containsObjects() {
		if (doc.getXObjects(entityReference) != null) {
			return true;
		}
		return false;
	}

	public List<MacroBlock> getMacros() {
		return getMacros(xdom);
	}

	public List<MacroBlock> getMacros(XDOM xdom) {
		List<MacroBlock> macros = new ArrayList<MacroBlock>();
		getMacros(macros, xdom.getRoot());
		return macros;
	}

	public boolean equalParameter(BaseObject o, MacroBlock m, String parameterName) {

		if (o == null) {
			return false;
		}

		if (m == null) {
			return false;
		}

		String valueObject = null;
		String valueMacro = null;

		if (parameterName.equals(Task.DUEDATE)) {
			valueObject = Util.fromDateOnly(o.getDateValue(Task.DUEDATE));
		} else {
			valueObject = o.getStringValue(parameterName);
		}
		
		if (parameterName.equals(Task.LABEL)) {
			valueMacro = m.getContent();
		} else {
			valueMacro = m.getParameter(parameterName);
		}

		if (valueObject == null) {
			valueObject = "";
		}
		if (valueMacro == null) {
			valueMacro = "";
		}

		if (!valueObject.equalsIgnoreCase(valueMacro)) {
			return false;
		}

		return true;
	}

	public boolean equalParameters(BaseObject o, MacroBlock m) {

		if (!equalGuids(o, m)) {
			return false;
		}

		if (!equalParameter(o, m, Task.LABEL)) {
			return false;
		}

		if (!equalParameter(o, m, Task.STATUS)) {
			return false;
		}

		if (!equalParameter(o, m, Task.PRIORITY)) {
			return false;
		}

		if (!equalParameter(o, m, Task.ASSIGNEE)) {
			return false;
		}
		
		if (!equalParameter(o, m, Task.DUEDATE)) {
			return false;
		}

		return true;
	}

	public boolean equalGuids(BaseObject o, MacroBlock m) {
		if (o == null) {
			return false;
		}
		if (m == null) {
			return false;
		}
		String go = getGuid(o);
		if (go == null) {
			return false;
		}
		String gm = getGuid(m);
		if (gm == null) {
			return false;
		}
		if (gm.equals(go)) {
			return true;
		}
		return false;
	}

	private String getGuid(BaseObject o) {
		return o.getGuid();
	}

	private String getGuid(MacroBlock m) {
		return m.getParameter(Task.GUID);
	}

	private BaseObject getObject(String guid) {
		if (this.containsObjects()) {
			for (BaseObject baseObject : getObjects()) {
				if (baseObject != null) {
					if (baseObject.getGuid().equals(guid)) {
						return baseObject;
					}
				}
			}
		}
		return null;
	}

	public void removeUnusedObjects() {
		List<BaseObject> unusedObjects = getUnusedObjects();
		for (BaseObject baseObject : unusedObjects) {
			doc.removeXObject(baseObject);
			needsUpdate = true;
		}
	}

	public List<BaseObject> getUnusedObjects() {
		List<BaseObject> list = new ArrayList<BaseObject>();
		if (this.containsObjects()) {
			for (BaseObject o : getObjects()) {
				if (o != null) {
					boolean found = false;
					for (MacroBlock m : getMacros()) {
						if (this.equalGuids(o, m)) {
							found = true;
						}
					}
					if (!found) {
						list.add(o);
					}
				}
			}
		}
		return list;
	}

	public List<MacroBlock> getChangedMacroObjects() {
		List<MacroBlock> list = new ArrayList<MacroBlock>();
		for (MacroBlock m : getUsedMacros()) {
			if (this.containsObjects()) {
				for (BaseObject o : getObjects()) {
					if (this.equalGuids(o, m)) {
						if (!this.equalParameters(o, m)) {
							if (!list.contains(m)) {
								list.add(m);
							}
						}
					}
				}
			}
		}
		return list;
	}

	public List<MacroBlock> getUsedMacros() {
		List<MacroBlock> list = new ArrayList<MacroBlock>();

		for (MacroBlock macro : getMacros()) {
			if (getGuid(macro) != null) {
				list.add(macro);
			}
		}

		return list;
	}

	public List<MacroBlock> getNewMacros() {
		List<MacroBlock> list = new ArrayList<MacroBlock>();

		for (MacroBlock macro : getMacros()) {
			if (getGuid(macro) == null) {
				list.add(macro);
			}
		}

		return list;
	}

	public void update() {
		if (needsUpdate) {
			try {
				doc.setContent(xdom);
				context.getWiki().saveDocument(doc, context);
				needsUpdate = false;
			} catch (XWikiException e) {
				e.printStackTrace();
			}
		}
	}

	public BaseObject getObject(int objectIndex) {
		if (!this.containsObjects()) {
			return null;
		}
		return getObjects().get(objectIndex);
	}

	private boolean containsMacro(String guid) {
		if (guid == null) {
			return false;
		}
		for (MacroBlock macro : getMacros()) {
			String mg = getGuid(macro);
			if (mg != null) {
				if (mg.equals(guid)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean containsObject(String guid) {
		if (guid == null) {
			return false;
		}
		if (this.containsObjects()) {
			for (BaseObject baseObject : getObjects()) {
				if (baseObject != null) {
					if (baseObject.getGuid().equals(guid)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public List<BaseObject> getObjects() {
		return doc.getXObjects(entityReference);
	}

	public void updateObject(MacroBlock macro) {
		BaseObject obj = getObject(getGuid(macro));
		obj.set(Task.LABEL, macro.getContent(), context);
		obj.set(Task.STATUS, macro.getParameter(Task.STATUS), context);
		obj.set(Task.ASSIGNEE, macro.getParameter(Task.ASSIGNEE), context);
		obj.set(Task.PRIORITY, macro.getParameter(Task.PRIORITY), context);
		obj.set(Task.CHANGED, Calendar.getInstance().getTime(), context);
		obj.set(Task.DUEDATE, Util.toDate(macro.getParameter(Task.DUEDATE)), context);
		needsUpdate = true;
	}

	public BaseObject createObject(MacroBlock macro) {
		try {
			int objectIndex = doc.createXObject(entityReference, context);
			BaseObject obj = getObject(objectIndex);
			macro.setParameter(Task.GUID, obj.getGuid());
			obj.set(Task.LABEL, macro.getContent(), context);
			Date createdDate = Calendar.getInstance().getTime();
			obj.set(Task.CREATED, createdDate, context);
			obj.set(Task.CHANGED, createdDate, context);
			obj.set(Task.STATUS, macro.getParameter(Task.STATUS), context);
			obj.set(Task.ASSIGNEE, macro.getParameter(Task.ASSIGNEE), context);
			obj.set(Task.PRIORITY, macro.getParameter(Task.PRIORITY), context);
			System.out.println(macro.getParameter(Task.DUEDATE)+": "+Util.toDate(macro.getParameter(Task.DUEDATE)));
			obj.set(Task.DUEDATE, Util.toDate(macro.getParameter(Task.DUEDATE)), context);
			needsUpdate = true;
			return obj;
		} catch (XWikiException e) {
		}
		return null;
	}

	private void getMacros(List<MacroBlock> list, Block block) {
		if (block instanceof MacroBlock) {
			MacroBlock mb = (MacroBlock) block;
			if (mb.getId().equals(Task.MACRONAME)) {
				list.add(mb);
			}
		}

		List<Block> children = block.getChildren();
		for (Block cBlock : children) {
			getMacros(list, cBlock);
		}

	}

}
