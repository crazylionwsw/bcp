package com.fuze.bcp.drools.filter;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

import java.util.List;

public class BcpAgendaFilter implements AgendaFilter {

    private List<String> ruleNames;

    public boolean accept(Match match) {
        String ruleName = match.getRule().getName();
        return ruleNames.contains(ruleName);
    }

    public BcpAgendaFilter(List ruleNames) {
        this.ruleNames = ruleNames;
    }
}