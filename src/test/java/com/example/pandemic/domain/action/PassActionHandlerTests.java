package com.example.pandemic.domain.action;

import com.example.pandemic.application.action.PassAction;
import com.example.pandemic.support.GameBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PassActionHandlerTests {

    private final PassActionHandler handler = new PassActionHandler();

    @Test
    public void shouldPassActionSuccessAndSetNumberOfAvailableActionsToZero() {
        var game = GameBuilder.aGame().build();
        var actionRequest = new PassAction();

        var result = handler.handle(game, actionRequest);

        assertThat(result.isSuccess()).isTrue();
        assertThat(game.getActivePlayer().getNumberOfAvailableActions()).isEqualTo(0);
    }
}
