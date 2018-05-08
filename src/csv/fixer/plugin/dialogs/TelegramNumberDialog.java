package csv.fixer.plugin.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IEditorPart;

import csv.fixer.plugin.main.CSVFixer;

public class TelegramNumberDialog extends TitleAreaDialog {
    
    private Text telegramNumberTF;
    private Label foundTelegramLabel;
    private IEditorPart activeEditor;

    public TelegramNumberDialog(Shell parentShell) {
        
        super(parentShell);
    }

    @Override
    public void create() {
        super.create();
        setTitle("Telegramnummer finder");
        setMessage("Durch Eingabe einer natürlichen Zahl in das Textfeld, wird das x-te Telegramm zurückgegeben, wenn vorhanden", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        createTextField(container);

        Button button = new Button(container, SWT.PUSH);
        button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
                false));
        button.setText("show Telegram");
        
        foundTelegramLabel = new Label(container, SWT.BEGINNING); // War: SWT.NONE
        foundTelegramLabel.setText("Telegramm Nummer");
        foundTelegramLabel.setBounds(foundTelegramLabel.getBounds().x, foundTelegramLabel.getBounds().y, 500, foundTelegramLabel.getBounds().height);
        
        button.addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                // Und starte damit den CSV Fixer
//                foundTelegramLabel.setText(CSVFixer.showTelegramNumber(activeEditor, telegramNumberTF.getText()));
            }
        });

        return area;
    }

    private void createTextField(Composite container) {
        
        Label textFieldLabel = new Label(container, SWT.NONE);
        textFieldLabel.setText("Telegramm Nummer");

        GridData dataFirstName = new GridData();
        dataFirstName.grabExcessHorizontalSpace = true;
        dataFirstName.horizontalAlignment = GridData.FILL;

        telegramNumberTF = new Text(container, SWT.BORDER);
        telegramNumberTF.setLayoutData(dataFirstName);
    }

    // overriding this methods allows you to set the
    // title of the custom dialog
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    @Override
    protected Point getInitialSize() {
        
        return new Point(800, 600);
    }

    public void setActiveEditor(IEditorPart activeEditor) {

        this.activeEditor = activeEditor;
    }

}
