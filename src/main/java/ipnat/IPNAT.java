/*
 * #%L
 * hIPNAT plugins for Fiji distribution of ImageJ
 * %%
 * Copyright (C) 2017 Tiago Ferreira
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package ipnat;


import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.util.VersionUtils;

import ij.IJ;

public class IPNAT {

	public static final String EXTENDED_NAME = "Image Processing for NeuroAnatomy and Tree-like structures";
	public static final String ABBREV_NAME = "hIPNAT";
	public static final String DOC_URL = "https://imagej.net/Neuroanatomy";
	public static final String SRC_URL = "https://github.com/tferr/hIPNAT";

	/** The hIPNAT version **/
	public static String VERSION = version();

	/** A reference to the build date */
	public static String BUILD_DATE = buildDate();

	/** A reference to the build year */
	public static String BUILD_YEAR = buildYear();

	private static Context context;
	private static LogService logService;
	private static boolean initialized;

	private IPNAT() {
	}

	private synchronized static void initialize() {
		if (initialized)
			return;
		if (context == null)
			context = (Context) IJ.runPlugIn("org.scijava.Context", "");
		if (logService == null)
			logService = context.getService(LogService.class);
		initialized = true;
	}

	protected static void error(final String string) {
		IJ.error("hIPNAT v" + VERSION, string);
	}

	protected static void log(final String string) {
		if (!initialized)
			initialize();
		logService.info("[hIPNAT] " + string);
	}

	protected static void warn(final String string) {
		if (!initialized)
			initialize();
		logService.warn("[hIPNAT] " + string);
	}

	protected static void log(final String... strings) {
		if (strings != null)
			log(String.join(" ", strings));
	}

	public static void handleException(final Exception e) {
		IJ.setExceptionHandler(new ipnat.ExceptionHandler());
		IJ.handleException(e);
		IJ.setExceptionHandler(null); // Revert to the default behavior
	}

	public static String getVersion() {
		return ABBREV_NAME + " v" + VERSION;
	}

	/**
	 * Retrieves hIPNAT's version
	 *
	 * @return the version or a non-empty place holder string if version could
	 *         not be retrieved.
	 *
	 */
	private static String version() {
		if (VERSION == null) {
			VERSION = VersionUtils.getVersion(IPNAT.class);
			return (VERSION == null) ? "X Dev" : VERSION;
		}
		return VERSION;
	}

	/**
	 * Retrieves hIPNAT's implementation date
	 *
	 * @return the implementation date or an empty strong if date could not be
	 *         retrieved.
	 */
	private static String buildDate() {
		// http://stackoverflow.com/questions/1272648/
		if (BUILD_DATE == null) {
			final Class<IPNAT> clazz = IPNAT.class;
			final String className = clazz.getSimpleName() + ".class";
			final String classPath = clazz.getResource(className).toString();
			final String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1)
					+ "/META-INF/MANIFEST.MF";
			try {
				final Manifest manifest = new Manifest(new URL(manifestPath).openStream());
				final Attributes attr = manifest.getMainAttributes();
				BUILD_DATE = attr.getValue("Implementation-Date");
				BUILD_DATE = BUILD_DATE.substring(0, BUILD_DATE.lastIndexOf("T"));
			} catch (final Exception ignored) {
				BUILD_DATE = "";
			}
		}
		return BUILD_DATE;
	}

	/**
	 * Retrieves hIPNAT's implementation year.
	 *
	 * @return the implementation year or an empty string if date could not be
	 *         retrieved.
	 */
	private static String buildYear() {
		return (BUILD_DATE == null || BUILD_DATE.length() < 4) ? "" : BUILD_DATE.substring(0, 4);
	}
}
