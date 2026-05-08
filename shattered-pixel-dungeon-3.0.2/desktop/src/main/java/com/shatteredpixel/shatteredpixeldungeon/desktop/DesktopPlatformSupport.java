/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.Game;
import com.watabou.utils.PlatformSupport;
import com.watabou.utils.Point;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.swing.JFileChooser;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DesktopPlatformSupport extends PlatformSupport {

	//we recall previous window sizes as a workaround to not save maximized size to settings
	//have to do this as updateDisplaySize is called before maximized is set =S
	protected static Point[] previousSizes = null;

	@Override
	public void updateDisplaySize() {
		if (previousSizes == null){
			previousSizes = new Point[2];
			previousSizes[1] = SPDSettings.windowResolution();
		} else {
			previousSizes[1] = previousSizes[0];
		}
		previousSizes[0] = new Point(Game.width, Game.height);
		if (!SPDSettings.fullscreen()) {
			SPDSettings.windowResolution( previousSizes[0] );
		}
	}

	private static boolean first = true;

	@Override
	public void updateSystemUI() {
		Gdx.app.postRunnable( new Runnable() {
			@Override
			public void run () {
				if (SPDSettings.fullscreen()){
					int monitorNum = 0;
					if (!first){
						Graphics.Monitor[] monitors = Gdx.graphics.getMonitors();
						for (int i = 0; i < monitors.length; i++){
							if (((Lwjgl3Graphics.Lwjgl3Monitor)Gdx.graphics.getMonitor()).getMonitorHandle()
									== ((Lwjgl3Graphics.Lwjgl3Monitor)monitors[i]).getMonitorHandle()) {
								monitorNum = i;
							}
						}
					} else {
						monitorNum = SPDSettings.fulLScreenMonitor();
					}

					Graphics.Monitor[] monitors = Gdx.graphics.getMonitors();
					if (monitors.length <= monitorNum) {
						monitorNum = 0;
					}
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode(monitors[monitorNum]));
					SPDSettings.fulLScreenMonitor(monitorNum);
				} else {
					Point p = SPDSettings.windowResolution();
					Gdx.graphics.setWindowedMode( p.x, p.y );
				}
				first = false;
			}
		} );
	}
	
	@Override
	public boolean connectedToUnmeteredNetwork() {
		return true; //no easy way to check this in desktop, just assume user doesn't care
	}

	@Override
	public boolean supportsVibration() {
		//only supports vibration via controller
		return ControllerHandler.vibrationSupported();
	}

	@Override
	public String chooseDirectory(String title, String initialPath) {
		String selectedPath = chooseDirectoryWithTinyFileDialogs(title, initialPath);
		if (selectedPath != null && !selectedPath.isEmpty()) {
			return selectedPath;
		}

		if (SharedLibraryLoader.isMac) {
			return chooseDirectoryWithMacFallback(title, initialPath);
		}

		return null;
	}

	private String chooseDirectoryWithTinyFileDialogs(String title, String initialPath) {
		try {
			return TinyFileDialogs.tinyfd_selectFolderDialog(title, initialPath);
		} catch (Throwable ignored) {
			return null;
		}
	}

	private String chooseDirectoryWithMacFallback(String title, String initialPath) {
		try {
			String selectedPath = chooseDirectoryWithMacFileDialog(title, initialPath);
			if (selectedPath != null && !selectedPath.isEmpty()) {
				return selectedPath;
			}
		} catch (Throwable ignored) {
			// Fall through to the Swing chooser if the native AWT dialog fails.
		}

		try {
			return chooseDirectoryWithSwingChooser(title, initialPath);
		} catch (Throwable ignored) {
			return null;
		}
	}

	private String chooseDirectoryWithMacFileDialog(String title, String initialPath) {
		File initialDirectory = normalizeInitialDirectory(initialPath);
		String previousDirectoryMode = System.getProperty("apple.awt.fileDialogForDirectories");
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
		try {
			FileDialog dialog = new FileDialog((Frame) null, title, FileDialog.LOAD);
			if (initialDirectory != null) {
				dialog.setDirectory(initialDirectory.getAbsolutePath());
			}
			dialog.setVisible(true);

			File[] selectedFiles = dialog.getFiles();
			if (selectedFiles != null && selectedFiles.length > 0 && selectedFiles[0] != null) {
				return selectedFiles[0].getAbsolutePath();
			} else if (dialog.getFile() != null) {
				File directory = dialog.getDirectory() == null
						? new File(dialog.getFile())
						: new File(dialog.getDirectory(), dialog.getFile());
				return directory.getAbsolutePath();
			}
			return null;
		} finally {
			if (previousDirectoryMode == null) {
				System.clearProperty("apple.awt.fileDialogForDirectories");
			} else {
				System.setProperty("apple.awt.fileDialogForDirectories", previousDirectoryMode);
			}
		}
	}

	private String chooseDirectoryWithSwingChooser(String title, String initialPath) {
		File initialDirectory = normalizeInitialDirectory(initialPath);
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(title);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (initialDirectory != null) {
			chooser.setCurrentDirectory(initialDirectory);
			chooser.setSelectedFile(initialDirectory);
		}

		int result = chooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	private File normalizeInitialDirectory(String initialPath) {
		if (initialPath == null || initialPath.isEmpty()) {
			return null;
		}

		File initialFile = new File(initialPath);
		if (initialFile.isDirectory()) {
			return initialFile;
		}

		File parent = initialFile.getParentFile();
		return parent != null && parent.isDirectory() ? parent : null;
	}

	/* FONT SUPPORT */
	
	//custom pixel font, for use with Latin and Cyrillic languages
	private static FreeTypeFontGenerator basicFontGenerator;
	//droid sans fallback, for asian fonts
	private static FreeTypeFontGenerator asianFontGenerator;
	
	@Override
	public void setupFontGenerators(int pageSize, boolean systemfont) {
		//don't bother doing anything if nothing has changed
		if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont){
			return;
		}
		this.pageSize = pageSize;
		this.systemfont = systemfont;

		resetGenerators(false);
		fonts = new HashMap<>();

		if (systemfont) {
			basicFontGenerator = asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		} else {
			basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
			asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		}
		
		fonts.put(basicFontGenerator, new HashMap<>());
		fonts.put(asianFontGenerator, new HashMap<>());
		
		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
	}
	
	private static Matcher asianMatcher = Pattern.compile("\\p{InHangul_Syllables}|" +
			"\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}|\\p{InHalfwidth_and_Fullwidth_Forms}|" +
			"\\p{InHiragana}|\\p{InKatakana}").matcher("");

	@Override
	protected FreeTypeFontGenerator getGeneratorForString( String input ){
		if (asianMatcher.reset(input).find()){
			return asianFontGenerator;
		} else {
			return basicFontGenerator;
		}
	}
	
	//splits on newline (for layout), chinese/japanese (for font choice), and '_'/'**' (for highlighting)
	private Pattern regularsplitter = Pattern.compile(
			"(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})");
	
	//additionally splits on spaces, so that each word can be laid out individually
	private Pattern regularsplitterMultiline = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})");
	
	@Override
	public String[] splitforTextBlock(String text, boolean multiline) {
		if (multiline) {
			return regularsplitterMultiline.split(text);
		} else {
			return regularsplitter.split(text);
		}
	}
}
