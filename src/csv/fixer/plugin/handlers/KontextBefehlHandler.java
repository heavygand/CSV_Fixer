package csv.fixer.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import csv.fixer.plugin.main.CSVFixer;

public class KontextBefehlHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Hole das aktuelle Fenster
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		// Und den aktuellen Editor
		IEditorPart activeEditor = window.getActivePage().getActiveEditor();
		
		// Und starte damit den CSV Fixer
		CSVFixer.removeTimestamp(activeEditor);
		
		return null;
	}
}