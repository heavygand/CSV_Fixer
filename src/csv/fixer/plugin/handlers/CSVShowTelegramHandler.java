package csv.fixer.plugin.handlers;

import java.io.IOException;

import org.eclipse.core.commands.*;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import csv.fixer.plugin.dialogs.TelegramNumberDialog;
import csv.fixer.plugin.main.CSVFixer;

/**
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CSVShowTelegramHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Hole das aktuelle Fenster
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		// Und den aktuellen Editor
		IEditorPart activeEditor = window.getActivePage().getActiveEditor();
		
		// Und starte damit den CSV Fixer
		try {
			
			CSVFixer.showTelegramNumber(activeEditor);
		}
		catch (IOException e) {
			
			e.printStackTrace();
		}
		
//		TelegramNumberDialog telegramNumberDialog = new TelegramNumberDialog(window.getShell());
//		
//		telegramNumberDialog.setActiveEditor(window.getActivePage().getActiveEditor());
//		telegramNumberDialog.create();
//		telegramNumberDialog.open();
		
		return null;
	}
}