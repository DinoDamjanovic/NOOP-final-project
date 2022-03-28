package memory_game_client.view.commands;

import memory_game_client.controller.Controller;

/**
 * Sends a rematch request to the opponent upon execution.
 */
public class RematchCmd implements ICommand {

    private Controller controller;

    public RematchCmd(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.sendRematchRequest();
    }
}
