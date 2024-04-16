package oogasalad.model.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

/**
 * Represents the JSON data for all rules (collision, static check) in the game.
 *
 * @author Judy He, Alisha Zhang
 */
@JsonPropertyOrder({"collisions", "turn_policy", "round_policy", "win_condition", "advance_turn",
    "advance_round"})
public record Rules(List<CollisionRule> collisions,
                    @JsonProperty("turn_policy") String turnPolicy,
                    @JsonProperty("round_policy") Map<String, List<Double>> roundPolicy,
                    @JsonProperty("win_condition") Map<String, List<Double>> winCondition,
                    @JsonProperty("advance_turn") List<Map<String, List<Double>>> advanceTurn,
                    @JsonProperty("advance_round") List<Map<String, List<Double>>> advanceRound,
                    @JsonProperty("strike_policy") String strikePolicy
) {

}
