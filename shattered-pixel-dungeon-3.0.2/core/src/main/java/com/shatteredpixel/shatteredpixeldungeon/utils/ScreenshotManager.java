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

package com.shatteredpixel.shatteredpixeldungeon.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.FileUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotManager {

	private static final String SCREENSHOTS_DIR = "screenshots";
	private static final String DESKTOP_SCREENSHOTS_DIR = "Shattered Pixel Dungeon Screenshots";
	private static final DateTimeFormatter FILE_TIMESTAMP =
			DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

	public static FileHandle activeDirectory() {
		FileHandle configured = configuredDirectory();
		if (configured != null) {
			return configured;
		}
		return defaultDirectory();
	}

	public static String activeDirectoryPath() {
		return activeDirectory().file().getAbsolutePath();
	}

	public static void capture() {
		try {
			final Pixmap screenshot = ScreenUtils.getFrameBufferPixmap(
					0,
					0,
					Gdx.graphics.getBackBufferWidth(),
					Gdx.graphics.getBackBufferHeight()
			);
			flipPixmap(screenshot);

			final FileHandle directory = activeDirectory();
			directory.mkdirs();

			final String fileName = "screenshot_" + FILE_TIMESTAMP.format(LocalDateTime.now()) + ".png";
			final FileHandle output = directory.child(fileName);

			Thread saver = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						PixmapIO.writePNG(output, screenshot);
						postLogSuccess(output.file().getAbsolutePath());
					} catch (Exception e) {
						ShatteredPixelDungeon.reportException(e);
						postLogFailure(e);
					} finally {
						screenshot.dispose();
					}
				}
			}, "spd-screenshot-saver");
			saver.setDaemon(true);
			saver.start();

		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			postLogFailure(e);
		}
	}

	private static void flipPixmap(Pixmap pixmap) {
		int width = pixmap.getWidth();
		int height = pixmap.getHeight();

		for (int y = 0; y < height / 2; y++) {
			int oppositeY = height - 1 - y;
			for (int x = 0; x < width; x++) {
				int top = pixmap.getPixel(x, y);
				int bottom = pixmap.getPixel(x, oppositeY);
				pixmap.drawPixel(x, y, bottom);
				pixmap.drawPixel(x, oppositeY, top);
			}
		}
	}

	private static FileHandle configuredDirectory() {
		String configuredPath = SPDSettings.screenshotDir();
		if (configuredPath == null || configuredPath.isEmpty()) {
			return null;
		}

		try {
			FileHandle configured = Gdx.files.absolute(configuredPath);
			configured.mkdirs();
			if (configured.exists() && configured.isDirectory()) {
				return configured;
			}
		} catch (Exception ignored) {
			//fall back to default location below
		}

		return null;
	}

	private static FileHandle defaultDirectory() {
		if (DeviceCompat.isDesktop()) {
			String userHome = System.getProperty("user.home");
			if (userHome != null && !userHome.isEmpty()) {
				FileHandle desktop = Gdx.files.absolute(userHome + "/Desktop");
				if (desktop.exists() && desktop.isDirectory()) {
					return desktop.child(DESKTOP_SCREENSHOTS_DIR);
				}
			}
		}

		return FileUtils.getFileHandle(Files.FileType.Local, SCREENSHOTS_DIR);
	}

	private static void postLogSuccess(final String fileName) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GLog.p(Messages.get((Class) null, "misc.screenshot_saved", fileName));
			}
		});
	}

	private static void postLogFailure(final Exception exception) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GLog.w(Messages.get((Class) null, "misc.screenshot_failed", exception.getMessage()));
			}
		});
	}
}
