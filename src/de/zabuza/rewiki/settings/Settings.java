package de.zabuza.rewiki.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Class for the tool settings.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 * 
 */
public final class Settings {
	/**
	 * Comment for the configuration file.
	 */
	private static final String FILE_COMMENT = "Configuration settings for reWiki.";
	/**
	 * File path of the settings.
	 */
	private static final String FILEPATH = "config.ini";
	/**
	 * Properties object which holds the saved settings.
	 */
	private final Properties mProperties;

	/**
	 * Create a new settings object.
	 */
	public Settings() {
		this.mProperties = new Properties();
	}

	/**
	 * Loads settings of the saved file and applies the properties to the
	 * provider settings.
	 * 
	 * @param provider
	 *            Provider which settings will be affected
	 */
	public final void loadSettings(final ISettingsProvider provider) {
		System.out.println("Loading settings");
		try (final FileInputStream fis = new FileInputStream(FILEPATH)) {
			try {
				this.mProperties.load(fis);
			} catch (final FileNotFoundException e) {
				System.out.println("Creating settings file");
				saveSettings(provider);

				try (final FileInputStream anotherFis = new FileInputStream(FILEPATH)) {
					this.mProperties.load(anotherFis);
				}
			}

			// Fetch and set every saved setting
			for (final Entry<Object, Object> entry : this.mProperties.entrySet()) {
				provider.setSetting((String) entry.getKey(), (String) entry.getValue());
			}
		} catch (final IOException e) {
			// Log the error but continue
			System.err.println("Error while loading settings: " + e.getStackTrace());
		}
	}

	/**
	 * Saves the current settings of the provider in a file.
	 * 
	 * @param provider
	 *            Provider which settings will be affected
	 */
	public final void saveSettings(final ISettingsProvider provider) {
		System.out.println("Saving settings");

		// Fetch and put every setting
		for (final Entry<String, String> entry : provider.getAllSettings().entrySet()) {
			this.mProperties.put(entry.getKey(), entry.getValue());
		}

		try (final FileOutputStream target = new FileOutputStream(new File(FILEPATH))) {
			// Save the settings
			this.mProperties.store(target, FILE_COMMENT);
		} catch (final IOException e) {
			// Log the error but continue
			System.err.println("Error while saving settings: " + e.getStackTrace());
		}
	}
}