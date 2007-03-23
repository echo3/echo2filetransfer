package nextapp.echo2.testapp.filetransfer.testscreen;

import nextapp.echo2.app.Extent;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.filetransfer.FilePane;
import nextapp.echo2.app.filetransfer.ResourceDownloadProvider;
import nextapp.echo2.testapp.filetransfer.ButtonColumn;

public class FilePaneTest extends SplitPane {

    public FilePaneTest() {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
        setStyleName("DefaultResizable");
        
        final FilePane filePane = new FilePane();

        ButtonColumn controlsColumn = new ButtonColumn();
        controlsColumn.setStyleName("TestControlsColumn");
        
        controlsColumn.addButton("Provider: Null", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filePane.setProvider(null);
            }
        });
        
        controlsColumn.addButton("Provider: Resource PDF", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filePane.setProvider(new ResourceDownloadProvider(
                        "/nextapp/echo2/testapp/filetransfer/resource/testfile/Test.pdf", "application/pdf"));
            }
        });
        
        add(controlsColumn);
        
        filePane.setProvider(new ResourceDownloadProvider(
                "/nextapp/echo2/testapp/filetransfer/resource/testfile/Test.pdf", "application/pdf"));
        add(filePane);
    }
}
