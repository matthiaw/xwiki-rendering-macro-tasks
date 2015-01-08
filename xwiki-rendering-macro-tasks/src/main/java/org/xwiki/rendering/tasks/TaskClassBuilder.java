package org.xwiki.rendering.tasks;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.component.wiki.WikiComponent;
import org.xwiki.component.wiki.WikiComponentBuilder;
import org.xwiki.component.wiki.WikiComponentException;
import org.xwiki.context.Execution;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.EntityReferenceSerializer;
import org.xwiki.query.Query;
import org.xwiki.query.QueryManager;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;

@Component
@Singleton
@Named("Task")
public class TaskClassBuilder implements WikiComponentBuilder {

	@Inject
	@Named("context")
	private Provider<ComponentManager> componentManagerProvider;

	@Inject
	private Execution execution;

	@Inject
	private QueryManager queryManager;

	@Inject
	private EntityReferenceSerializer<String> serializer;

	@Override
	public List<DocumentReference> getDocumentReferences() {
		List<DocumentReference> references = new ArrayList<DocumentReference>();

		try {
			Query query = queryManager.createQuery("SELECT doc.space, doc.name FROM Document doc, doc.object(" + Task.SPACE + "." + Task.CLASS + "Class) AS obj", Query.XWQL);
			List<Object[]> results = query.execute();
			for (Object[] result : results) {
				references.add(new DocumentReference(getXWikiContext().getDatabase(), (String) result[0], (String) result[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return references;
	}

	@Override
	public List<WikiComponent> buildComponents(DocumentReference reference) throws WikiComponentException {
		List<WikiComponent> components = new ArrayList<WikiComponent>();
		DocumentReference documentReference = new DocumentReference(getXWikiContext().getDatabase(), Task.SPACE, Task.CLASS + "Class");

		XWikiDocument doc = null;
		try {
			doc = getXWikiContext().getWiki().getDocument(reference, getXWikiContext());
		} catch (XWikiException e1) {
			e1.printStackTrace();
		}

		List<BaseObject> objects = doc.getXObjects(documentReference);

		if (objects != null) {
			for (final BaseObject obj : objects) {
				if (obj != null) {
					try {
						String roleHint = serializer.serialize(obj.getReference());

						DefaultTask q = new DefaultTask();
						q.setLabel(obj.getStringValue(Task.LABEL));
						q.setStatus(obj.getStringValue(Task.STATUS));
						q.setAssignee(obj.getStringValue(Task.ASSIGNEE));
						q.setPriority(obj.getStringValue(Task.PRIORITY));
						q.setCreateDate(obj.getDateValue(Task.CREATED));
						q.setChangeDate(obj.getDateValue(Task.CHANGED));
						q.setDueDate(obj.getDateValue(Task.DUEDATE));
						q.setAuthorReference(doc.getAuthorReference());
						q.setRoleHint(roleHint);
						q.setDocumentReference(reference);
						q.setDocument(doc);
						components.add(q);
					} catch (Exception e) {
						e.printStackTrace();
						throw new WikiComponentException(String.format("Failed to build " + Task.CLASS + "Class components from document [%s]", reference.toString()), e);
					}
				}
			}
		}

		return components;
	}

	private XWikiContext getXWikiContext() {
		return (XWikiContext) this.execution.getContext().getProperty("xwikicontext");
	}

}
