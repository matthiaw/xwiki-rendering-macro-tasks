package org.xwiki.rendering.tasks;

import javax.inject.Inject;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.sheet.SheetBinder;

import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.internal.mandatory.AbstractMandatoryDocumentInitializer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.sheet.SheetBinder;

import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.classes.BaseClass;
import com.xpn.xwiki.objects.classes.BooleanClass;

@Component
@Named(Task.CLASS + "Initializer")
@Singleton
public class TaskClassInitializer extends AbstractMandatoryDocumentInitializer {

	/**
	 * Default list separators of XWiki.TaskClass fields.
	 */
	public static final String DEFAULT_FIELDS = "|";

	/**
	 * Used to bind a class to a document sheet.
	 */
	@Inject
	@Named("class")
	private SheetBinder classSheetBinder;

	/**
	 * Used to access current XWikiContext.
	 */
	@Inject
	private Provider<XWikiContext> xcontextProvider;

	/**
	 * Overriding the abstract class' private reference.
	 */
	private DocumentReference reference;

	public TaskClassInitializer() {
		// Since we can`t get the main wiki here, this is just to be able to use
		// the Abstract class.
		super(Task.SPACE, Task.CLASS + "Class");
	}

	@Override
	public boolean updateDocument(XWikiDocument document) {
		boolean needsUpdate = false;

		// Add missing class fields
		BaseClass baseClass = document.getXClass();
		needsUpdate |= baseClass.addTextField(Task.LABEL, Task.PN_LABEL, 30);
		needsUpdate |= baseClass.addStaticListField(Task.STATUS, Task.PN_STATUS, "Open|Started|Closed");
		needsUpdate |= baseClass.addTextField(Task.ASSIGNEE, Task.PN_ASSIGNEE, 30);
		needsUpdate |= baseClass.addTextField(Task.PRIORITY, Task.PN_PRIORITY, 5);
		needsUpdate |= baseClass.addDateField(Task.CREATED, Task.PN_CREATED);
		needsUpdate |= baseClass.addDateField(Task.CHANGED, Task.PN_CHANGED);
		needsUpdate |= baseClass.addDateField(Task.DUEDATE, Task.PN_DUEDATE);
		
		// Add missing document fields
		needsUpdate |= setClassDocumentFields(document, Task.CLASS + "Class");

		// Use Sheet to display documents having Class objects if no other class
		// sheet is specified.
		if (this.classSheetBinder.getSheets(document).isEmpty()) {
			String wikiName = document.getDocumentReference()
					.getWikiReference().getName();
			DocumentReference sheet = new DocumentReference(wikiName,
					Task.SPACE, Task.CLASS + "Class" + "Sheet");
			needsUpdate |= this.classSheetBinder.bind(document, sheet);
		}

		return needsUpdate;
	}

	/**
	 * Initialize and return the main wiki's class document reference.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public EntityReference getDocumentReference() {
		if (this.reference == null) {
			synchronized (this) {
				if (this.reference == null) {
					String mainWikiName = xcontextProvider.get().getMainXWiki();
					this.reference = new DocumentReference(mainWikiName,
							Task.SPACE, Task.CLASS + "Class");
				}
			}
		}

		return this.reference;
	}

}
