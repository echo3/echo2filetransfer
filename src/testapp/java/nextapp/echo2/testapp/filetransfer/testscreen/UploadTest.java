/* 
 * This file is part of the Echo Web Application Framework (hereinafter "Echo").
 * Copyright (C) 2002-2009 NextApp, Inc.
 *
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 */

package nextapp.echo2.testapp.filetransfer.testscreen;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

import nextapp.echo2.app.Color;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.filetransfer.UploadEvent;
import nextapp.echo2.app.filetransfer.UploadListener;
import nextapp.echo2.app.filetransfer.UploadSelect;
import nextapp.echo2.testapp.filetransfer.ButtonColumn;
import nextapp.echo2.testapp.filetransfer.InteractiveApp;
import nextapp.echo2.testapp.filetransfer.StyleUtil;

/**
 * Interactive test module for <code>ContentPane</code>s.
 */
public class UploadTest extends SplitPane {

    public UploadTest() {
        super(SplitPane.ORIENTATION_HORIZONTAL, new Extent(250, Extent.PX));
        setStyleName("DefaultResizable");
        
        ButtonColumn controlsColumn = new ButtonColumn();
        controlsColumn.setStyleName("TestControlsColumn");
        add(controlsColumn);
        
        final UploadSelect uploadSelect = new UploadSelect();
        try {
            uploadSelect.addUploadListener(new UploadListener() {
            
                public void invalidFileUpload(UploadEvent e) {
                    InteractiveApp app = InteractiveApp.getApp();
                    app.consoleWrite("-----------------------------");
                    app.consoleWrite("Invalid File Upload");
                }
            
                public void fileUpload(UploadEvent e) {
                    InteractiveApp app = InteractiveApp.getApp();
                    app.consoleWrite("-----------------------------");
                    app.consoleWrite("File Upload");
                    app.consoleWrite("ContentType = " + e.getContentType());
                    app.consoleWrite("FileName = " + e.getFileName());
                    app.consoleWrite("Size = " + e.getSize());
                    
                    int totalBytesRead = 1; // offset -1 returned when no more bytes available.
                    InputStream in = e.getInputStream();
                    try {
                        byte[] data = new byte[16];
                        int bytesRead = in.read(data);
                        totalBytesRead += bytesRead;
                        while (bytesRead != -1) {
                            if (totalBytesRead < 1024) {
                                StringBuffer out = new StringBuffer();
                                for (int i = 0; i < bytesRead; ++i) {
                                    int value = data[i] & 0xff;
                                    if (value < 0x10) {
                                        out.append("0");
                                    }
                                    out.append(Integer.toString(value, 16));
                                    out.append(' ');
                                }
                                for (int i = bytesRead; i < 16; ++i) {
                                    out.append("   ");
                                }
                                out.append(" | ");
                                for (int i = 0; i < bytesRead; ++i) {
                                    if (data[i] >= 32 && data[i] <= 126) {
                                        out.append((char) data[i]);
                                    } else {
                                        out.append(' ');
                                    }
                                }
                                app.consoleWrite(out.toString());
                            }
                            bytesRead = in.read(data);
                            totalBytesRead += bytesRead;
                        }
                    } catch (IOException ex) {
                        app.consoleWrite(ex.toString());
                    } finally {
                        try {
                            in.close();
                        } catch (IOException ex) {
                            app.consoleWrite(ex.toString());
                        }
                    }
                    app.consoleWrite("InputStream Bytes Read = " + totalBytesRead);
                }
            });
        } catch (TooManyListenersException ex) {
            throw new RuntimeException(ex);
        }
        add(uploadSelect);
        
        controlsColumn.addButton("Set Foreground", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = StyleUtil.randomColor();
                uploadSelect.setForeground(color);
            }
        });
        
        controlsColumn.addButton("Set Background", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = StyleUtil.randomColor();
                uploadSelect.setBackground(color);
            }
        });
        
        controlsColumn.addButton("Set Width = Default", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setWidth(null);
            }
        }); 
        
        controlsColumn.addButton("Set Width = 500px", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setWidth(new Extent(500));
            }
        }); 
        
        controlsColumn.addButton("Set Width = 100%", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uploadSelect.setWidth(new Extent(100, Extent.PERCENT));
            }
        }); 
    }
}
