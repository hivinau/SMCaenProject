/**
 * @author Hivinau GRAFFE & Jesus OLARRA GARNICA
 * @version 1.0.0
 * @since september, 2016
 */
package fr.unicaen.projects.smcaenproject;

import fr.unicaen.projects.smcaenproject.forms.MainForm;
import fr.unicaen.projects.smcaenproject.listeners.SearchListener;

public final class Main {

    /**
     * Application entry.
     * @param args arguments passed to application.
     */
    public static void main(String[] args) {
		
        MainForm mainForm = new MainForm();
        mainForm.addSearchListener(new SearchListener() {
            
            @Override
            public void onTextChanged(final String text) {
                
                System.out.print(text);
            } 
        });
    }

}
