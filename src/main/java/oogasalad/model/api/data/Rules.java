package oogasalad.model.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Represents the JSON data for all rules (collision, static check) in the game.
 *
 * @author Judy He, Alisha Zhang
 */
public record Rules(List<CollisionRule> collisions,
                    @JsonProperty("static_checks") List<StaticCheckRule> staticChecks,
                    @JsonProperty("turn_policy") String turnPolicy,
                    @JsonProperty("round_policy") String roundPolicy,
                    @JsonProperty("win_condition") Map<String, List<Double>> winCondition,
                    List<Map<String, List<Double>>> advance) {

}

