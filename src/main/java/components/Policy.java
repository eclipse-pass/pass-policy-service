package components;

import rules.Condition;

public class Policy {

    private String id; //json:"policy-id"
    private String description; //json:"description"
    private String type; //json:"type"
    private Repository[] respositories; //json:"repositories"
    private Condition[] conditions; //json:"conditions"

}
