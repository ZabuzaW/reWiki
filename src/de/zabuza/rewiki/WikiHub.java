package de.zabuza.rewiki;

import net.sourceforge.jwbf.core.contentRep.Article;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

/**
 * Hub that is used to connect with the <tt>FreewarWiki</tt> in order to publish
 * results.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class WikiHub {
	/**
	 * The password of the user to post with.
	 */
	private final String mPassword;
	/**
	 * The name of the user to post with.
	 */
	private final String mUsername;
	/**
	 * WikiBot that is used to access the given Wiki.
	 */
	private final MediaWikiBot mWikiBot;

	/**
	 * Creates a new WikiHub that can interact with the given Wiki.
	 * 
	 * @param server
	 *            The full server address of the wiki to connect with, including
	 *            protocol (for example <tt>http://www.example.org</tt>)
	 * @param username
	 *            The name of the user to post with
	 * @param password
	 *            The password of the user to post with
	 */
	public WikiHub(final String server, final String username, final String password) {
		this.mWikiBot = new MediaWikiBot(server);
		this.mUsername = username;
		this.mPassword = password;
	}

	/**
	 * Gets the article with the given name.
	 * 
	 * @param name
	 *            The name of the article to get
	 * @return The article with the given name
	 */
	public Article getArticle(final String name) {
		return this.mWikiBot.getArticle(name);
	}

	/**
	 * Saves the article using its current information by connecting to the
	 * Wiki.
	 * 
	 * @param article
	 *            Article to save
	 */
	public void saveArticle(final Article article) {
		login();
		article.setEditSummary("Automatisiertes Update (Verwendung der [[Skripte]])");
		article.save();
	}

	/**
	 * Login to the Wiki.
	 */
	private void login() {
		if (!this.mWikiBot.isLoggedIn()) {
			this.mWikiBot.login(this.mUsername, this.mPassword);
		}
	}
}
