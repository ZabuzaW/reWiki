package de.zabuza.rewiki;

import java.util.LinkedList;
import java.util.List;

import de.zabuza.rewiki.settings.SettingsController;
import de.zabuza.rewiki.tasks.IWikiTask;
import de.zabuza.rewiki.tasks.map.AreaListTask;
import de.zabuza.rewiki.tasks.map.CoordinateListTask;
import de.zabuza.rewiki.tasks.map.LocationListTask;
import de.zabuza.rewiki.tasks.map.MapListTask;
import de.zabuza.rewiki.tasks.npc.FightCalcDataTask;
import de.zabuza.rewiki.tasks.npc.NpcImagesTask;
import de.zabuza.rewiki.tasks.npc.NpcListDataTask;
import de.zabuza.rewiki.tasks.npc.NpcListTask;

/**
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class ReWiki {
	/**
	 * 
	 * @param args
	 *            Not supported
	 */
	public static void main(final String[] args) {
		System.out.println("Initializing...");

		// Fetch the settings
		final SettingsController settings = new SettingsController();
		settings.initialize();

		// Create the Wiki hub
		final WikiHub wiki = new WikiHub(settings.getServerAddress(), settings.getUsername(), settings.getPassword());

		// Create all tasks
		final LinkedList<IWikiTask> tasks = new LinkedList<>();
		tasks.add(new NpcListDataTask());
		tasks.add(new FightCalcDataTask());
		tasks.add(new NpcListTask());
		tasks.add(new NpcImagesTask());

		tasks.add(new MapListTask());
		tasks.add(new CoordinateListTask());
		tasks.add(new LocationListTask());

		// Execute them and push results
		executeTasks(tasks, "");
		pushingTasksResults(tasks, wiki, "");

		// Handle dependent tasks
		final LinkedList<IWikiTask> dependentTasks = new LinkedList<>();
		dependentTasks.add(new AreaListTask());
		// TODO Temporarily disabled because it contains bugs that are not
		// likely to be fixed in the near future
		// dependentTasks.add(new LocateRegionTask());

		// Execute them and push results
		executeTasks(dependentTasks, "dependent ");
		pushingTasksResults(dependentTasks, wiki, "dependent ");

		System.out.println("Terminated.");
	}

	private static void executeTasks(final List<IWikiTask> tasks, final String loggingAdjective) {
		// Execute all tasks
		System.out.println("Executing " + loggingAdjective + "tasks...");
		int counter = 0;
		for (final IWikiTask task : tasks) {
			task.executeCommand();

			counter++;
			System.out.println("\tFinished (" + counter + "/" + tasks.size() + ")");
		}
	}

	private static void pushingTasksResults(final List<IWikiTask> tasks, final WikiHub wiki,
			final String loggingAdjective) {
		// Push all task results to the wiki
		System.out.println("Pushing " + loggingAdjective + "results...");
		int counter = 0;
		for (final IWikiTask task : tasks) {
			task.pushToWiki(wiki);

			counter++;
			System.out.println("\tFinished (" + counter + "/" + tasks.size() + ")");
		}
	}
}
