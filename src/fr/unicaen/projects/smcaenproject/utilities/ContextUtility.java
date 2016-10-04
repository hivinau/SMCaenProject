/**
 * @author Hivinau GRAFFE & Jesus OLARRA GARNICA
 * @version 1.0.0
 * @since september, 2016
 */
package fr.unicaen.projects.smcaenproject.utilities;

import javax.swing.SwingUtilities;

public final class ContextUtility {

	public final static void runOnUIThread(Runnable runnable) {
		
		SwingUtilities.invokeLater(runnable);
	}
}
