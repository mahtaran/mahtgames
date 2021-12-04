package com.amuzil.mahtaran.mahtgames.skript.effect.game;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.amuzil.mahtaran.mahtgames.Game;
import com.amuzil.mahtaran.mahtgames.MahtGames;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Cancel Start Game")
@Description("Cancels the launch of a game.")
@Examples({
		"command cancel:",
		"\ttrigger:",
		"\tstart game \"test\":",
		"\tcancel start of game \"test\"",
		"\tsend \"Ups wrong command :(\""
})
@Since("0.0.1-alpha")
public class CancelStartGameEffect extends Effect {
	public static void register() {
		Skript.registerEffect(CancelStartGameEffect.class,
				"cancel [the] start of %game%",
				"%game%'s cancel start"
		);
	}

	private Expression<Game> gameExpression;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
		gameExpression = (Expression<Game>) expressions[0];
		return true;
	}

	@Override
	protected void execute(@NotNull Event event) {
		if (gameExpression.getSingle(event) == null) {
			MahtGames.error("Can't start a game \"null\"");
			return;
		}
		Game currentGame = gameExpression.getSingle(event);
		if (currentGame != null) {
			if (currentGame.getState() == Game.State.STARTING) {
				currentGame.setState(Game.State.WAITING);
			} else {
				MahtGames.error("Can't start the game \"" + currentGame.getInternalName() + "\": you don't have any team created.");
			}
		}
	}

	@Override
	@NotNull
	public String toString(Event event, boolean debug) {
		Game game = gameExpression.getSingle(event);
		String gameName = game != null ? game.getInternalName() : "null";
		return "Start game \"" + gameName + "\"";
	}
}
