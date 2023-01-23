package me.szindzeks.ChunkyPlots.commands.plot.subcommands;

import me.szindzeks.ChunkyPlots.commands.Subcommand;
import me.szindzeks.ChunkyPlots.guis.MenuGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotGuiCommand extends Subcommand {
	@Override
	public String getName() {
		return "gui";
	}

	@Override
	public String getDescription() {
		return "Otwiera GUI służące graficznej obsłudze pluginu";
	}

	@Override
	public String getSyntax() {
		return "/plot gui";
	}

	@Override
	public String getPermission() {
		return "chunkyplots.gui";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			player.openInventory(new MenuGUI(null).getInventory());
		}
	}
}
