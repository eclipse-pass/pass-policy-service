package org.eclipse.pass.policy.rules;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.pass.policy.interfaces.Evaluation;
import org.eclipse.pass.policy.interfaces.VariableResolver;
import org.json.JSONObject;

/**
 * Represents the Condition object
 * Condition in the policy rules DSL is a JSON object that determines whether a
 * policy applies or not.
 * It is generally of the form:
 * {
 * "anyOf": [
 * {"equals":{"one":"two"}},
 * {"endsWith":{"one":"gone"}}
 * ]
 * }
 *
 * @author David McIntyre
 */
public class Condition {

    private JSONObject conditions;

    public Condition(JSONObject conditions) {
        this.conditions = conditions;
    }

    public Boolean apply(VariableResolver variables) throws Exception {
        Boolean passes;

        for (String cond : conditions.keySet()) {
            passes = false;
            try {
                switch (cond) {
                    case "endsWith":
                        passes = endsWith(conditions.get(cond), variables);
                        break;
                    case "equals":
                        passes = equals(conditions.get(cond), variables);
                        break;
                    case "anyOf":
                        passes = anyOf(conditions.getJSONArray(cond).toList(), variables);
                        break;
                    case "noneOf":
                        passes = noneOf(conditions.getJSONArray(cond).toList(), variables);
                        break;
                    case "contains":
                        passes = contains(conditions.get(cond), variables);
                        break;
                    default:
                        throw new Exception("Unknown condition " + cond);
                }

                if (!passes) {
                    return false;
                }

            } catch (Exception e) {
                throw new Exception("Could not evaluate condition " + cond, e);
            }
        }

        return true;
    }

    public Boolean endsWith(Object fromCondition, VariableResolver variables) throws Exception {
        Evaluation test = (a, b) -> a.toString().endsWith(b.toString());
        return eachPair(fromCondition, variables, test);
    }

    public Boolean equals(Object fromCondition, VariableResolver variables) throws Exception {
        Evaluation test = (a, b) -> a.toString().equals(b.toString());
        return eachPair(fromCondition, variables, test);
    }

    public Boolean contains(Object fromCondition, VariableResolver variables) throws Exception {
        Evaluation test = (a, b) -> a.toString().contains(b.toString());
        return eachPair(fromCondition, variables, test);
    }

    public Boolean anyOf(List<Object> arg, VariableResolver variables) throws Exception {
        if (!(arg instanceof List)) {
            throw new Exception("Expecting a list, but got " + arg.getClass());
        }

        List<Object> list = arg;
        Boolean passes;

        for (Object item : list) {
            passes = false;
            if (!(item instanceof JSONObject)) {
                throw new Exception("Expecting a JSON object as a list item, but got " + item.getClass());
            }

            JSONObject object = (JSONObject) item;
            try {
                passes = new Condition(object).apply(variables);
            } catch (Exception e) {
                throw new Exception("Condition failed to apply", e);
            }

            if (passes) {
                return true;
            }
        }

        return false;
    }

    public Boolean noneOf(List<Object> arg, VariableResolver variables) throws Exception {
        Boolean passes = anyOf(arg, variables);
        return !passes;
    }

    public Boolean eachPair(Object source, VariableResolver variables, Evaluation test) throws Exception {
        if (!(source instanceof JSONObject)) {
            throw new Exception("Expecting a JSON object, instead got a " + source.getClass());
        }

        JSONObject operands = (JSONObject) source;

        // If there's no resolver, just pass through.
        if (variables == null) {
            variables = new Variable("");
        }

        for (String b : operands.keySet()) {
            if (!(operands.get(b) instanceof URI)) {
                throw new Exception("Given a " + source.getClass() + " instead of a URI");
            }

            URI variable = (URI) operands.get(b);
            String a;

            try {
                // a = singleValued(variables.resolve(variable));
                // b = singleValued(variables.resolve(b));

                // if (!test.eval(a, b)) {
                // return false;
                // }
            } catch (Exception e) {
                throw new Exception("Could not resolve variable " + variable);
            }
        }

        return true;
    }

    public String singleValued(List<URI> list) throws Exception {
        if (list.isEmpty()) {
            return "";
        }

        if (list.size() != 1) {
            throw new Exception("Expecting single valued string, instead got " + list);
        }

        return list.get(0).toString();
    }
}
