package csv.fixer.plugin.main;

import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.texteditor.*;

public class CSVFixer {

	private static HashMap<String, Integer> connections = new HashMap<>();
	private static IDocument doc;

	public static void startFixing(IEditorPart part) {
		
		ISelectionProvider selectionProvider = ((ITextEditor)part).getSelectionProvider();
	    ISelection selection = selectionProvider.getSelection();
		
		connections.clear();

		try {
			
			List<String> allLines = getAllLines(part);
			
			int lineNumber = 0;
			boolean changed = false;
			
			for(String line : allLines) {
				
				// Wenn es sich um ein Telegramm handelt
				if (!line.startsWith("@") && !line.startsWith("#") && line.trim().length() > 1) {
					
					// Hole den richtigen Teil raus (also nicht den timestamp)
					String timestampPart = substringBefore(line, ";");
					System.out.println("timestampPart: " + timestampPart);
					
					String telegramPart = ";"+substringAfter(line, ";");
					System.out.println("telegramPart: " + telegramPart);
					
					// Hole den Counter für diese Verbindung
					int theRightConnectionCounter = getOrCreateCounter(telegramPart);
					
					// Hole den Zähler raus aus dem aktuellen Telegramm
					int currentConnectionCounter = Integer.parseInt(telegramPart.substring(1, 3));
					
					// Setze den Zähler (telegramCounter), wenn er nicht richtig ist
					if(currentConnectionCounter != theRightConnectionCounter) {
						
						changed = true;
						String correctedLine = timestampPart + ";" + StringUtils.leftPad((theRightConnectionCounter+""), 2, "0") + telegramPart.substring(3);
						
						modifyLine(allLines, lineNumber, line, correctedLine);
					}
					
				}
				
				lineNumber++;
			}
			
			if(!changed) {
				
				System.out.println("Keine Änderungen");
				System.out.println();
			}
			// Changed
			else {
				
				putIntoEditor(doc, allLines);
				
				selectionProvider.setSelection(selection);	
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
    public static void showTelegramNumber(IEditorPart part) throws IOException {
        
        connections.clear();
        
        try {
            
            List<String> allLines = getAllLines(part);
            
            int lineNumber = 1;
            int telegramNumber = 1;
            
            for(String line : allLines) {
                
                // Wenn es sich um ein Telegramm handelt
                if (!line.startsWith("@") && !line.startsWith("#") && line.trim().length() > 1) {
                    
                    // Und die Telegrammnummer stimmt
//                    if((telegramNumber+"").equals(number)) {
//                        
//                        System.out.println("Telegramm #"+telegramNumber+" ist in Zeile "+lineNumber+".");
//                        break;
//                    }
                    
                    telegramNumber++;
                }
                
                lineNumber++;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
	
	public static void removeTimestamp(IEditorPart part) {
		
		ISelectionProvider selectionProvider = ((ITextEditor)part).getSelectionProvider();
	    ISelection selection = selectionProvider.getSelection();
	    
	    ITextSelection textSelection = null;
	    
	    if (selection instanceof ITextSelection) {
	    	
	    	textSelection = (ITextSelection)selection;
	    }
		
		int startLine = textSelection.getStartLine();
		int endLine = textSelection.getEndLine();
		
		System.out.println("Zeile "+(startLine+1)+" ist markiert");
		
		connections.clear();

		try {
			
			List<String> allLines = getAllLines(part);
			
			int lineNumber = 0;
			
			// Gehe durch alle zeilen
			for(String line : allLines) {
				
				System.out.println("Zeile "+lineNumber+": "+line);
				
				// Wenn die Zeile im ausgewählten Bereich ist
				if (startLine <= lineNumber && lineNumber <= endLine && line.contains(";")) {
					
					String newLine = substringAfter(line, ";");
					
					// Zeile ohne timestamp reinschreiben
					modifyLine(allLines, lineNumber, line, ";"+newLine);
					
					putIntoEditor(doc, allLines);
				}
				
				lineNumber++;
			}
		
			selectionProvider.setSelection(selection);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void toggleComment(IEditorPart part) {
		
		ISelectionProvider selectionProvider = ((ITextEditor)part).getSelectionProvider();
	    ISelection selection = selectionProvider.getSelection();
	    
	    ITextSelection textSelection = null;
	    
	    if (selection instanceof ITextSelection) {
	    	
	    	textSelection = (ITextSelection)selection;
	    }
		
		int startLine = textSelection.getStartLine();
		int endLine = textSelection.getEndLine();
		
		int selectedLine = textSelection.getStartLine();
		
		System.out.println("Zeile "+(selectedLine+1)+" ist markiert");
		
		connections.clear();

		try {
			
			List<String> allLines = getAllLines(part);
			
			int lineNumber = 0;
			
			// Immer alles kommentieren, außer wenn alles ein Kommentar ist
			// D.h. Wenn irgendwo !kein! Kommentar vorkommt, dann muss alles kommentiert werden, sonst alles entkommentieren
			boolean commentAll = false;
			for(String line : allLines) {
				
				if (startLine <= lineNumber && lineNumber <= endLine && !line.startsWith("#")) {
					
					commentAll = true;
				}
				
				lineNumber++;
			}
			
			lineNumber = 0;
			
			// Gehe durch alle Zeilen und mache was zuvor festgelegt wurde
			for(String line : allLines) {
				
				if (startLine <= lineNumber && lineNumber <= endLine) {
					
					System.out.println("Zeile "+(lineNumber+1)+" wird "+(commentAll?"kommentiert":"unkommentiert")+": "+line);
					
					if(commentAll) {

						modifyLine(allLines, lineNumber, line, "#"+line);
					}
					else {
						
						modifyLine(allLines, lineNumber, line, line.substring(1));
					}
					putIntoEditor(doc, allLines);
				}
				
				lineNumber++;
			}
		
			selectionProvider.setSelection(selection);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private static List<String> getAllLines(IEditorPart part) {
		
		if (!(part instanceof AbstractTextEditor)) throw new RuntimeException("Fehler: part war nicht instanceof AbstractTextEditor");
		
		ITextEditor editor = (ITextEditor)part;
		IDocumentProvider dp = editor.getDocumentProvider();
		doc = dp.getDocument(editor.getEditorInput());

		String[] allLinesArray = doc.get().split("\n");
		
		return Arrays.asList(allLinesArray);
	}

	private static int getOrCreateCounter(String line) {
		
		String connection = null;
		try {
			// Hole die Connection
			connection = line.substring(4, 9);
		}
		catch (StringIndexOutOfBoundsException e) {
			
			System.out.println("Line "+line+" war komisch");
		}

		Integer connectionCounter = connections.get(connection);

		// Counter existierte schon
		if(connectionCounter != null) {

			// Dann um 1 erhöhen
			connectionCounter++;

			// In die Hashmap schreiben
			connections.put(connection, connectionCounter);

			// Und den erhöhten Wert zurückgeben
			return connectionCounter;
		}
		// Existierte noch nicht -> liefere 0 zurück
		else {

			connections.put(connection, 0);
			return 0;
		}
	}

	// Das hier ist before FIRST
	private static String substringBefore(String line, String beforeWhat) {

		return line.substring(0, line.indexOf(beforeWhat));
	}

	/**
	 * @param line
	 * @param afterWhat
	 * @return
	 */
	private static String substringAfter(String line, String afterWhat) {

		return line.substring(line.indexOf(afterWhat)+1);
	}

	/**
	 * @param doc
	 * @param allLines
	 */
	private static void putIntoEditor(IDocument doc, List<String> allLines) {

		String newLines = "";
		
		for(String line : allLines) {
			
			newLines += line+"\n";
		}
		
		// Schreibe die neuen Lines in den Editor
		doc.set(newLines);
	}

	/**
	 * Modifiziert eine Line in der Liste der Lines
	 * 
	 * @param allLines Alle Zeilen
	 * @param lineNumber
	 * @param oldLine
	 * @param newLine
	 */
	private static List<String> modifyLine(List<String> allLines, int lineNumber, String oldLine, String newLine) {

		// Ausgeben
		System.out.println("Vorher:  "+oldLine);
		
		// Baue den neuen Zähler in die Line ein
		allLines.set(lineNumber, newLine);
		
		// Ausgeben
		System.out.println("Nachher: "+allLines.get(lineNumber));
		System.out.println("");
		
		return allLines;
	}
}
