package csv.fixer.plugin.handlers;

import org.eclipse.core.commands.*;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import csv.fixer.plugin.main.CSVFixer;

/**
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CSVFixerHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Hole das aktuelle Fenster
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		// Und den aktuellen Editor
		IEditorPart activeEditor = window.getActivePage().getActiveEditor();
		
		// Und starte damit den CSV Fixer
		CSVFixer.startFixing(activeEditor);
		
		return null;
	}
}