package org.xwiki.rendering.tasks;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.xwiki.bridge.event.DocumentCreatedEvent;
import org.xwiki.bridge.event.DocumentUpdatedEvent;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.observation.EventListener;
import org.xwiki.observation.event.Event;
import org.xwiki.rendering.block.MacroBlock;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;

@Component
@Named("TaskEventListener")
public class TaskEventListener implements EventListener {

	@Inject
	@Named("context")
	private Provider<ComponentManager> componentManagerProvider;

	@Override
	public List<Event> getEvents() {
		List<Event> events = new ArrayList<Event>();
		events.add(new DocumentUpdatedEvent());
		events.add(new DocumentCreatedEvent());
		return events;
	}

	@Override
	public String getName() {
		return Task.MACRONAME;
	}
	
	@Override
	public void onEvent(Event event, Object source, Object data) {
		TaskContainer tc = new TaskContainer((XWikiDocument) source, (XWikiContext) data);
		
		if ((event instanceof DocumentUpdatedEvent) || (event instanceof DocumentCreatedEvent)) {

			tc.removeUnusedObjects();
			
			List<MacroBlock> newMacros = tc.getNewMacros();
			for (MacroBlock m : newMacros) {
				tc.createObject(m);
			}
			
			List<MacroBlock> changedMacros = tc.getChangedMacroObjects();
			for (MacroBlock m : changedMacros) {
				tc.updateObject(m);
			}
			
			tc.update();
		}
	}



}
