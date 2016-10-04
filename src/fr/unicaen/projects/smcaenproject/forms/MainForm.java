/**
 * @author Hivinau GRAFFE & Jesus OLARRA GARNICA
 * @version 1.0.0
 * @since september, 2016
 */
package fr.unicaen.projects.smcaenproject.forms;

import java.util.Vector;
import java.util.Collections;
import java.util.List;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import fr.unicaen.projects.smcaenproject.utilities.ContextUtility;
import fr.unicaen.projects.smcaenproject.utilities.XMLUtility;
import fr.unicaen.projects.smcaenproject.listeners.SearchListener;

@SuppressWarnings("serial")
public class MainForm extends JFrame implements DocumentListener {

	//PROPERTIES
	private JTextField searchTextField = null;
    
    private final List<SearchListener> searchListeners;
    private final Object synchronize = new Object();
	
	//METHODS DELEGATE
        
    @Override
    public void insertUpdate(DocumentEvent e) {
        
        Document document = e.getDocument();
        handleSearchEvent(document);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        
        Document document = e.getDocument();
        handleSearchEvent(document);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        
        Document document = e.getDocument();
        handleSearchEvent(document);
    }
	
	//INIT

	/**
	 * Constructor :
	 * Init form components.
	 */
	public MainForm() {
            
		this.searchListeners = Collections.synchronizedList(new Vector<>());
		
        ContextUtility.runOnUIThread(() -> {
            
            MainForm.this.createAndShowGUI();
        });
	}
	
	//PUBLIC METHODS
	
	/**
     * Add listener to be notified if text in textField changed.
     * @param listener listener used to be fired.
     */
	public void addSearchListener(SearchListener listener) {

		 if((this.searchListeners == null) || (listener == null)) {

             return;
         }

         if(!this.searchListeners.contains(listener)) {
         
             this.searchListeners.add(listener);
         }
    }
	
	//PRIVATE METHODS
        
	private void handleSearchEvent(final Document document) {
        
        Thread thread = new Thread(new Runnable() {
            
            @Override
            public void run() {
                
                ContextUtility.runOnUIThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        
                        List<SearchListener> _listeners;

                        synchronized (MainForm.this.synchronize){
            
                            if(MainForm.this.searchListeners.isEmpty()){
                                return;
                            }   

                            _listeners = new Vector<>(MainForm.this.searchListeners.size());
                            _listeners.addAll(MainForm.this.searchListeners);
                        }

                        _listeners.forEach((listener) -> {
            
                        int textLength = document.getLength();
            
                        try {
                
                            String text = document.getText(0, textLength);
                
                            if(text != null) {
                    
                                listener.onTextChanged(text);
                            }
                
                        } catch (BadLocationException ignored) {
                            //TODO : handle error while text changed.
                            
                        }
            
                     });
                    }
                    
                });
            }
        });
        
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

	/**
     * Init components.
     * 
     * For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
	private void createAndShowGUI() {
		
		//Standard parameters :
		//- when user clicks on x, app will be closed
		//- set minimum size for resizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
    	
        //Center frame to DisplayScreen
        Window component = getOwner();
        setLocationRelativeTo(component);
	
        //Create content panel
        JPanel panel = new JPanel();
        
        //Use layout to auto-resize components
        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[]{0, 0, 0, 0};
        layout.rowHeights = new int[]{0, 0};
        layout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(layout);
        
		//Create text field to type text :
		//- add listener to prevent text changed
        this.searchTextField = new JTextField(10);
        this.searchTextField.getDocument().addDocumentListener(this);
        
        //Add constraints for searchTextField relative to layout
        GridBagConstraints searchtextFieldConstraints = new GridBagConstraints();
        searchtextFieldConstraints.gridwidth = GridBagConstraints.VERTICAL;
        searchtextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        searchtextFieldConstraints.insets = new Insets(10, 10, 10, 10);
		
        panel.add(this.searchTextField, searchtextFieldConstraints);
        
        setContentPane(panel);
        
        try {
        	
        	//Retrieve datas from xmls : as string, color or size
        	
            String appName = XMLUtility.getString("app_name");
            setTitle(appName);
            
            int appDefaultWidth = XMLUtility.getSize("app_default_width");
            int appDefaultHeight = XMLUtility.getSize("app_default_height");
            setSize(appDefaultWidth, appDefaultHeight);
		
            Color appbackgroundColor =  XMLUtility.getColor("app_background");
            setBackground(appbackgroundColor);
            panel.setBackground(appbackgroundColor);
            
            this.searchTextField.setForeground(XMLUtility.getColor("search_field"));
		
        } catch (Exception ignored) {
            //TODO : app title and background color can be set manually here if needed
            
        }
        
        //show window
        setVisible(true);
    }
}
