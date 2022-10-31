package rules;

import components.Repository;

public class Requirements {

    private Repository[] required; //json:"required"
    private Repository[][] oneOf; //json:"one-of"
    private Repository[] optional; //json:"optional"

}
